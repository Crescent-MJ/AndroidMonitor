package com.example.wifidetection;

import com.google.gson.Gson;

import java.io.EOFException;
import java.io.IOException;
import java.net.HttpURLConnection;

import okhttp3.ResponseBody;
import okio.Buffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.Response;

public class OkHttpUtils {
    public static String headersToJsonString(Headers headers) {
        if (headers != null) {
            List<HttpHeader> httpHeaders = new ArrayList<>();
            for (int i = 0; i < headers.size(); i++) {
                httpHeaders.add(new HttpHeader(headers.name(i), headers.value(i)));
            }
            return GsonHelper.toJson(httpHeaders);
        } else {
            return "";
        }
    }

    public static boolean promisesBody(Response response) {
        // HEAD requests never yield a body regardless of the response headers.
        if ("HEAD".equals(response.request().method())) {
            return false;
        }

        int responseCode = response.code();
        if ((responseCode < 100 || responseCode >= 200) && responseCode != HttpURLConnection.HTTP_NO_CONTENT && responseCode != HttpURLConnection.HTTP_NOT_MODIFIED) {
            return true;
        }

        // If the Content-Length or Transfer-Encoding headers disagree with the response code, the
        // response is malformed. For best compatibility, we honor the headers.
        if (headersContentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return true;
        }

        return false;
    }

    /** Returns the Content-Length as reported by the response headers. */
    public static long headersContentLength(Response response) {
        String contentLength = response.header("Content-Length");
        return contentLength != null ? toLongOrDefault(contentLength, -1L) : -1L;
    }

    public static long toLongOrDefault(String value, long defaultValue) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean isProbablyUtf8(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            int byteCount = (int) Math.min(buffer.size(), 64);
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    public static String readString(ResponseBody body) {
        String result = "";
        try {
            Buffer buffer = new Buffer();
            body.source().readAll(buffer);
            Charset charset = body.contentType() != null ? body.contentType().charset(StandardCharsets.UTF_8) : StandardCharsets.UTF_8;
            if (isProbablyUtf8(buffer)) {
                result = buffer.readString(charset);
            }
        } catch (IOException e) {
            // handle exception
        }
        return result;
    }

    static class HttpHeader {
        String name;
        String value;

        public HttpHeader(String name, String value) {
            this.name = name;
            this.value = value;
        }
    }

    static class GsonHelper {
        static String toJson(List<HttpHeader> httpHeaders) {
            Gson gson = new Gson();
            return gson.toJson(httpHeaders);
        }
    }
}
