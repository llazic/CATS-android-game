package rs.etf.pmu.cats.db.daos.components.chassis;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;

@Dao
public abstract class ChassisDAO {

    @Insert
    public abstract long insert(Chassis chassis);

    @Query("SELECT * " +
            "FROM chassis_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Chassis>> getAllByPlayerId(long player_id);
}
