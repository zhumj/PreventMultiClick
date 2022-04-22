package com.zhumj.preventmulticlick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.zhumj.preventmulticlick.aop.AOPActivity;
import com.zhumj.preventmulticlick.aop.AopClickUtil;
import com.zhumj.preventmulticlick.aop.PreventMultiClick;
import com.zhumj.preventmulticlick.listener.ListenerActivity;
import com.zhumj.preventmulticlick.listener.OnPreventMultiClickListener;

public class MainActivity extends AppCompatActivity {

    private Context mContext;
    private Button btn1, btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AopClickUtil.disable();

        mContext = this;

        initView();
        setViewListener();
    }

    private void initView() {
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
    }

    private void setViewListener() {
        btn1.setOnClickListener(mClickListener);
        btn2.setOnClickListener(new View.OnClickListener() {
            @PreventMultiClick
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, AOPActivity.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mContext = null;
    }

    private final OnPreventMultiClickListener mClickListener = new OnPreventMultiClickListener() {
        @Override
        public void onValidClick(View view) {
            startActivity(new Intent(mContext, ListenerActivity.class));
        }

        @Override
        public void onInvalidClick(View view) {

        }
    };

}