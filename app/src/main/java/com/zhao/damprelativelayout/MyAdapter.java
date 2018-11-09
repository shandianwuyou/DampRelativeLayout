package com.zhao.damprelativelayout;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 创建者 ：赵鹏   时间：2018/11/6
 */
public class MyAdapter extends RecyclerView.Adapter {

    private Context mContext;

    public MyAdapter(Context context){
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TextView textView = new TextView(mContext);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(20);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(10, 0 , 0, 0);
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, 100);
        textView.setLayoutParams(params);
        return new MyHolder(textView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyHolder){
            MyHolder mHolder = (MyHolder) holder;
            mHolder.textView.setText(String.valueOf(position));
            mHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.onClickItem(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class MyHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public MyHolder(TextView itemView) {
            super(itemView);
            this.textView = itemView;
        }
    }

    public interface ClickListener{
        void onClickItem(int pos);
    }

    private ClickListener mListener;

    public void setClickListener(ClickListener listener){
        this.mListener = listener;
    }
}
