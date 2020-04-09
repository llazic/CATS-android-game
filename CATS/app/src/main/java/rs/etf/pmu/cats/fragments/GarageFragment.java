package rs.etf.pmu.cats.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.dialogs.SettingsDialogFragment;
import rs.etf.pmu.cats.dialogs.StatsDialogFragment;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;

public class GarageFragment extends Fragment {

    //CurrentPlayerViewModel has actual settings, and everything is up to date
    //when onPause occurs currentPlayer from CurrentPlayerViewModel is saved to db
    private CurrentPlayerViewModel currentPlayerViewModel;

    private ImageView[] boxes;
    private TextView[] boxTimers;

    private ImageView[] checkMarks;
    private ImageView stats, settings;
    private Button fightButton;

    private MediaPlayer mediaPlayer;

    private CatsRoomDatabase catsRoomDatabase;

    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.garage_fragment, container, false);

        catsRoomDatabase = CatsRoomDatabase.getDatabase(context);
        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);

        getViews(view);
        setupBoxes(view);
        setupDialogs();
        setupFightButton();

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private void getViews(View view){
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
    }

    private void setupBoxes(View view){}

    private void setupDialogs(){
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
    }

    private void setupFightButton(){
        fightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_garageFragment_to_fightFragment);
            }
        });
    }

    private void setFullscreenMode(){
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

    public void playMusic(){
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

    public void stopMusic(){
        if (mediaPlayer != null) mediaPlayer.stop();
        mediaPlayer = null;
    }
}
