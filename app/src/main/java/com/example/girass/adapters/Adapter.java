package com.example.girass.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.girass.Data.DataService;
import com.example.girass.model.HeadZikrObject;
import com.example.girass.R;

import java.util.ArrayList;


/*
 * This adapter is related to the AzkarFragment
 *
 *
 * */


public class Adapter extends RecyclerView.Adapter<Adapter.viewHolder> implements Filterable {

    private static Context mContext;
    private static ArrayList<String> mAzkarArray;
    private ArrayList<String> serachList;
    private ValueFilter valueFilter;

    private static SelectedUser selectedUser;
    public static int selectedItem;

    public Adapter(Context context, ArrayList<String> mAzkarArray, SelectedUser selectedUser) {
        this.mContext = context;
        this.mAzkarArray = mAzkarArray;
        this.serachList = mAzkarArray;
        this.selectedUser = selectedUser;
    }

    @Override
    public Adapter.viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        View view = mInflater.inflate(R.layout.zikr_list_item, parent, false);
        return new Adapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.mTextView.setText(mAzkarArray.get(position));

        if (mAzkarArray.get(position).length() > 20) {
            holder.textSize = 18;
        }

        scaleAnimation(holder.zikrLinear);
    }

    @Override
    public int getItemCount() {
        return mAzkarArray.size();
    }

    private void scaleAnimation(View view) {
        ScaleAnimation alphaAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, (float) 0.5, Animation.RELATIVE_TO_SELF, 0.5f);
        alphaAnimation.setDuration(700);
        view.startAnimation(alphaAnimation);

    }
    //--------- listener for each item---------------
    public interface SelectedUser {

        void selectedUser(String s);

    }

    // -------------- filter the search -------------
    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        SharedPreferences pref;
        SharedPreferences.Editor editor;
        RelativeLayout zikrLinear;

        int textSize;

        Typeface defaultFont;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

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

            mTextView = itemView.findViewById(R.id.zikrText);
            zikrLinear = itemView.findViewById(R.id.zikr_linear);
            //-------------------- set Zikr text ---------------------------
            if (textSize > 20)
                mTextView.setTextSize(20);
            else
                mTextView.setTextSize(textSize);

            mTextView.setTypeface(defaultFont);
            //--------------------------------------------------------

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectedUser.selectedUser(mAzkarArray.get(getAdapterPosition()));
                    selectedItem = getAdapterPosition();

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

