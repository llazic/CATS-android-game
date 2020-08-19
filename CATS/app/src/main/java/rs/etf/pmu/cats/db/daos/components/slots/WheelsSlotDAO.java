package rs.etf.pmu.cats.db.daos.components.slots;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;

@Dao
public abstract class WheelsSlotDAO {

    @Insert
    public abstract long insert(WheelsSlot wheelsSlot);

    @Query("SELECT * " +
            "FROM wheels_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract LiveData<List<WheelsSlot>> getAllByChassisId(long chassis_id);

    @Query("SELECT * " +
            "FROM wheels_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract List<WheelsSlot> getAllByChassisIdNow(long chassis_id);
}
