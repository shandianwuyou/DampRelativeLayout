package com.zhao.damprelativelayout;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyAdapter.ClickListener {

    private DampRelativeLayout mRootView;
    private RelativeLayout mHeaderContainer;
    private RecyclerView mRecyclerView;

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAdapter = new MyAdapter(this);
        mAdapter.setClickListener(this);

        mRootView = findViewById(R.id.root_view);
        mHeaderContainer = findViewById(R.id.header_container);
        mRecyclerView = findViewById(R.id.recyclerview);

        mHeaderContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.click_header_tips), Toast.LENGTH_SHORT).show();
            }
        });

        mHeaderContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mRootView.setDestView(mHeaderContainer);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    mHeaderContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    mHeaderContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClickItem(int pos) {
        Toast.makeText(this, getResources().getString(R.string.click_item_tips, pos), Toast.LENGTH_SHORT).show();
    }
}
