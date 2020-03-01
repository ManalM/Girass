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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/*
 * This adapter is related to the AzkarFragment
 *
 *
 * */


public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> implements Filterable {

    private static Context mContext;
    private ArrayList<String> mAzkarArray;
    private ArrayList<String> serachList;
    private Adapter.OnItemClickListener mListener;
    private ValueFilter valueFilter;


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(Adapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public Adapter(Context context, ArrayList<String> mAzkarArray) {
        this.mContext = context;
        this.mAzkarArray = mAzkarArray;
        this.serachList = mAzkarArray;
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
        holder.mTextView.setText(mAzkarArray.get(position));

    }

    @Override
    public int getItemCount() {
        return mAzkarArray.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        SharedPreferences pref;
        SharedPreferences.Editor editor;

        int textSize;

        Typeface defaultFont;

        public viewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            //-------------------------- SharedPreference -----------------
            pref = PreferenceManager.getDefaultSharedPreferences(Adapter.mContext);
            editor = PreferenceManager.getDefaultSharedPreferences(Adapter.mContext).edit();

            if (pref != null) {
                textSize = pref.getInt("fontSize", 18);


                if (pref.getString("defaultFont", "regular").equals("regular"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont = Adapter.mContext.getResources().getFont(R.font.tajawal_regular);
                    } else if (pref.getString("defaultFont", "bold").equals("bold"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont = Adapter.mContext.getResources().getFont(R.font.tajwal_bold);
                        } else if (pref.getString("defaultFont", "light").equals("light"))
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                defaultFont = Adapter.mContext.getResources().getFont(R.font.tajawal_light);
                            }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont = Adapter.mContext.getResources().getFont(R.font.tajawal_regular);
                }
                editor.putString("defaultFont", "regular");

                textSize = 18;
                editor.putInt("fontSize", textSize);
                editor.apply();
            }
            //--------------------------------------------------------

            mTextView = itemView.findViewById(R.id.zikrText);

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


    //-------------------searching class -----------------------------
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            DataService dataService = new DataService();
            final HeadZikrObject[] headZikrObjects = dataService.GetAllAzkar();

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<>();
                for (HeadZikrObject headZikrObject : headZikrObjects) {
                    if (headZikrObject.SearchTitle.contains(constraint)) {
                        filterList.add(headZikrObject.TITLE);
                    }

                }

                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = serachList.size();
                results.values = serachList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            mAzkarArray = (ArrayList<String>) results.values;

            notifyDataSetChanged();
        }
    }
}

