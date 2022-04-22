package com.zhumj.preventmulticlick.listener;

import android.view.View;

import androidx.collection.ArrayMap;

/**
 * @ProjectName : PreventMultiClick
 * @Author : zhumj
 * @Time : 2022/4/21 17:25
 * @Description : 预防短时间内多次点击
 */
public abstract class OnPreventMultiClickListener implements View.OnClickListener {

    // 间隔时间，默认 500 毫秒
    private final long intervalTime;
    // 以 id 存储各控件最后点击的时间
    private final ArrayMap<Integer, Long> arrayMap;

    public OnPreventMultiClickListener() {
        this(500);
    }

    public OnPreventMultiClickListener(long intervalTime) {
        this.intervalTime = intervalTime;
        arrayMap = new ArrayMap<>();
    }

    @Override
    public void onClick(View view) {
        long currentTime = System.currentTimeMillis();
        long lastTime = 0;
        if (arrayMap.containsKey(view.getId())) {
            Long time = arrayMap.get(view.getId());
            lastTime = time != null ? time : 0;
        }
        long diffTime = currentTime - lastTime;
        if (diffTime > intervalTime) {
            arrayMap.put(view.getId(), currentTime);
            onValidClick(view);
        } else {
            onInvalidClick(view);
        }
    }

    /**
     * 有效点击回调
     */
    public abstract void onValidClick(View view);
    /**
     * 无效点击回调
     */
    public abstract void onInvalidClick(View view);

}
