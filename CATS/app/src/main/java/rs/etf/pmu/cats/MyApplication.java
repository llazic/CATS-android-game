package rs.etf.pmu.cats;

import android.app.Application;
import android.graphics.BitmapFactory;

import com.facebook.stetho.Stetho;

import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;
import rs.etf.pmu.cats.db.entities.components.weapons.Chainsaw;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;
import rs.etf.pmu.cats.db.entities.components.wheels.BigWheels;

public class MyApplication extends Application {

    public static final double INITIAL_DENSITY = 2.625; //density for pixel 2
    public static double DENSITY;

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        initBitmaps();
    }

    private void initBitmaps(){
        TankChassis.tankChassisBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.tank_chassis);
        TankChassis.tankChassisDrawable = getDrawable(R.drawable.tank_chassis);

        UtilitySlot.utilitySlotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.utility_slot);
        WeaponSlot.weaponSlotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.weapon_slot);
        WheelsSlot.wheelsSlotBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.wheel_slot);

        Stinger.stingerBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.stinger);
        Stinger.stingerDrawable = getDrawable(R.drawable.stinger);

        Chainsaw.chainsawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.chainsaw);
        Chainsaw.chainsawDrawable = getDrawable(R.drawable.chainsaw);

        Rocket.rocketLauncherBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rocket_launcher);
        Rocket.rocketLauncherDrawable = getDrawable(R.drawable.rocket_launcher);
        Rocket.rocketBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rocket);
        Rocket.rocketDrawable = getDrawable(R.drawable.rocket);

        Blade.bladeBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.blade);
        Blade.bladeDrawable = getDrawable(R.drawable.blade);

        BigWheels.bigWheelsBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.big_wheels);
        BigWheels.bigWheelsDrawable = getDrawable(R.drawable.big_wheels);

        DENSITY = getResources().getDisplayMetrics().density / INITIAL_DENSITY;
    }
}
