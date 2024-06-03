package com.example.wifidetection.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.wifidetection.detectionHelper;

import java.io.Serializable;
import java.util.regex.Pattern;

@Entity(tableName = "detection")
public class detectionData implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "duration")
    private long duration;

    @ColumnInfo(name = "method")
    private String method;

    @ColumnInfo(name = "url")
    private String url;

    @ColumnInfo(name = "host")
    private String host;

    @ColumnInfo(name = "path")
    private String path;

    @ColumnInfo(name = "scheme")
    private String scheme;

    @ColumnInfo(name = "protocol")
    private String protocol;

    @ColumnInfo(name = "requestTime")
    private String requestTime;

    @ColumnInfo(name = "requestHeaders")
    private String requestHeaders;

    @ColumnInfo(name = "requestBody")
    private String requestBody;

    @ColumnInfo(name = "requestContentType")
    private String requestContentType;

    @ColumnInfo(name = "responseCode")
    private int responseCode;

    @ColumnInfo(name = "responseTime")
    private String responseTime;

    @ColumnInfo(name = "responseHeaders")
    private String responseHeaders;

    @ColumnInfo(name = "responseBody")
    private String responseBody;

    @ColumnInfo(name = "responseMessage")
    private String responseMessage;

    @ColumnInfo(name = "responseContentType")
    private String responseContentType;

    @ColumnInfo(name = "responseContentLength")
    private Long responseContentLength;

    @ColumnInfo(name = "errorMsg")
    private String errorMsg;

    @ColumnInfo(name = "source")
    private String source;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getRequestHeaders() {
        return requestHeaders;
    }

    public void setRequestHeaders(String requestHeaders) {
        this.requestHeaders = requestHeaders;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getRequestContentType() {
        return requestContentType;
    }

    public void setRequestContentType(String requestContentType) {
        this.requestContentType = requestContentType;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(String responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getResponseContentType() {
        return responseContentType;
    }

    public void setResponseContentType(String responseContentType) {
        this.responseContentType = responseContentType;
    }

    public Long getResponseContentLength() {
        return responseContentLength;
    }

    public void setResponseContentLength(Long responseContentLength) {
        this.responseContentLength = responseContentLength;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    // Method to check if the host is in the white list
    public boolean isWhiteHosts() {
        return detectionHelper.whiteHosts != null && detectionHelper.whiteHosts.contains(this.host);
    }

    // Method to check if the host is in the black list
    public boolean isBlackHosts() {
        return detectionHelper.blackHosts != null && detectionHelper.blackHosts.contains(this.host);
    }

    // Method to check if the host is an IP address
    public boolean isIpAddress() {
        // Simple check to see if host is an IPv4 address
        Pattern ipPattern = Pattern.compile("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
        return ipPattern.matcher(this.host).matches();
    }

}