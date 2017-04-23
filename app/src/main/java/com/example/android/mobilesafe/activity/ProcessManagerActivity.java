package com.example.android.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.engine.ProcessInfoProvider;

import org.w3c.dom.Text;

public class ProcessManagerActivity extends AppCompatActivity {

    private TextView mProcessCountText;
    private TextView mMemoryInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_manager);

        initTitle();
    }

    private void initTitle() {

        mProcessCountText = (TextView) findViewById(R.id.tv_process_count);

        mMemoryInfoText = (TextView) findViewById(R.id.tv_memory_info);

        mProcessCountText.setText("进程总数:" + ProcessInfoProvider.getProcessCount(this));
        mMemoryInfoText.setText("剩余/总共:" + ProcessInfoProvider.getAvailSpace(this) + "/" + ProcessInfoProvider.getTotalSpace(this));
    }
}
