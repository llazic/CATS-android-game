package rs.etf.pmu.cats.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.customImageViews.GarageCustomImageView;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.dialogs.SettingsDialogFragment;
import rs.etf.pmu.cats.dialogs.StatsDialogFragment;
import rs.etf.pmu.cats.other.RandomGenerator;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;
import rs.etf.pmu.cats.viewModels.Inventory;
import rs.etf.pmu.cats.viewModels.InventoryViewModel;

public class GarageFragment extends Fragment {

    //CurrentPlayerViewModel has actual settings, and everything is up to date
    //when onPause occurs currentPlayer from CurrentPlayerViewModel is saved to db
    private CurrentPlayerViewModel currentPlayerViewModel;
    private InventoryViewModel inventoryViewModel;

    private ImageView[] boxes;
    private TextView[] boxTimers;

    private ImageView[] checkMarks;
    private ImageView stats, settings;
    private GarageCustomImageView garageCustomImageView;

    private Button fightButton;

    private MediaPlayer mediaPlayer;

    private CatsRoomDatabase catsRoomDatabase;

    private Context context;

    private Timer timer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.garage_fragment, container, false);

        catsRoomDatabase = CatsRoomDatabase.getDatabase(context);
        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);

        getViews(view);
        setupBoxes();
        setupCheckMarks();
        setupDialogs();
        setupNavigation();

        inventoryViewModel = ViewModelProviders.of(getActivity()).get(InventoryViewModel.class);
        inventoryViewModel.getInventoryLiveData().observe(getActivity(), new Observer<Inventory>() {
            @Override
            public void onChanged(Inventory inventory) {
                for (DrawableComponent c : inventory.currentlyInUse) {
                    if (c instanceof Chassis && ((Chassis) c).inUse) {
                        garageCustomImageView.updateView((Chassis) c);
                        return;
                    }
                }
            }
        });

        Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
        for (int i = 0; i < 4; i++) {
            final Long time;
            if (i == 0) time = player.box0Time;
            else if (i == 1) time = player.box1Time;
            else if (i == 2) time = player.box2Time;
            else time = player.box3Time;
            boxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (time != null && time <= System.currentTimeMillis()) {
                        openNewBox(time);
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getViews(View view) {
        boxes = new ImageView[4];
        boxTimers = new TextView[4];

        boxes[0] = view.findViewById(R.id.box0);
        boxes[1] = view.findViewById(R.id.box1);
        boxes[2] = view.findViewById(R.id.box2);
        boxes[3] = view.findViewById(R.id.box3);

        boxTimers[0] = view.findViewById(R.id.timeBox0);
        boxTimers[1] = view.findViewById(R.id.timeBox1);
        boxTimers[2] = view.findViewById(R.id.timeBox2);
        boxTimers[3] = view.findViewById(R.id.timeBox3);

        checkMarks = new ImageView[3];

        checkMarks[0] = view.findViewById(R.id.check_mark_0);
        checkMarks[1] = view.findViewById(R.id.check_mark_1);
        checkMarks[2] = view.findViewById(R.id.check_mark_2);

        stats = view.findViewById(R.id.stats_icon);
        settings = view.findViewById(R.id.settings_icon);
        fightButton = view.findViewById(R.id.buttonFight);
        garageCustomImageView = view.findViewById(R.id.garage_custom_image_view);
    }

    private void openNewBox(long time) {
        Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
        Inventory inventory = inventoryViewModel.getInventoryLiveData().getValue();
        inventory.components.addAll(RandomGenerator.generateNewBox(player.id, catsRoomDatabase));

        if (player.box0Time != null && time == player.box0Time) { player.box0Time = null; boxes[0].setVisibility(View.INVISIBLE); }
        else if (player.box1Time != null && time == player.box1Time) { player.box1Time = null; boxes[1].setVisibility(View.INVISIBLE); }
        else if (player.box2Time != null && time == player.box2Time) { player.box2Time = null; boxes[2].setVisibility(View.INVISIBLE); }
        else if (player.box3Time != null && time == player.box3Time) { player.box3Time = null; boxes[3].setVisibility(View.INVISIBLE); }

        inventoryViewModel.setInventoryLiveData(inventory);
        currentPlayerViewModel.setCurrentPlayerLiveData(player);
        Toast.makeText(getContext(), "New components have been added to your inventory!", Toast.LENGTH_LONG).show();
    }

    public void setupBoxes() {
        Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
        long currentTime = System.currentTimeMillis(), difference = 0, num;
        String min, sec;

        boxes[0].setVisibility(player.box0Time != null ? View.VISIBLE : View.INVISIBLE);
        difference = player.box0Time != null ? (player.box0Time - currentTime) / 1000 : 0;
        num = difference / 60;
        min = num >= 10 ? "" + num : "0" + num;
        num = difference % 60;
        sec = num >= 10 ? "" + num : "0" + num;
        boxTimers[0].setText(player.box0Time != null ? (difference > 0 ? (min + ":" + sec) : "") : "");

        boxes[1].setVisibility(player.box1Time != null ? View.VISIBLE : View.INVISIBLE);
        difference = player.box1Time != null ? (player.box1Time - currentTime) / 1000 : 0;
        num = difference / 60;
        min = num >= 10 ? "" + num : "0" + num;
        num = difference % 60;
        sec = num >= 10 ? "" + num : "0" + num;
        boxTimers[1].setText(player.box1Time != null ? (difference > 0 ? (min + ":" + sec) : "") : "");

        boxes[2].setVisibility(player.box2Time != null ? View.VISIBLE : View.INVISIBLE);
        difference = player.box2Time != null ? (player.box2Time - currentTime) / 1000 : 0;
        num = difference / 60;
        min = num >= 10 ? "" + num : "0" + num;
        num = difference % 60;
        sec = num >= 10 ? "" + num : "0" + num;
        boxTimers[2].setText(player.box2Time != null ? (difference > 0 ? (min + ":" + sec) : "") : "");

        boxes[3].setVisibility(player.box3Time != null ? View.VISIBLE : View.INVISIBLE);
        difference = player.box3Time != null ? (player.box3Time - currentTime) / 1000 : 0;
        num = difference / 60;
        min = num >= 10 ? "" + num : "0" + num;
        num = difference % 60;
        sec = num >= 10 ? "" + num : "0" + num;
        boxTimers[3].setText(player.box3Time != null ? (difference > 0 ? (min + ":" + sec) : "") : "");
    }

    private void setupCheckMarks() {
        Player player = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
        int num = player.gamesWon % 3, i = 0;
        for (ImageView imageView : checkMarks) {
            imageView.setVisibility(i < num ? View.VISIBLE : View.INVISIBLE);
            i++;
        }
    }

    private void setupDialogs() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment settingsFragment = new SettingsDialogFragment();
                settingsFragment.setTargetFragment(GarageFragment.this, 1337);
                settingsFragment.show(getParentFragmentManager(), "settings");
            }
        });
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment statsFragment = new StatsDialogFragment();
                statsFragment.show(getParentFragmentManager(), "stats");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setFullscreenMode();

        //play music if needed
        if (currentPlayerViewModel.getCurrentPlayerLiveData().getValue().isMusicPlaying()) {
            playMusic();
        }

        //start timer
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
                                      @Override
                                      public void run() {
                                          setupBoxes();
                                      }
                                  },
                0, 1000);   // 1000 Millisecond  = 1 second
    }

    @Override
    public void onPause() {
        super.onPause();
        //always stop music when leaving fragment
        stopMusic();

        //update db
        new Thread(new Runnable() {
            @Override
            public void run() {
                Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
                catsRoomDatabase.playerDAO().update(currentPlayer);
                currentPlayerViewModel.postCurrentPlayerLiveData(currentPlayer);
            }
        }).start();

        //stop timer
        timer.cancel();
        timer = null;
    }

    private void setupNavigation() {
        fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_garageFragment_to_fightFragment);
            }
        });
        garageCustomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_garageFragment_to_vehicleModificationFragment);
            }
        });
    }

    private void setFullscreenMode() {
        //hide navigation and status bar
        final View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer = null;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer = MediaPlayer.create(context, R.raw.cats_garage_music);
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        }).start();
    }

    public void stopMusic() {
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = null;
    }
}
