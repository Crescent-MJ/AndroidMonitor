package com.example.wifidetection.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wifidetection.R;
import com.example.wifidetection.data.detectionData;
import com.example.wifidetection.detectionHelper;
import com.example.wifidetection.detectionMainFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Action_Module extends AppCompatActivity {
    private Handler handler = new Handler(Looper.getMainLooper());
    private TextView tvTitle;
    private TextView tvClean;
    private Switch detectionSwitchBtn;

    private ViewPager2 viewPager;
    private static final String PREFS_NAME = "MonitorConfigPrefs";
    private static final String DETECTION_SWITCH_KEY = "detection_switch";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_module);
        detectionSwitchBtn = findViewById(R.id.capture_switch);

        findViewById(R.id.Main).setOnClickListener(v -> {
            startActivity(new Intent(Action_Module.this, MainWifi.class));
        });

        initView();
        initPage();

        initMonitorView();
        initDetectionListener();

        String dbName = "detectionDatabase";
        detectionHelper.initMonitorDataDao(getApplicationContext(), dbName);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
    private void initView() {
        tvTitle = findViewById(R.id.tvTitle);
        tvClean = findViewById(R.id.tvClean);
        viewPager = findViewById(R.id.viewPager);
        // 设置清理按钮点击事件

        tvClean.setOnClickListener(v -> {
            new Thread(() -> {
                if (detectionHelper.isDetectionDataNull()) {
                    detectionHelper.deleteAll();
                } else {
                    runOnUiThread(() -> Toast.makeText(Action_Module.this, "The current content is empty", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });
    }
    private void initPage() {
        final List<Fragment> fragments = Arrays.asList(new detectionMainFragment());

        if (viewPager != null) {
            viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager.setAdapter(new FragmentStateAdapter(this) {
                @Override
                public int getItemCount() {
                    return fragments.size();
                }

                @Override
                public Fragment createFragment(int position) {
                    return fragments.get(position);
                }
            });
            viewPager.setOffscreenPageLimit(fragments.size());
        } else {
            // 处理 viewPager 为空的情况
            Toast.makeText(this, "ViewPager is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void Page(List<detectionData> dataList) {
        final List<Fragment> fragments = Collections.singletonList(new detectionMainFragment());

        // 根据监测数据列表动态创建 Fragment
        for (detectionData data : dataList) {
            detectionMainFragment fragment = new detectionMainFragment();
            Bundle bundle = new Bundle();
            bundle.putString("responseTime", data.getResponseTime()); // 假设 detectionData 类有一个 getResponseTime() 方法返回响应时间
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }

        if (viewPager != null) {
            viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
            viewPager.setAdapter(new FragmentStateAdapter(this) {
                @Override
                public int getItemCount() {
                    return fragments.size();
                }

                @Override
                public Fragment createFragment(int position) {
                    return fragments.get(position);
                }
            });
            viewPager.setOffscreenPageLimit(fragments.size());
        } else {
            // 处理 viewPager 为空的情况
            Toast.makeText(this, "ViewPager is null", Toast.LENGTH_SHORT).show();
        }
    }


    private void initMonitorView() {
        detectionSwitchBtn = findViewById(R.id.capture_switch);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isOpenMonitor = settings.getBoolean(DETECTION_SWITCH_KEY, false);
        detectionSwitchBtn.setChecked(isOpenMonitor);
    }

    private void initDetectionListener() {
        detectionSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                detectionHelper.isOpenMonitor = isChecked;
                Toast.makeText(Action_Module.this, isChecked ? "检测功能已开启/Detection is Open" : "检测功能已关闭/Detection is Close", Toast.LENGTH_SHORT).show();

                // 开关状态改变后，更新数据
                if (isChecked) {
                    handler.post(updateTask); // 开启定时任务
                } else {
                    handler.removeCallbacks(updateTask); // 停止定时任务
                }
            }
        });
    }
    private Runnable updateTask = new Runnable() {
        @Override
        public void run() {
            if (detectionHelper.isOpenMonitor) {
                updateData();
                handler.postDelayed(this, 5000); // 每5秒更新一次数据
            } else {
                // 如果开关状态为关闭，停止执行任务
                handler.removeCallbacks(this); // 停止任务的执行
            }
        }
    };

    private void updateData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<detectionData> dataList = detectionHelper.getMonitorDataList(10, 0); // 获取最新的10条数据，从第0条开始
                // 更新UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Page(dataList);
                    }
                });
            }
        }).start();
    }

}