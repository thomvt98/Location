package com.example.chien.location.touchHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import com.example.chien.location.adapter.SosAdapter;

public class SimpleItemTouchHelperCallbackSos extends ItemTouchHelper.SimpleCallback {
    private final SosAdapter recycleAdapter;

    public SimpleItemTouchHelperCallbackSos(SosAdapter recycleAdapter) {
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.recycleAdapter = recycleAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        recycleAdapter.swap(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        recycleAdapter.remove(viewHolder.getAdapterPosition());
    }
}
