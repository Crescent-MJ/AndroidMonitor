package com.example.wifidetection.data;

public class propertiesData {
    private String port;
    private String dbName;
    private String whiteContentTypes; // ContentType白名单
    private String whiteHosts; // host白名单
    private String blackHosts; // host黑名单
    private boolean isFilterIPAddressHost; // 是否过滤纯IP地址的host

    public propertiesData(String port, String dbName, String whiteContentTypes, String whiteHosts, String blackHosts, boolean isFilterIPAddressHost) {
        this.port = port;
        this.dbName = dbName;
        this.whiteContentTypes = whiteContentTypes;
        this.whiteHosts = whiteHosts;
        this.blackHosts = blackHosts;
        this.isFilterIPAddressHost = isFilterIPAddressHost;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getWhiteContentTypes() {
        return whiteContentTypes;
    }

    public void setWhiteContentTypes(String whiteContentTypes) {
        this.whiteContentTypes = whiteContentTypes;
    }

    public String getWhiteHosts() {
        return whiteHosts;
    }

    public void setWhiteHosts(String whiteHosts) {
        this.whiteHosts = whiteHosts;
    }

    public String getBlackHosts() {
        return blackHosts;
    }

    public void setBlackHosts(String blackHosts) {
        this.blackHosts = blackHosts;
    }

    public boolean isFilterIPAddressHost() {
        return isFilterIPAddressHost;
    }

    public void setFilterIPAddressHost(boolean filterIPAddressHost) {
        isFilterIPAddressHost = filterIPAddressHost;
    }
}
