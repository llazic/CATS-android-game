package rs.etf.pmu.cats.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import rs.etf.pmu.cats.db.entities.Player;

public class CurrentPlayerViewModel extends ViewModel {

    private MutableLiveData<Player> currentPlayerLiveData;

    public CurrentPlayerViewModel(){
        currentPlayerLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Player> getCurrentPlayerLiveData() {
        return currentPlayerLiveData;
    }

    public void setCurrentPlayerLiveData(Player player){
        currentPlayerLiveData.setValue(player);
    }

    public void postCurrentPlayerLiveData(Player player){
        currentPlayerLiveData.postValue(player);
    }
}
