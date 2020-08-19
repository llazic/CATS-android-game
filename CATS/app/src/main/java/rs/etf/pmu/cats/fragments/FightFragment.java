package rs.etf.pmu.cats.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.customImageViews.FightCustomImageView;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.other.FightEndable;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.other.FightAsyncTask;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;
import rs.etf.pmu.cats.viewModels.Inventory;
import rs.etf.pmu.cats.viewModels.InventoryViewModel;

public class FightFragment extends Fragment implements FightEndable {

    private CurrentPlayerViewModel currentPlayerViewModel;
    private InventoryViewModel inventoryViewModel;
    private FightCustomImageView fightCustomImageView;
    private FightAsyncTask fightAsyncTask;
    private CatsRoomDatabase catsRoomDatabase;
    private Chassis playerChassis;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fight_fragment, container, false);

        fightCustomImageView = view.findViewById(R.id.fight_custom_image_view);
        fightCustomImageView.setFightEndable(this);
        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);
        inventoryViewModel = ViewModelProviders.of(getActivity()).get(InventoryViewModel.class);
        catsRoomDatabase = CatsRoomDatabase.getDatabase(getContext());

        Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();
        Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();

        for (DrawableComponent d : inventory.currentlyInUse){
            if (d instanceof Chassis) {
                playerChassis = (Chassis)d;
                fightCustomImageView.setPlayerChassis(playerChassis, player.vehicleControl);
                break;
            }
        }

        fightCustomImageView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    playerChassis.handleTouchDuringFight(motionEvent.getX(), motionEvent.getY());
                }
                return true;
            }
        });

        fightAsyncTask = new FightAsyncTask(fightCustomImageView);
        fightAsyncTask.execute();

        return view;
    }


    @Override
    public void onPause() {
        super.onPause();
        fightAsyncTask.stop();
    }

    @Override
    public void fightEnded(boolean playerWon) {
        fightAsyncTask.stop();

        final Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
        if (playerWon) {
            player.gamesWon++;
            if (player.gamesWon % 3 == 0) {
                if (player.box0Time == null) player.box0Time = System.currentTimeMillis() + 20 * 60 * 1000L;
                else if (player.box1Time == null) player.box1Time = System.currentTimeMillis() + 20 * 60 * 1000L;
                else if (player.box2Time == null) player.box2Time = System.currentTimeMillis() + 20 * 60 * 1000L;
                else if (player.box3Time == null) player.box3Time = System.currentTimeMillis() + 20 * 60 * 1000L;
            }
        }
        else player.gamesLost++;
        currentPlayerViewModel.setCurrentPlayerLiveData(player);

        saveToDB(player);
    }

    private void saveToDB(final Player player) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                catsRoomDatabase.playerDAO().update(player);
                SystemClock.sleep(2000);
                redirect();
            }
        }).start();
    }

    public void redirect() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        navController.popBackStack();
    }
}
