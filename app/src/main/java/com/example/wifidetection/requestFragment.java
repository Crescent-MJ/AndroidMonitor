package com.example.wifidetection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wifidetection.data.detectionData;

public class requestFragment extends Fragment {

    private detectionData monitorData;
    private TextView tvUrl;
    private TextView tvMethod;
    private TextView tvRequestDate;
    private TextView tvHeader;
    private TextView tvRequestBody;

    public static requestFragment newInstance(detectionData monitorData) {
        requestFragment fragment = new requestFragment();
        fragment.monitorData = monitorData;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor_request, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvUrl = view.findViewById(R.id.tv_url);
        tvMethod = view.findViewById(R.id.tv_method);
        tvRequestDate = view.findViewById(R.id.tv_request_date);
        tvHeader = view.findViewById(R.id.tv_header);
        tvRequestBody = view.findViewById(R.id.tv_request_body);
        initData();
    }

    private void initData() {
        tvUrl.setText(monitorData.getUrl());
        tvMethod.setText(monitorData.getMethod());
        tvRequestDate.setText(monitorData.getRequestTime());
        tvHeader.setText(monitorData.getSource().equals("Flutter") ? formatBody(monitorData.getRequestHeaders(), "json") : monitorData.getRequestHeaders());
        if (monitorData.getRequestBody() != null && !monitorData.getRequestBody().isEmpty()) {
            tvRequestBody.setText(formatBody(monitorData.getRequestBody(), monitorData.getRequestContentType()));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        monitorData = null;
    }

    private String formatBody(String body, String contentType) {
        // Implement the formatBody logic here
        return body;
    }

}
