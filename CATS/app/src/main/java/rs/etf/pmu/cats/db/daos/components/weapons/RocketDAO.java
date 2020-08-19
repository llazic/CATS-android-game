package rs.etf.pmu.cats.db.daos.components.weapons;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;

@Dao
public abstract class RocketDAO {

    @Insert
    public abstract long insert(Rocket rocket);

    @Query("SELECT * " +
            "FROM rocket_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Rocket>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM rocket_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<Rocket> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(Rocket... rockets);

}