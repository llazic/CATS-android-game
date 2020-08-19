package rs.etf.pmu.cats.db.daos.components.weapons;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;

@Dao
public abstract class StingerDAO {

    @Insert
    public abstract long insert(Stinger stinger);

    @Query("SELECT * " +
            "FROM stinger_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Stinger>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM stinger_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<Stinger> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(Stinger... stingers);
}
