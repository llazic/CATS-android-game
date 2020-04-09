package rs.etf.pmu.cats.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.fragments.GarageFragment;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;

public class SettingsDialogFragment extends DialogFragment {

    private LinearLayout musicLayout, controlLayout;
    private ImageView musicPlayingImageView, vehicleControlImageView;

    private CurrentPlayerViewModel currentPlayerViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.settings_dialog, null);
        musicLayout = view.findViewById(R.id.music_linear_layout);
        controlLayout = view.findViewById(R.id.control_linear_layout);
        musicPlayingImageView = view.findViewById(R.id.music_playing);
        vehicleControlImageView = view.findViewById(R.id.control_active);

        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);
        setupMusicControl();
        setupVehicleControl();

        builder.setView(view);
        return builder.create();
    }

    private void setupMusicControl() {
        //set the right image
        if (currentPlayerViewModel.getCurrentPlayerLiveData().getValue().isMusicPlaying()){
            musicPlayingImageView.setImageDrawable(getResources().getDrawable(R.drawable.check_mark, null));
        } else {
            musicPlayingImageView.setImageDrawable(getResources().getDrawable(R.drawable.x, null));
        }

        musicLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
                GarageFragment garageFragment = (GarageFragment) getTargetFragment();
                if (currentPlayer.isMusicPlaying()){
                    //music is currently playing, stop it
                    garageFragment.stopMusic();
                    currentPlayer.setMusicPlaying(false);
                    musicPlayingImageView.setImageDrawable(getResources().getDrawable(R.drawable.x, null));
                } else {
                    garageFragment.playMusic();
                    currentPlayer.setMusicPlaying(true);
                    musicPlayingImageView.setImageDrawable(getResources().getDrawable(R.drawable.check_mark, null));
                }
                currentPlayerViewModel.setCurrentPlayerLiveData(currentPlayer);
            }
        });
    }

    private void setupVehicleControl() {
        if(currentPlayerViewModel.getCurrentPlayerLiveData().getValue().isVehicleControl()){
            vehicleControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.check_mark, null));
        } else {
            vehicleControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.x, null));
        }

        controlLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();
                if(currentPlayer.isVehicleControl()){
                    currentPlayer.setVehicleControl(false);
                    vehicleControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.x, null));
                } else {
                    currentPlayer.setVehicleControl(true);
                    vehicleControlImageView.setImageDrawable(getResources().getDrawable(R.drawable.check_mark, null));
                }
                currentPlayerViewModel.setCurrentPlayerLiveData(currentPlayer);
            }
        });
    }
}
