package com.dozo.girass.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dozo.girass.R;

import java.util.ArrayList;

public class FavAdapter extends RecyclerView.Adapter<FavAdapter.viewHolder> {
    private FavAdapter.OnItemClickListener mListener;
    public static Context mContext;
    private static ArrayList<String> arrayList;


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

        View view = mInflater.inflate(R.layout.list_item, parent, false);
        return new FavAdapter.viewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FavAdapter.viewHolder holder, int position) {

        holder.title.setText(arrayList.get(position));
        if (arrayList.size() > 0)
            scaleAnimation(holder.cardView);
    }

    private void scaleAnimation(View view) {
        ScaleAnimation alphaAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setDuration(700);
        view.startAnimation(alphaAnimation);

    }
    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title;
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        CardView cardView;
        int textSize;
        Typeface defaultFont;

        public viewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);

            title = itemView.findViewById(R.id.zikrText_fav);

            cardView = itemView.findViewById(R.id.fav_layout);
            //-------------------------- SharedPreference -----------------
            pref = PreferenceManager.getDefaultSharedPreferences(mContext);
            editor = PreferenceManager.getDefaultSharedPreferences(mContext).edit();

            if (pref != null) {
                textSize = pref.getInt("fontSize", 18);

                if (pref.getString("defaultFont", "regular").equals("regular"))
                    defaultFont = Typeface.createFromAsset(mContext.getAssets(),
                            "fonts/tajawal_regular.ttf");
                else if (pref.getString("defaultFont", "bold").equals("bold"))
                    defaultFont = Typeface.createFromAsset(mContext.getAssets(), "fonts/arial.ttf");
                else if (pref.getString("defaultFont", "light").equals("light"))
                    defaultFont = Typeface.createFromAsset(mContext.getAssets(),
                            "fonts/sans-serif.ttf");
            } else {

                defaultFont = Typeface.createFromAsset(mContext.getAssets(),
                        "fonts/tajawal_regular.ttf");

                editor.putString("defaultFont", "regular");

                textSize = 18;
                editor.putInt("fontSize", textSize);
                editor.apply();
            }
            //--------------------------------------------------------

            if (textSize > 20)
                title.setTextSize(20);
            else
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
