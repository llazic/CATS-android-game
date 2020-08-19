package rs.etf.pmu.cats.other;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.customImageViews.ViewHolderCustomImageView;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.viewModels.Inventory;
import rs.etf.pmu.cats.viewModels.InventoryViewModel;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private Inventory inventory;
    private InventoryViewModel inventoryViewModel;
    private SelectedComponentUpdatable selectedComponentUpdatable;

    public MyAdapter(InventoryViewModel inventoryViewModel, SelectedComponentUpdatable selectedComponentUpdatable) {
        this.inventoryViewModel = inventoryViewModel;
        this.inventory = inventoryViewModel.getInventoryLiveData().getValue();
        this.selectedComponentUpdatable = selectedComponentUpdatable;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderCustomImageView v = (ViewHolderCustomImageView) LayoutInflater.from(parent.getContext()).inflate(R.layout.image_view_view_holder, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        //v.setOnDragListener(this.vehicleModificationCustomImageView);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DrawableComponent drawableComponent = inventory.components.get(position);
        holder.viewHolderCustomImageView.updateView(drawableComponent);

        holder.viewHolderCustomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedComponentUpdatable.updateSelectedComponent(drawableComponent);
            }
        });

        holder.viewHolderCustomImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                v.startDragAndDrop(null, new View.DragShadowBuilder(v), null, 0);
                Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();

                inventory.draggedIndex = position;
                inventory.viewHolderCustomImageView = holder.viewHolderCustomImageView;
                inventory.draggedDrawableComponent = drawableComponent;

                //inventoryViewModel.setInventoryLiveData(inventory);
                holder.viewHolderCustomImageView.setVisibility(View.INVISIBLE);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventory.components.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ViewHolderCustomImageView viewHolderCustomImageView;

        public MyViewHolder(ViewHolderCustomImageView v) {
            super(v);
            viewHolderCustomImageView = v;
        }
    }
}