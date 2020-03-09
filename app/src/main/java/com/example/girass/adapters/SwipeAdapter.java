package com.example.girass.adapters;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.MotionEventCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.girass.Data.DataService;
import com.example.girass.FavoriteFragment;
import com.example.girass.R;
import com.example.girass.ZikrDetails;
import com.example.girass.helpers.ItemTouchHelperAdapter;
import com.example.girass.helpers.ItemTouchHelperViewHolder;
import com.example.girass.helpers.OnStartDragListener;
import com.example.girass.model.HeadZikrObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import static com.example.girass.AllZikr.title;

public class SwipeAdapter extends RecyclerView.Adapter<SwipeAdapter.ItemViewHolder> implements ItemTouchHelperAdapter {


    public static ArrayList<String> mItems;
    private Context context;
    private final OnStartDragListener mDragStartListener;
    public static SharedPreferences pref;
    public static SharedPreferences.Editor editor;
    private final String mapKey = "map";
    HashMap<String, String> hashMap = loadMap();

    public SwipeAdapter(Context context, ArrayList<String> array, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.context = context;
        mItems = array;
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
/*        for (int i =0; i<mItems.size();i++){
            hashMap.put(String.valueOf(position+1),mItems.get(position));
        }
        saveMap(hashMap);*/
    }

    private void saveMap(HashMap<String, String> inputMap) {

        if (pref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            Toast.makeText(context, jsonString, Toast.LENGTH_SHORT).show();
            editor.remove(mapKey).apply();
            editor.putString(mapKey, jsonString);
            editor.commit();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);

        notifyItemMoved(fromPosition, toPosition);

        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    private HashMap<String, String> loadMap() {
        HashMap<String, String> outputMap = new HashMap<>();

        try {
            if (pref != null) {
                String jsonString = pref.getString(mapKey, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    outputMap.put(key, (String) jsonObject.get(key));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }


/*
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            int position = viewHolder.getAdapterPosition();
            Toast.makeText(context, position, Toast.LENGTH_SHORT).show();
        }
    };
*/

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
