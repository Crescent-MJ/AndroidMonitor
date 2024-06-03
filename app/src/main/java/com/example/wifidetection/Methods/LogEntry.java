package com.example.wifidetection.Methods;

public class LogEntry {
    private String timestamp;
    private String event;
    private String details;

    public LogEntry(String timestamp, String event, String details) {
        this.timestamp = timestamp;
        this.event = event;
        this.details = details;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getEvent() {
        return event;
    }

    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        return "LogEntry{" +
                "timestamp='" + timestamp + '\'' +
                ", event='" + event + '\'' +
                ", details='" + details + '\'' +
                '}';
    }
}
