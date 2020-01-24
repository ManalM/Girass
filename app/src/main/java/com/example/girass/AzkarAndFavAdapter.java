package com.example.girass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class AzkarAndFavAdapter extends  RecyclerView.Adapter<AzkarAndFavAdapter.NewViewHolder>  {
    private Context mContext;
    private String[] mAzkarArray;
    private AzkarAndFavAdapter.OnItemClickListener mListener;
    private LayoutInflater mInflater;
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(AzkarAndFavAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public AzkarAndFavAdapter(Context context, String[] mAzkarArray) {
        this.mContext = context;
        this.mAzkarArray = mAzkarArray;
    }

    @NonNull

    @Override
    public NewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.zikr_list_item,parent,false);

        return new AzkarAndFavAdapter.NewViewHolder(view,mListener);
    }


    @Override
    public void onBindViewHolder(final NewViewHolder holder, final int position) {

        holder.mTextView.setText(mAzkarArray[position]);

    }

    @Override
    public int getItemCount() {
        return 0;
    }
 public static class NewViewHolder extends RecyclerView.ViewHolder{
        private TextView mTextView;
        private LinearLayout mLinearLayout;

        public NewViewHolder(View itemView,final AzkarAndFavAdapter.OnItemClickListener listener){
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
