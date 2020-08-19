package rs.etf.pmu.cats.db.daos.components.utilities;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.db.entities.components.utilities.Utility;

@Dao
public abstract class UtilityDAO {

    @Insert
    public abstract long insert(Utility utility);

    @Query("SELECT * " +
            "FROM utility_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Utility>> getAllByPlayerId(long player_id);

    @Update
    public abstract int update(Utility... utilities);
}
