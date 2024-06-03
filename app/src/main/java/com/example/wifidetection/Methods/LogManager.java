package com.example.wifidetection.Methods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogManager {
    private static LogManager instance;
    private List<LogEntry> logs;
    private File logFile;

    private LogManager(File logFile) {
        this.logs = new ArrayList<>();
        this.logFile = logFile;
        loadLogsFromFile();
    }

    public static synchronized LogManager getInstance(File logFile) {
        if (instance == null) {
            instance = new LogManager(logFile);
        }
        return instance;
    }

    public void logEvent(String event, String details) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        LogEntry logEntry = new LogEntry(timestamp, event, details);
        logs.add(logEntry);
        saveLogToFile(logEntry);
    }

    public List<LogEntry> getLogs() {
        return new ArrayList<>(logs);  // Return a copy to prevent external modification
    }

    private void saveLogToFile(LogEntry logEntry) {
        try {
            FileWriter writer = new FileWriter(logFile, true);
            writer.append(logEntry.toString()).append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLogsFromFile() {
        if (logFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(logFile));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(", ");
                    if (parts.length == 3) {
                        logs.add(new LogEntry(parts[0].split("=")[1], parts[1].split("=")[1], parts[2].split("=")[1].replace("}", "")));
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearLogs() {
        logs.clear();
        if (logFile.exists()) {
            logFile.delete();
        }
    }
}

