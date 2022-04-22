package com.zhumj.preventmulticlick.listener;

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

public class ListenerActivity extends AppCompatActivity {

    private Button btn1, btn2, btn3;
    private TextView tvText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listener);

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
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        tvText = findViewById(R.id.tvText);

        // TextView 内容滚动
        tvText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private void setViewListener() {
        btn1.setOnClickListener(mClickListener);
        btn2.setOnClickListener(mClickListener);
        btn3.setOnClickListener(mClickListener);
    }

    /**
     * OnPreventMultiClickListener 默认点击间隔 500 毫秒，这里创建的是间隔 2000 毫秒
     */
    @SuppressLint("SetTextI18n")
    private final OnPreventMultiClickListener mClickListener = new OnPreventMultiClickListener(2000) {
        @Override
        public void onValidClick(View view) {
            if (view.getId() == R.id.btn1) {
                tvText.setText("");
            }
            tvText.setText(tvText.getText() + "\n" + ((Button) view).getText() + "点击有效");
            scrollToBottom();
        }

        @Override
        public void onInvalidClick(View view) {
            tvText.setText(tvText.getText() + "\n" + ((Button) view).getText() + "点击无效");
            scrollToBottom();
        }
    };

    private void scrollToBottom() {
        int desired = tvText.getLayout().getLineTop(tvText.getLineCount());
        int padding = tvText.getCompoundPaddingTop() + tvText.getCompoundPaddingBottom();
        int offset = desired + padding - tvText.getHeight();
        if (offset < 0) {
            offset = 0;
        }
        tvText.scrollTo(0, offset);
    }

}