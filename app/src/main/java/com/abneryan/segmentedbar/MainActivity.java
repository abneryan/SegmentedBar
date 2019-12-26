package com.abneryan.segmentedbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import com.abneryan.segmentedbar.bean.IndicatorBean;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SegemtedBarContract.View{

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutmanager;
    private int lastVisibleItemPosition;
    private SegementedPresenter mPresenter;
    private SegmentedtAdapter mSegmentedtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getSegmentedDatas();
    }

    private void initData() {
        mPresenter = new SegementedPresenter(this,getBaseContext());
        mSegmentedtAdapter = new SegmentedtAdapter(getBaseContext());
        mSegmentedtAdapter.setItemExpandStateListener(new SegmentedtAdapter.ItemExpandStateListener() {
            @Override
            public void scrollItem(int position) {
                Log.d(TAG, " position= " + position + " lastVisibleItemPosition= " + lastVisibleItemPosition);
                if (lastVisibleItemPosition == position) {
                    mRecyclerView.smoothScrollBy(0, 200);
                }
            }

            @Override
            public void onItemCollapsed() {
                lastVisibleItemPosition = layoutmanager.findLastVisibleItemPosition();
                Log.d(TAG, " onItemCollapsed lastVisibleItemPosition= " + lastVisibleItemPosition);
            }
        });
        mRecyclerView.setAdapter(mSegmentedtAdapter);
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.result_recyclerview);
        mRecyclerView.setFocusableInTouchMode(false);
        // 控制smoothScrollToPosition的滑动速度
        layoutmanager = new LinearLayoutManager(this) {
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                LinearSmoothScroller smoothScroller =
                        new LinearSmoothScroller(recyclerView.getContext()) {
                            // 返回：滑过1px时经历的时间(ms)。
                            @Override
                            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                return 150f / displayMetrics.densityDpi;
                            }
                        };

                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
        // 设置RecyclerView 布局
        mRecyclerView.setLayoutManager(layoutmanager);
//        mRecyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取最后一个可见view的位置
                lastVisibleItemPosition = layoutmanager.findLastVisibleItemPosition();
                Log.d(TAG, "onScrollStateChanged lastVisibleItemPosition= " + lastVisibleItemPosition);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    @Override
    public void updateSegmentedDatas(List<IndicatorBean> dataList) {
        mSegmentedtAdapter.updateList(dataList);
        lastVisibleItemPosition = layoutmanager.findLastVisibleItemPosition();
    }
}
