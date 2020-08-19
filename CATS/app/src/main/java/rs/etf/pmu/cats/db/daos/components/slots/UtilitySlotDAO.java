package rs.etf.pmu.cats.db.daos.components.slots;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;

@Dao
public abstract class UtilitySlotDAO {

    @Insert
    public abstract long insert(UtilitySlot utilitySlot);

    @Query("SELECT * " +
            "FROM utility_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract LiveData<List<UtilitySlot>> getAllByChassisId(long chassis_id);

    @Query("SELECT * " +
            "FROM utility_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract List<UtilitySlot> getAllByChassisIdNow(long chassis_id);
}
