package com.example.girass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    private Context mContext;
    private String[] mAzkarArray;
    private Adapter mAdapterAzkar;
    private Adapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(Adapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter(Context context, String[] mAzkarArray) {
        this.mContext = context;
        this.mAzkarArray = mAzkarArray;
    }

    @Override
    public Adapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.zikr_list_item,parent,false);
        return new Adapter.viewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.mTextView.setText(mAzkarArray[position]);

    }

    @Override
    public int getItemCount() {
        return  mAzkarArray.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

    private TextView mTextView;
    private LinearLayout mLinearLayout;

    public viewHolder(@NonNull View itemView,final Adapter.OnItemClickListener listener) {
        super(itemView);
        mTextView=itemView.findViewById(R.id.zikrText);
        mLinearLayout=itemView.findViewById(R.id.zikr_linear);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            }
        });
    }


    }
}

