package rs.etf.pmu.cats.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import rs.etf.pmu.cats.db.daos.PlayerDAO;
import rs.etf.pmu.cats.db.entities.*;

@Database(version = 6,
    entities = {
        Player.class
    },
    exportSchema = false)
public abstract class CatsRoomDatabase extends RoomDatabase {

    private static CatsRoomDatabase instance;

    public abstract PlayerDAO playerDAO();

    public static CatsRoomDatabase getDatabase(Context context){
        if (instance == null) {
            synchronized (CatsRoomDatabase.class){
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(), CatsRoomDatabase.class, "cats_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return instance;
    }
}
