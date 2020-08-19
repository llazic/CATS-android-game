package rs.etf.pmu.cats.db.daos.components.chassis;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;

@Dao
public abstract class TankChassisDAO {

    @Insert
    public abstract long insert(TankChassis tankChassis);

    @Query("SELECT * " +
            "FROM tank_chassis_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<TankChassis>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM tank_chassis_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<TankChassis> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(TankChassis... tankChassis);
}
