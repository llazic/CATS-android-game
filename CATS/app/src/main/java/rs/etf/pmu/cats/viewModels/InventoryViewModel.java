package rs.etf.pmu.cats.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import rs.etf.pmu.cats.db.entities.Player;

public class InventoryViewModel extends ViewModel {

    private MutableLiveData<Inventory> inventoryLiveData;

    public InventoryViewModel(){
        inventoryLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Inventory> getInventoryLiveData() {
        return inventoryLiveData;
    }

    public void setInventoryLiveData(Inventory inventory){
        inventoryLiveData.setValue(inventory);
    }

    public void postInventoryLiveData(Inventory inventory){
        inventoryLiveData.postValue(inventory);
    }
}
