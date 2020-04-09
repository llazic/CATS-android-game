package rs.etf.pmu.cats.fragments;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;

public class EntryFragment extends Fragment {

    private CurrentPlayerViewModel currentPlayerViewModel;
    private NavController navController;

    private List<Player> allPlayers;
    private Player currentPlayer;
    private int currentIndex;
    private boolean firstTimeLoadingPlayers = true;

    private TextView textViewCurrentPlayer;
    private EditText editTextNickname;
    private Button buttonPlay, buttonRegister;
    private ImageView leftArrow, rightArrow;

    private CatsRoomDatabase catsRoomDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entry_fragment, container, false);

        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        getViews(view);

        catsRoomDatabase = CatsRoomDatabase.getDatabase(getContext());
        loadPlayers();

        configureActions();



        return view;
    }

    private void getViews(View view){
        textViewCurrentPlayer = view.findViewById(R.id.textViewCurrentPlayer);
        editTextNickname = view.findViewById(R.id.editText_player_nickname);
        buttonPlay = view.findViewById(R.id.buttonPlay);
        buttonRegister = view.findViewById(R.id.buttonRegister);
        leftArrow = view.findViewById(R.id.leftArrow);
        rightArrow = view.findViewById(R.id.rightArrow);
    }

    private void loadPlayers(){
        catsRoomDatabase.playerDAO().getAllPlayers().observe(getActivity(), new Observer<List<Player>>() {
            @Override
            public void onChanged(List<Player> players) {
                allPlayers = players;
                if (players.size() != 0) {
                    if (firstTimeLoadingPlayers) currentIndex = 0;
                    else currentIndex = players.size() - 1;
                    currentPlayer = players.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });
    }

    private void configureActions(){

        rightArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allPlayers.size() != 0) {
                    currentIndex = (currentIndex + 1) % allPlayers.size();
                    currentPlayer = allPlayers.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allPlayers.size() != 0) {
                    int numOfPlayers = allPlayers.size();
                    currentIndex = (currentIndex + numOfPlayers - 1) % numOfPlayers;
                    currentPlayer = allPlayers.get(currentIndex);
                    textViewCurrentPlayer.setText(currentPlayer.getNickname());
                }
            }
        });

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final String nickname = editTextNickname.getText().toString();

                if (nickname != null && nickname.equals("") == false){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Player newPlayer = new Player(nickname);
                                long id = catsRoomDatabase.playerDAO().insert(newPlayer);
                                newPlayer.setId(id);
                                currentPlayerViewModel.postCurrentPlayerLiveData(newPlayer);
                                navController.navigate(R.id.action_entryFragment_to_garageFragment);
                            } catch (SQLiteConstraintException e){
                                //nickname already exists
                                view.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getContext(), "That nickname already exists!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).start();
                } else {
                    //Player didn't type in nickname
                    Toast.makeText(getContext(), "Enter your nickname!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentPlayer != null) {
                    currentPlayerViewModel.setCurrentPlayerLiveData(currentPlayer);
                    navController.navigate(R.id.action_entryFragment_to_garageFragment);
                } else {
                    Toast.makeText(getContext(), "You haven't picked a player!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();

        setFullscreenMode();
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
}
