package rs.etf.pmu.cats.db.daos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.*;

@Dao
public abstract class PlayerDAO {

    @Insert
    public abstract long insert(Player player);

    @Query("SELECT * " +
            "FROM player_table p " +
            "WHERE p.id = :id")
    public abstract Player getPlayer(long id);

    @Update
    public abstract int update(Player... players);

    @Query("SELECT * FROM player_table")
    public abstract LiveData<List<Player>> getAllPlayers();
}
