package rs.etf.pmu.cats.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.viewModels.CurrentPlayerViewModel;

public class StatsDialogFragment extends DialogFragment {

    private CurrentPlayerViewModel currentPlayerViewModel;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.stats_dialog, null);
        currentPlayerViewModel = ViewModelProviders.of(getActivity()).get(CurrentPlayerViewModel.class);
        Player currentPlayer = currentPlayerViewModel.getCurrentPlayerLiveData().getValue();

        if (currentPlayer.getGamesWon() + currentPlayer.getGamesLost() > 0) {
            int winRate = (int) ((currentPlayer.getGamesWon() * 100.0) / (currentPlayer.getGamesLost() + currentPlayer.getGamesWon()));
            //int winRate = (int) ((2 * 100.0) / (3 + 2));
            TextView statsTextView = view.findViewById(R.id.statsTextView);
            statsTextView.setText("Your win rate is " + winRate + "%");
       }

        builder.setView(view);
        return builder.create();
    }
}

