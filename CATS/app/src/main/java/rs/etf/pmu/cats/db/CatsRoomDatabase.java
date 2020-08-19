package rs.etf.pmu.cats.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import rs.etf.pmu.cats.db.daos.PlayerDAO;
import rs.etf.pmu.cats.db.daos.components.chassis.ChassisDAO;
import rs.etf.pmu.cats.db.daos.components.chassis.TankChassisDAO;
import rs.etf.pmu.cats.db.daos.components.slots.UtilitySlotDAO;
import rs.etf.pmu.cats.db.daos.components.slots.WeaponSlotDAO;
import rs.etf.pmu.cats.db.daos.components.slots.WheelsSlotDAO;
import rs.etf.pmu.cats.db.daos.components.weapons.BladeDAO;
import rs.etf.pmu.cats.db.daos.components.weapons.ChainsawDAO;
import rs.etf.pmu.cats.db.daos.components.weapons.RocketDAO;
import rs.etf.pmu.cats.db.daos.components.weapons.StingerDAO;
import rs.etf.pmu.cats.db.daos.components.wheels.BigWheelsDAO;
import rs.etf.pmu.cats.db.entities.Player;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;
import rs.etf.pmu.cats.db.entities.components.utilities.Utility;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;
import rs.etf.pmu.cats.db.entities.components.weapons.Chainsaw;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;
import rs.etf.pmu.cats.db.entities.components.wheels.BigWheels;
import rs.etf.pmu.cats.db.entities.components.wheels.Wheels;

@Database(version = 23,
        entities = {
                Player.class,
                Chassis.class, TankChassis.class,
                Slot.class, UtilitySlot.class, WeaponSlot.class, WheelsSlot.class,
                Utility.class,
                Weapon.class, Stinger.class, Chainsaw.class, Rocket.class, Blade.class,
                Wheels.class, BigWheels.class
        },
        exportSchema = false)
public abstract class CatsRoomDatabase extends RoomDatabase {

    private static CatsRoomDatabase instance;

    public abstract PlayerDAO playerDAO();

    public abstract ChassisDAO chassisDAO();

    public abstract TankChassisDAO tankChassisDAO();

    public abstract UtilitySlotDAO utilitySlotDAO();

    public abstract WeaponSlotDAO weaponSlotDAO();

    public abstract WheelsSlotDAO wheelsSlotDAO();

    //public abstract UtilityDAO utilityDAO();

    public abstract StingerDAO stingerDAO();

    public abstract ChainsawDAO chainsawDAO();

    public abstract RocketDAO rocketDAO();

    public abstract BladeDAO bladeDAO();

    public abstract BigWheelsDAO bigWheelsDAO();

    public static CatsRoomDatabase getDatabase(Context context) {
        if (instance == null) {
            synchronized (CatsRoomDatabase.class) {
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
