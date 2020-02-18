package com.example.girass;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeAdapter extends ItemTouchHelper.SimpleCallback {
    private FavAdapter favAdapter;

    public SwipeAdapter(FavAdapter adapter) {
        super(0, ItemTouchHelper.RIGHT);

        this.favAdapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();

    }
}
