package rs.etf.pmu.cats.fragments;

import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.customImageViews.VehicleModificationCustomImageView;
import rs.etf.pmu.cats.customImageViews.ViewHolderCustomImageView;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.other.MyAdapter;
import rs.etf.pmu.cats.other.SelectedComponentUpdatable;
import rs.etf.pmu.cats.viewModels.Inventory;
import rs.etf.pmu.cats.viewModels.InventoryViewModel;

public class VehicleModificationFragment extends Fragment implements SelectedComponentUpdatable {

    private InventoryViewModel inventoryViewModel;

    private VehicleModificationCustomImageView vehicleModificationCustomImageView;
    private RecyclerView recyclerView;
    private ViewHolderCustomImageView helperImageView;
    private TextView componentHealth, componentEnergy, componentPower;
    private TextView vehicleHealth, vehicleEnergy, vehiclePower;
    private Button backButton;
    private SelectedComponentUpdatable selectedComponentUpdatable;
    private NavController navController;
    private CatsRoomDatabase catsRoomDatabase;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.vehicle_modification_fragment, container, false);

        catsRoomDatabase = CatsRoomDatabase.getDatabase(getContext());
        selectedComponentUpdatable = this;
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        getViews(view);
        setupDragAndDrop(view);

        return view;
    }

    private void getViews(View view) {
        vehicleModificationCustomImageView = view.findViewById(R.id.vehicle_modification_custom_image_view);
        helperImageView = view.findViewById(R.id.helper_image_view);

        componentHealth = view.findViewById(R.id.component_health_text_view);
        componentEnergy = view.findViewById(R.id.component_energy_text_view);
        componentPower = view.findViewById(R.id.component_power_text_view);

        vehicleHealth = view.findViewById(R.id.vehicle_health_text_view);
        vehicleEnergy = view.findViewById(R.id.vehicle_energy_text_view);
        vehiclePower = view.findViewById(R.id.vehicle_power_text_view);

        backButton = view.findViewById(R.id.back_button);

        recyclerView = view.findViewById(R.id.my_recycler_view);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        inventoryViewModel = ViewModelProviders.of(getActivity()).get(InventoryViewModel.class);

        MyAdapter mAdapter = new MyAdapter(inventoryViewModel, this);
        recyclerView.setAdapter(mAdapter);

        inventoryViewModel.getInventoryLiveData().observe(getActivity(), new Observer<Inventory>() {
            @Override
            public void onChanged(Inventory inventory) {
                for (DrawableComponent c : inventory.currentlyInUse) {
                    if (c instanceof Chassis && ((Chassis) c).inUse) {
                        vehicleModificationCustomImageView.updateView((Chassis) c);
                        break;
                    }
                }
                MyAdapter mAdapter = new MyAdapter(inventoryViewModel, selectedComponentUpdatable);
                recyclerView.setAdapter(mAdapter);
                setupVehicleAndComponentStats();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.popBackStack();
            }
        });
    }

    private void setupVehicleAndComponentStats() {
        Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();

        int vehicleMaxEnergy = 0;
        int healthSum = 0;
        int powerSum = 0;
        int energySum = 0;
        for (DrawableComponent d : inventory.currentlyInUse) {
            if (d instanceof Chassis) vehicleMaxEnergy = d.getEnergy();
            else energySum += d.getEnergy();

            healthSum += d.getHealth();
            powerSum += d.getPower();
        }

        vehicleHealth.setText("Health=" + healthSum);
        vehicleEnergy.setText("Energy=" + energySum + "/" + vehicleMaxEnergy);
        vehiclePower.setText("Power=" + powerSum);
    }

    private void setupDragAndDrop(final View view) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();
                DrawableComponent draggedDrawableComponent = inventory.draggedDrawableComponent;

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP:

                        if (inventory.components.contains(draggedDrawableComponent) == false)
                            inventory.components.add(draggedDrawableComponent);
                        inventoryViewModel.setInventoryLiveData(inventory);
                        if (inventoryViewModel.getInventoryLiveData().getValue().viewHolderCustomImageView != helperImageView)
                            inventoryViewModel.getInventoryLiveData().getValue().viewHolderCustomImageView.setVisibility(View.VISIBLE);
                        System.out.println("Fragment DragEvent x : " + dragEvent.getX() + "         y : " + dragEvent.getY());
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                }
                return true;
            }
        });


        this.vehicleModificationCustomImageView.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {

                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DROP:

                        Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();
                        DrawableComponent draggedDrawableComponent = inventory.draggedDrawableComponent;

                        //helperImageView.setVisibility(View.INVISIBLE);

                        if (draggedDrawableComponent instanceof Chassis) {
                            vehicleModificationCustomImageView.chassis.removeFromUse();
                            Chassis newChassisInUse = (Chassis) draggedDrawableComponent;
                            newChassisInUse.inUse = true;

                            inventory.components.addAll(inventory.currentlyInUse);
                            inventory.components.remove(newChassisInUse);
                            inventory.currentlyInUse = new ArrayList<>();
                            inventory.currentlyInUse.add(newChassisInUse);

                            inventoryViewModel.setInventoryLiveData(inventory);
                        } else if (vehicleModificationCustomImageView.chassis.canSetNewComponent(draggedDrawableComponent, dragEvent.getX(), dragEvent.getY())) {
                            System.out.println("MOZE");
                            //this below sets up new and old drawableComponents correctly

                            int chassisEnergy = 0;
                            int sumEnergy = 0;
                            for (DrawableComponent d : inventory.currentlyInUse){
                                if (d instanceof Chassis) chassisEnergy = d.getEnergy();
                                else sumEnergy += d.getEnergy();
                            }

                            if (sumEnergy + draggedDrawableComponent.getEnergy() > chassisEnergy) {
                                inventory.viewHolderCustomImageView.setVisibility(View.VISIBLE);
                                return false;
                            }

                            DrawableComponent oldDrawableComponent = vehicleModificationCustomImageView.chassis.setNewComponentAndReturnOld(draggedDrawableComponent, dragEvent.getX(), dragEvent.getY());

                            if (oldDrawableComponent != null) {
                                inventory.currentlyInUse.remove(oldDrawableComponent);
                                inventory.components.add(oldDrawableComponent);
                            }

                            inventory.components.remove(draggedDrawableComponent);
                            inventory.currentlyInUse.add(draggedDrawableComponent);

                            inventoryViewModel.setInventoryLiveData(inventory);
                        } else {
                            System.out.println("NE MOZE");

                            if (inventory.components.contains(draggedDrawableComponent) == false)
                                inventory.components.add(draggedDrawableComponent);
                            inventoryViewModel.setInventoryLiveData(inventory);
                            //return false;
                        }

                        System.out.println("---->" + vehicleModificationCustomImageView.chassis.isSlotPoint(dragEvent.getX(), dragEvent.getY()));

                        if (inventoryViewModel.getInventoryLiveData().getValue().viewHolderCustomImageView != helperImageView)
                            inventoryViewModel.getInventoryLiveData().getValue().viewHolderCustomImageView.setVisibility(View.VISIBLE);


                        System.out.println("Slika DROP X : " + dragEvent.getX() + ", Y : " + dragEvent.getY());
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        break;
                }
                return true;
            }
        });

        this.vehicleModificationCustomImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                System.out.println(motionEvent.getAction());
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        DrawableComponent selectedDrawableComponent = vehicleModificationCustomImageView.chassis.getSelectedComponentAndRemoveItFromUse(motionEvent.getX(), motionEvent.getY());
                        System.out.println("MotionEvent: X = " + motionEvent.getX() + "         Y = " + motionEvent.getY());
                        System.out.println(selectedDrawableComponent == null);
                        if (selectedDrawableComponent != null) {
                            vehicleModificationCustomImageView.invalidate();

                            helperImageView.updateView(selectedDrawableComponent);
                            //helperImageView.setVisibility(View.VISIBLE);
                            Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();

                            inventory.draggedIndex = -1;
                            inventory.viewHolderCustomImageView = helperImageView;
                            inventory.draggedDrawableComponent = selectedDrawableComponent;

                            inventory.currentlyInUse.remove(selectedDrawableComponent);

                            //inventoryViewModel.setInventoryLiveData(inventory);
                            view.startDragAndDrop(null, new View.DragShadowBuilder(helperImageView), null, 0);
                            return true;
                        } else {
                            return false;
                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void updateSelectedComponent(DrawableComponent drawableComponent) {
        componentHealth.setText("Health=" + drawableComponent.getHealth());
        componentEnergy.setText("Energy=" + drawableComponent.getEnergy());
        componentPower.setText("Power=" + drawableComponent.getPower());
    }

    @Override
    public void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();
                for (DrawableComponent d : inventory.currentlyInUse) {
                    d.update(catsRoomDatabase);
                }
                for (DrawableComponent d : inventory.components) {
                    d.update(catsRoomDatabase);
                }
            }
        }).start();
    }
}