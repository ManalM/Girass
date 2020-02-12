package com.example.girass;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.viewHolder> {
    private FavAdapter.OnItemClickListener mListener;
    public static Context mContext;
    private ArrayList<String> arrayList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(FavAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public FavAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FavAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        /*    TODO: ZIKR_LIST_ITEM LAYOUT */

        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new FavAdapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.viewHolder holder, int position) {

        holder.title.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        SharedPreferences pref;
        SharedPreferences.Editor editor;

        int textSize;

        Typeface defaultFont;

        public viewHolder(@NonNull View itemView, FavAdapter.OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.zikrText_fav);
            //-------------------------- SharedPreference -----------------
            pref = PreferenceManager.getDefaultSharedPreferences(FavAdapter.mContext);
            editor = PreferenceManager.getDefaultSharedPreferences(FavAdapter.mContext).edit();

            if (pref != null) {
                textSize = pref.getInt("fontSize", 18);


                if (pref.getString("defaultFont", "regular").equals("regular"))
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        defaultFont = FavAdapter.mContext.getResources().getFont(R.font.tajawal_regular);
                    } else if (pref.getString("defaultFont", "bold").equals("bold"))
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            defaultFont = FavAdapter.mContext.getResources().getFont(R.font.tajwal_bold);
                        } else if (pref.getString("defaultFont", "light").equals("light"))
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                defaultFont = FavAdapter.mContext.getResources().getFont(R.font.tajawal_light);
                            }
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    defaultFont = FavAdapter.mContext.getResources().getFont(R.font.tajawal_regular);
                }
                editor.putString("defaultFont", "regular");

                textSize = 18;
                editor.putInt("fontSize", textSize);
                editor.apply();
            }
            //--------------------------------------------------------

            title.setTextSize(textSize);
            title.setTypeface(defaultFont);
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
