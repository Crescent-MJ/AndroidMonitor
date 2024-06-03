package com.example.wifidetection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.wifidetection.data.detectionData;

public class detectionMainFragment extends Fragment {

    private listAdapter adapter;
    private Handler handle = new Handler(Looper.getMainLooper());
    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView rv_monitor;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_monitor_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        swipeRefresh = view.findViewById(R.id.swipe_refresh);
        rv_monitor = view.findViewById(R.id.rv_monitor);

        swipeRefresh.setOnRefreshListener(() -> {
            handle.postDelayed(() -> {
                swipeRefresh.setRefreshing(false);
                setData();
            }, 1000);
        });

        initRv();
        setData();
    }

    private void setData() {
        detectionHelper.getMonitorDataListForAndroid(100, 0)
                .observe(getViewLifecycleOwner(), adapter::setData);
    }

    private void initRv() {
        adapter = new listAdapter();
        adapter.setOnItemClickListener(this::gotoMonitorDetail);
        rv_monitor.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_monitor.setAdapter(adapter);
    }

    private void gotoMonitorDetail(detectionData monitorData) {
        startActivity(detailActivity.buildIntent(requireContext(), monitorData));
    }
}



