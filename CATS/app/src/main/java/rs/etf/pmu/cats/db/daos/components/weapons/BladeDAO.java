package rs.etf.pmu.cats.db.daos.components.weapons;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;

@Dao
public abstract class BladeDAO {

    @Insert
    public abstract long insert(Blade blade);

    @Query("SELECT * " +
            "FROM blade_table p " +
            "WHERE p.player_id = :player_id")
    public abstract LiveData<List<Blade>> getAllByPlayerId(long player_id);

    @Query("SELECT * " +
            "FROM blade_table p " +
            "WHERE p.player_id = :player_id")
    public abstract List<Blade> getAllByPlayerIdNow(long player_id);

    @Update
    public abstract int update(Blade... blades);

}