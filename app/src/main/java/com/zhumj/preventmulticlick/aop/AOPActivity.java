package com.zhumj.preventmulticlick.aop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhumj.preventmulticlick.R;

/**
 * 1、PreventMultiClick 和 PreventMultiClickAspect 是配套的，
 *   只有标注了 @PreventMultiClick 的方法才会启用防重复点击；
 * 2、AopClickUtil、AopClickExcept、AopClickAspect 是配套的，
 *   自动全局启用防重复点击，支持Lambda，布局中设置，butterknife注解，
 *   通过 AopClickUtil 控制间隔时间、启用、停用，标注 @AopClickExcept 的方法不启用防重复点击范围；
 *   注意：
 *      Lambda表达式，butterknife，布局中设置 不支持 AopClickExcept 忽略注解
 *      AopClickExcept 忽略注解支持setOnClickListener()方式
 */
public class AOPActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn1, btn2, btn3;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aop);

        AopClickUtil.setIntervalTime(2000);

        //显示返回按键
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initView();
        setViewListener();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        btn1 = findViewById(R.id.btnAOP1);
        btn2 = findViewById(R.id.btnAOP2);
        btn3 = findViewById(R.id.btnAOP3);
        tvText = findViewById(R.id.tvText);

        // TextView 内容滚动
        tvText.setMovementMethod(ScrollingMovementMethod.getInstance());

        if (AopClickUtil.isEnable) {
            btn1.setText("防重复点击：启用");
        } else {
            btn1.setText("防重复点击：停用");
        }
    }

    private void setViewListener() {
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnAOP1) {
            if (AopClickUtil.isEnable) {
                btn1.setText("防重复点击：停用");
                AopClickUtil.disable();
                tvText.setText("");
            } else {
                btn1.setText("防重复点击：启用");
                AopClickUtil.enable();
            }
            return;
        }
        tvText.setText(tvText.getText() + "\n" + ((Button) view).getText() + "点击有效");
        scrollToBottom();
    }

    private void scrollToBottom() {
        int desired = 0;
        if (tvText.getLayout() != null) {
            desired = tvText.getLayout().getLineTop(tvText.getLineCount());
        }
        int padding = tvText.getCompoundPaddingTop() + tvText.getCompoundPaddingBottom();
        int offset = desired + padding - tvText.getHeight();
        if (offset < 0) {
            offset = 0;
        }
        tvText.scrollTo(0, offset);
    }
}