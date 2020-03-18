package com.dozo.girass.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dozo.girass.helpers.ItemTouchHelperAdapter;
import com.dozo.girass.helpers.OnStartDragListener;
import com.dozo.girass.model.PrefMethods;
import com.dozo.girass.R;
import com.dozo.girass.helpers.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {


    public static ArrayList<String> mItems;
    public static Context context;
    private final OnStartDragListener mDragStartListener;


    public SwipeAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.context = context;
        mItems = new PrefMethods(context).getArray();
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.textView.setText(mItems.get(position));

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onItemDismiss(position);

            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
        new PrefMethods(context).saveArray(mItems);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);
        new PrefMethods(context).saveArray(mItems);

        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }



    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        SharedPreferences pref;
        SharedPreferences.Editor editor;
        public final TextView textView;
        public ImageView handleView, delete;
        int textSize;
        Typeface defaultFont;

        public ItemViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.zikrText_fav_edit);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            //-------------------------- SharedPreference -----------------
            pref = PreferenceManager.getDefaultSharedPreferences(context);
            editor = PreferenceManager.getDefaultSharedPreferences(context).edit();

            if (pref != null) {
                textSize = pref.getInt("fontSize", 18);


                if (pref.getString("defaultFont", "regular").equals("regular"))
                    defaultFont = Typeface.createFromAsset(context.getAssets(),
                            "fonts/tajawal_regular.ttf");
                else if (pref.getString("defaultFont", "bold").equals("bold"))
                    defaultFont = Typeface.createFromAsset(context.getAssets(), "fonts/arial.ttf");
                else if (pref.getString("defaultFont", "light").equals("light"))
                    defaultFont = Typeface.createFromAsset(context.getAssets(),
                            "fonts/sans-serif.ttf");

            } else {

                defaultFont = Typeface.createFromAsset(context.getAssets(),
                        "fonts/tajawal_regular.ttf");

                editor.putString("defaultFont", "regular");

                textSize = 18;
                editor.putInt("fontSize", textSize);
                editor.apply();
            }
            //--------------------------------------------------------

            if (textSize > 18)
                textView.setTextSize(18);
            else
                textView.setTextSize(textSize);
            textView.setTypeface(defaultFont);
            //--------------------------------------------------------

        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
