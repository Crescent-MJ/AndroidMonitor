package com.example.wifidetection.interceptor;

import static com.example.wifidetection.OkHttpUtils.headersToJsonString;
import static com.example.wifidetection.detectionHelper.context;
import static com.example.wifidetection.detectionHelper.isWhiteContentType;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wifidetection.data.detectionData;
import com.example.wifidetection.detectionHelper;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;

import okhttp3.Protocol;
import okhttp3.ResponseBody;

import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

public class MonitorInterceptor implements Interceptor {
    private static final String TAG = "MonitorInterceptor";
    private static long maxContentLength = 1024 * 1024;
    private static final int PING_INTERVAL = 1000; // 每秒ping一次
    private static final int PING_DURATION = 10000; // 10秒内的波动检测
    private static final int PING_THRESHOLD = 100; // 100ms的波动阈值

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (!detectionHelper.isOpenMonitor()) {
            return chain.proceed(request);
        }

        detectionData monitorData = new detectionData();
        monitorData.setMethod(request.method());
        String url = request.url().toString();
        monitorData.setUrl(url);

        if (!url.isEmpty()) {
            Uri uri = Uri.parse(url);
            monitorData.setHost(uri.getHost());
            monitorData.setPath(uri.getPath() + (uri.getQuery() != null ? "?" + uri.getQuery() : ""));
            monitorData.setScheme(uri.getScheme());

            // Ping值波动检测
            if (isPingFluctuationTooHigh(uri.getHost())) {
                showBlacklistAlert();
                return new Response.Builder()
                        .code(400)
                        .message("High Ping Fluctuation")
                        .protocol(Protocol.HTTP_1_1)
                        .request(request)
                        .build();
            }
        }

        if (!monitorData.isWhiteHosts()) {
            return chain.proceed(request);
        }

        if (monitorData.isBlackHosts()) {
            // 如果检测到黑名单，向用户发出警告并建议断开连接
            showBlacklistAlert();
            // 返回空的响应，终止拦截器链
            return new Response.Builder()
                    .code(400)
                    .message("Blacklisted Host")
                    .protocol(Protocol.HTTP_1_1)
                    .request(request)
                    .build();
        }

        if (detectionHelper.isFilterIPAddressHost() && monitorData.isIpAddress()) {
            return chain.proceed(request);
        }

        RequestBody requestBody = request.body();
        monitorData.setRequestTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()));

        if (requestBody != null && requestBody.contentType() != null) {
            monitorData.setRequestContentType(requestBody.contentType().toString());
        }

        long startTime = System.nanoTime();
        Response response;

        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            monitorData.setErrorMsg(e.toString());
            insert(monitorData);
            throw e;
        }

        try {
            monitorData.setRequestHeaders(headersToJsonString(response.request().headers()));

            String contentType = response.headers().get("Content-Type");
            if (!isWhiteContentType(contentType)) {
                return response;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = dateFormat.format(new Date());
            monitorData.setResponseTime(formattedDate);
            monitorData.setDuration(TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime));
            monitorData.setProtocol(response.protocol().toString());
            monitorData.setResponseCode(response.code());
            monitorData.setResponseMessage(response.message());

            //RequestBody requestBody = request.body();
            if (requestBody == null || bodyHasUnknownEncoding(request.headers()) || requestBody.isDuplex() || requestBody.isOneShot()) {
                // do nothing
            } else if (requestBody instanceof MultipartBody) {
                MultipartBody multipartBody = (MultipartBody) requestBody;
                StringBuilder formatRequestBody = new StringBuilder();
                for (MultipartBody.Part part : multipartBody.parts()) {
                    boolean isStream = part.body().contentType().toString().contains("otcet-stream");
                    String key = part.headers().value(0);
                    if (isStream) {
                        formatRequestBody.append(key).append("; value=文件流\n");
                    } else {
                        Buffer buffer = new Buffer();
                        part.body().writeTo(buffer);
                        String value = buffer.readUtf8();
                        formatRequestBody.append(key).append("; value=").append(value).append("\n");
                    }
                }
                monitorData.setRequestBody(formatRequestBody.toString());
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = requestBody.contentType() != null ?
                        requestBody.contentType().charset(StandardCharsets.UTF_8) :
                        StandardCharsets.UTF_8;
                String body = buffer.readString(charset);
                monitorData.setRequestBody(body);
            }

            ResponseBody responseBody = response.body();

            if (responseBody != null && bodyHasUnknownEncoding(response.headers())) {
                Buffer buffer = new Buffer();
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.

                buffer = source.buffer();

                if (bodyGzipped(response.headers())) {
                    try (GzipSource gzippedResponseBody = new GzipSource(buffer.clone())) {
                        buffer = new Buffer();
                        buffer.writeAll(gzippedResponseBody);
                    }
                }

                Charset charset = responseBody.contentType() != null ?
                        responseBody.contentType().charset(StandardCharsets.UTF_8) :
                        StandardCharsets.UTF_8;

                String body = readFromBuffer(buffer.clone(), charset);
                monitorData.setResponseBody(body);
                monitorData.setResponseContentLength(buffer.size());
            }

            insert(monitorData);
            return response;
        } catch (Exception e) {
            Log.d("MonitorHelper", e.getMessage() != null ? e.getMessage() : "");
            return response;
        }
    }

    private boolean isPingFluctuationTooHigh(String host) {
        // 模拟Ping操作并检查波动情况
        long[] pings = new long[PING_DURATION / PING_INTERVAL];
        for (int i = 0; i < pings.length; i++) {
            pings[i] = getPing(host);
            try {
                Thread.sleep(PING_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long minPing = Long.MAX_VALUE;
        long maxPing = Long.MIN_VALUE;
        for (long ping : pings) {
            if (ping < minPing) minPing = ping;
            if (ping > maxPing) maxPing = ping;
        }

        return (maxPing - minPing) > PING_THRESHOLD;
    }

    private long getPing(String host) {
        // 模拟获取Ping值的逻辑
        // 具体实现根据实际情况编写
        try {
            Process process = Runtime.getRuntime().exec("/system/bin/ping -c 1 " + host);
            int status = process.waitFor();
            if (status == 0) {
                // 解析ping命令的输出获取时间
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String output;
                while ((output = bufferedReader.readLine()) != null) {
                    if (output.contains("time=")) {
                        int index = output.indexOf("time=");
                        String time = output.substring(index + 5, output.indexOf("ms", index)).trim();
                        return Long.parseLong(time);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }

    private void insert(detectionData monitorData) {
        detectionHelper.insert(monitorData);
    }

    private void update(detectionData monitorData) {
        detectionHelper.update(monitorData);
    }

    private boolean bodyHasUnknownEncoding(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity") &&
                !contentEncoding.equalsIgnoreCase("gzip");
    }

    public static String readFromBuffer(Buffer buffer, Charset charset) {
        int bufferSize = (int) buffer.size();
        int maxBytes = (int) Math.min(bufferSize, maxContentLength);
        String body;
        try {
            body = buffer.readString(maxBytes, charset);
        } catch (EOFException e) {
            body = "\n\n--- Unexpected end of content ---";
        }
        if (bufferSize > maxContentLength) {
            body += "\n\n--- Content truncated ---";
        }
        return body;
    }


    private boolean bodyGzipped(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && contentEncoding.equalsIgnoreCase("gzip");
    }

    private void showBlacklistAlert() {
        // 向用户显示警告，并建议断开与黑名单WiFi的连接
        AlertDialog.Builder builder = new AlertDialog.Builder(context); // 请替换为正确的上下文
        builder.setTitle("警告/Warning");
        builder.setMessage("您当前连接的WiFi存在安全风险，请断开连接并更换WiFi/There is a security risk with the WiFi you are currently connected to. Please disconnect and switch to another WiFi.");
        builder.setPositiveButton("确定/OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 用户点击确定时执行的操作，可以在此处添加断开WiFi连接的逻辑
                disconnectFromCurrentWifi();
            }
        });
        builder.setNegativeButton("取消/Cancel", null);
        builder.show();
    }
    private void disconnectFromCurrentWifi() {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            // 禁用当前WiFi
            wifiManager.disconnect();
        }
    }

}
