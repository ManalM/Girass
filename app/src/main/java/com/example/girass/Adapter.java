package com.example.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/*
 * This adapter is related to the AzkarFragment
 *
 *
 * */


public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> {

    private static Context mContext;
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
    /*    TODO: ZIKR_LIST_ITEM LAYOUT */

        View view = mInflater.inflate(R.layout.zikr_list_item, parent, false);
        return new Adapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.mTextView.setText(mAzkarArray[position]);

    }

    @Override
    public int getItemCount() {
        return mAzkarArray.length;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private LinearLayout mLinearLayout;
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        int textSize;

        Typeface   defaultFont;
        public viewHolder(@NonNull View itemView, final Adapter.OnItemClickListener listener) {
            super(itemView);

            //-------------------------- SharedPreference -----------------
            pref = PreferenceManager.getDefaultSharedPreferences(Adapter.mContext);
            editor = PreferenceManager.getDefaultSharedPreferences(Adapter.mContext).edit();

            if (pref != null){
                    textSize = pref.getInt("fontSize", 18);


                    //TODO: font doesnt change
            if (pref.getString("defaultFont", "regular").equals("regular"))
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont = Adapter.mContext.getResources().getFont(R.font.tajawal_regular);
                } else if (pref.getString("defaultFont", "bold").equals("bold"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont =  Adapter.mContext.getResources().getFont(R.font.tajwal_bold);
                    } else if (pref.getString("defaultFont", "light").equals("light"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont =  Adapter.mContext.getResources().getFont(R.font.tajawal_light);
                        }
        }else{

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont=  Adapter.mContext.getResources().getFont(R.font.tajawal_regular);
                }
                editor.putString("defaultFont", "regular");

                textSize = 18;
                editor.putInt("fontSize", textSize);
                editor.apply();
            }
            //--------------------------------------------------------

            mTextView = itemView.findViewById(R.id.zikrText);
            mLinearLayout = itemView.findViewById(R.id.zikr_linear);
            //-------------------- set Zikr text ---------------------------

            mTextView.setTextSize(textSize);
            mTextView.setTypeface(defaultFont);
            //--------------------------------------------------------

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

