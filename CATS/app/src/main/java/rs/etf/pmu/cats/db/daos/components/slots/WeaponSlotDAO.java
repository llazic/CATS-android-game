package rs.etf.pmu.cats.db.daos.components.slots;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;

@Dao
public abstract class WeaponSlotDAO {

    @Insert
    public abstract long insert(WeaponSlot weaponSlot);

    @Query("SELECT * " +
            "FROM weapon_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract LiveData<List<WeaponSlot>> getAllByChassisId(long chassis_id);

    @Query("SELECT * " +
            "FROM weapon_slot_table p " +
            "WHERE p.chassis_id = :chassis_id")
    public abstract List<WeaponSlot> getAllByChassisIdNow(long chassis_id);
}
