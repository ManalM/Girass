package com.example.girass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class AdapterAzkar extends RecyclerView.Adapter<AdapterAzkar.NewViewHolder> {

    private Context mContext;
    private String[] mAzkarArray;
    private AdapterAzkar mAdapterAzkar;


    public AdapterAzkar(Context context, String[] mAzkarArray) {
        this.mContext = context;
        this.mAzkarArray = mAzkarArray;
    }

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.dialog_item,parent,false);
        return new AdapterAzkar.NewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position) {

        holder.mTextView.setText(mAzkarArray[position]);

    }

    @Override
    public int getItemCount() {
        return mAzkarArray.length;
    }

    public static class NewViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private LinearLayout mLinearLayout;

        public NewViewHolder(View itemView){
            super(itemView);
            mTextView=itemView.findViewById(R.id.choose_zikr_item);
            mLinearLayout=itemView.findViewById(R.id.linearLayout);
        }

    }
}
