package com.example.wifidetection.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.example.wifidetection.R;

import java.util.List;

public class MainWifi extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private TextView wifiInfoTextView;
    private Switch wifiSwitch;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_wifi);

        findViewById(R.id.Action).setOnClickListener(v -> startActivity(new Intent(MainWifi.this, Action_Module.class)));

        wifiInfoTextView = findViewById(R.id.wifiInfoTextView);
        wifiSwitch = findViewById(R.id.wifiSwitch);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        setupWifiSwitch();

        // 获取并显示WiFi信息
        updateWifiInfo();

        // 设置Switch状态并监听状态变化
        wifiSwitch.setChecked(wifiManager.isWifiEnabled());
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 切换WiFi状态
                wifiManager.setWifiEnabled(isChecked);
                // 更新WiFi信息显示
                if (isChecked) {
                    updateWifiInfo();
                } else {
                    wifiInfoTextView.setText("No WiFi Connection");
                }
            }
        });
    }

    private void setupWifiSwitch() {
        // 设置 WiFi 开关的状态和监听器
        wifiSwitch.setChecked(wifiManager.isWifiEnabled());
        wifiSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 切换 WiFi 状态
                wifiManager.setWifiEnabled(isChecked);
                if (isChecked) {
                    // 如果开关打开，连接到已保存的 WiFi 网络
                    connectToSavedWifi();
                }
            }
        });
    }

    private void connectToSavedWifi() {
        // Get the saved WiFi configuration information
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
        if (configuredNetworks != null) {
            // Iterate over the saved network configuration
            for (WifiConfiguration config : configuredNetworks) {
                if (config.status == WifiConfiguration.Status.CURRENT) {
                    // If the currently connected network configuration is found, enable the network configuration connection
                    wifiManager.enableNetwork(config.networkId, true);
                    break;
                }
            }
        }
    }

    private void updateWifiInfo() {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        String ip = android.text.format.Formatter.formatIpAddress(ipAddress);

        String macAddress = wifiInfo.getMacAddress();

        String gateway = intToIp(wifiManager.getDhcpInfo().gateway);

        String subnetMask = intToIp(wifiManager.getDhcpInfo().netmask);

        String dns1 = intToIp(wifiManager.getDhcpInfo().dns1);
        String dns2 = intToIp(wifiManager.getDhcpInfo().dns2);

        int linkSpeed = wifiInfo.getLinkSpeed(); // in Mbps

        String wifiInfoText = "IP Address: " + ip + "\n" +
                "Device MAC Address: " + macAddress + "\n" +
                "Gateway: " + gateway + "\n" +
                "Subnet Mask: " + subnetMask + "\n" +
                "DNS 1: " + dns1 + "\n" +
                "DNS 2: " + dns2 + "\n" +
                "Transmit Link Speed: " + linkSpeed + " Mbps\n" +
                "Receive Link Speed: " + linkSpeed + " Mbps";

        wifiInfoTextView.setText(wifiInfoText);
    }

    private String intToIp(int ipAddress) {
        return ((ipAddress & 0xFF) + "." +
                ((ipAddress >> 8) & 0xFF) + "." +
                ((ipAddress >> 16) & 0xFF) + "." +
                ((ipAddress >> 24) & 0xFF));
    }

}