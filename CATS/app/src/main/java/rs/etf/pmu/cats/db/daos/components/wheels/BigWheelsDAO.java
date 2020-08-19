package rs.etf.pmu.cats.db.daos.components.wheels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.components.wheels.BigWheels;

@Dao
public abstract class BigWheelsDAO {

    @Insert
    public abstract long insert(BigWheels bigWheels);

    @Query("SELECT * " +
            "FROM big_wheels_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<BigWheels>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM big_wheels_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<BigWheels> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(BigWheels... bigWheels);
}
