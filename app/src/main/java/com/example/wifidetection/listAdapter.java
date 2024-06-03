package com.example.wifidetection;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wifidetection.data.detectionData;

import java.util.ArrayList;
import java.util.List;

public class listAdapter extends RecyclerView.Adapter<listAdapter.detectionListHolder> {

    private TextView tvHost;
    private TextView tvPath;
    private TextView tvRequestDate;
    private TextView tvDuration;
    private TextView tvCode;
    private TextView tvMethod;
    private TextView tvSource;

    private OnItemClickListener itemClick;

    private List<detectionData> monitorList = new ArrayList<>();

    @Override
    public detectionListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new detectionListHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_adapter, parent, false)
        );
    }

    @Override
    public int getItemCount() {
        return monitorList.size();
    }

    @Override
    public void onBindViewHolder(detectionListHolder holder, int position) {
        holder.bindData(monitorList.get(position));
    }

    public void setData(List<detectionData> list) {
        this.monitorList = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClick = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(detectionData monitorData);
    }

    public class detectionListHolder extends RecyclerView.ViewHolder {

        private detectionData monitorData;

        public detectionListHolder(View containerView) {
            super(containerView);
            tvHost = containerView.findViewById(R.id.tv_host);
            tvPath = containerView.findViewById(R.id.tv_path);
            tvRequestDate = containerView.findViewById(R.id.tv_request_date);
            tvDuration = containerView.findViewById(R.id.tv_duration);
            tvCode = containerView.findViewById(R.id.tv_code);
            tvMethod = containerView.findViewById(R.id.tv_method);
            tvSource = containerView.findViewById(R.id.tv_source);

            containerView.setOnClickListener(v -> {
                if (itemClick != null && monitorData != null) {
                    itemClick.onItemClick(monitorData);
                }
            });

            containerView.setOnClickListener(v -> {
                if (itemClick != null && monitorData != null) {
                    itemClick.onItemClick(monitorData);
                }
            });
        }

        public void bindData(detectionData monitorData) {
            this.monitorData = monitorData;

            tvHost.setText(monitorData.getHost());
            tvPath.setText(monitorData.getPath());
            tvRequestDate.setText(monitorData.getRequestTime());
            tvDuration.setVisibility(monitorData.getDuration() <= 0 ? View.GONE : View.VISIBLE);
            tvDuration.setText(monitorData.getDuration() + "ms");
            tvCode.setText(String.valueOf(monitorData.getResponseCode()));
            tvMethod.setText(monitorData.getMethod());
            tvSource.setText(monitorData.getSource());
            tvSource.setVisibility(monitorData.getSource() == null || monitorData.getSource().isEmpty() ? View.GONE : View.VISIBLE);

            tvPath.setTextColor(getColor(monitorData.getResponseCode()));
            tvCode.setTextColor(getColor(monitorData.getResponseCode()));
        }

        private int getColor(int code) {
            switch (code) {
                case 200:
                    return ContextCompat.getColor(itemView.getContext(), R.color.monitor_status_success);
                case 300:
                    return ContextCompat.getColor(itemView.getContext(), R.color.monitor_status_300);
                case 400:
                    return ContextCompat.getColor(itemView.getContext(), R.color.monitor_status_400);
                case 500:
                    return ContextCompat.getColor(itemView.getContext(), R.color.monitor_status_500);
                default:
                    return ContextCompat.getColor(itemView.getContext(), R.color.monitor_status_error);
            }
        }
    }
}

