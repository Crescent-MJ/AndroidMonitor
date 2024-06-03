package com.example.wifidetection;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wifidetection.data.detectionData;

public class responseFragment extends Fragment {
    private detectionData monitorData;
    private TextView tvUrl;
    private TextView tvMethod;
    private TextView tvCode;
    private TextView tvResponseDate;
    private TextView tvResponseBody;

    public static responseFragment newInstance(detectionData monitorData) {
        responseFragment fragment = new responseFragment();
        fragment.monitorData = monitorData;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor_response, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUrl = view.findViewById(R.id.tv_url);
        tvMethod = view.findViewById(R.id.tv_method);
        tvCode = view.findViewById(R.id.tv_code);
        tvResponseDate = view.findViewById(R.id.tv_response_date);
        tvResponseBody = view.findViewById(R.id.tv_response_body);

        initData();
    }

    private void initData() {
        if (monitorData != null) {
            tvUrl.setText(monitorData.getUrl());
            tvMethod.setText(monitorData.getMethod());
            tvCode.setText(String.valueOf(monitorData.getResponseCode()));
            tvResponseDate.setText(monitorData.getResponseTime());

            String responseBody = "";
            if ("Flutter".equals(monitorData.getSource())) {
                responseBody = formatBody(monitorData.getResponseBody() != null ? monitorData.getResponseBody() : "", "json");
            } else {
                responseBody = monitorData.getResponseBody();
            }
            String responseType = monitorData.getResponseContentType();

            if (TextUtils.isEmpty(responseBody)) {
                tvResponseBody.setText(monitorData.getErrorMsg() != null ? monitorData.getErrorMsg() : monitorData.getResponseMessage());
            } else {
                tvResponseBody.setText(formatBody(responseBody, responseType));
            }
        } else {
            tvUrl.setText("");
            tvMethod.setText("");
            tvCode.setText("");
            tvResponseDate.setText("");
            tvResponseBody.setText("");
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        monitorData = null;
    }

    private String formatBody(String body, String type) {
        // Implement the formatBody method to format the response body based on the content type
        return body;
    }
}
