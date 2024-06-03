package com.example.wifidetection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.wifidetection.data.detectionData;
import com.google.android.material.tabs.TabLayout;

public class detailActivity extends AppCompatActivity {

    private static detectionData monitorData = null;
    private ImageView iv_back;
    private TextView tv_title;
    private ViewPager viewPager;
    private TabLayout tab_layout;

    public static Intent buildIntent(Context context, detectionData data) {
        Intent intent = new Intent(context, detailActivity.class);
        monitorData = data;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail); // Replace with your actual layout resource ID

        iv_back = findViewById(R.id.iv_back); // Replace with actual ID
        tv_title = findViewById(R.id.tv_title); // Replace with actual ID
        viewPager = findViewById(R.id.viewPager); // Replace with actual ID
        tab_layout = findViewById(R.id.tab_layout); // Replace with actual ID

        initView();
    }

    private void initView() {
        iv_back.setOnClickListener(v -> finish());
        tv_title.setText(monitorData != null ? monitorData.getPath() : "");

        pagerAdapter fragmentPagerAdapter = new pagerAdapter(null);
        fragmentPagerAdapter.addFragment(responseFragment.newInstance(monitorData), "响应");
        fragmentPagerAdapter.addFragment(requestFragment.newInstance(monitorData), "请求");

        viewPager.setAdapter(fragmentPagerAdapter);
        tab_layout.setupWithViewPager(viewPager);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        monitorData = null;
    }
}
