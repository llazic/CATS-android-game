package rs.etf.pmu.cats.db.daos.components.weapons;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.components.weapons.Chainsaw;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;

@Dao
public abstract class ChainsawDAO {

    @Insert
    public abstract long insert(Chainsaw chainsaw);

    @Query("SELECT * " +
            "FROM chainsaw_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Chainsaw>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM chainsaw_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<Chainsaw> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(Chainsaw... chainsaws);

}
