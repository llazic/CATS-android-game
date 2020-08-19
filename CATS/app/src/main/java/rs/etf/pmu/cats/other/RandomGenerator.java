package rs.etf.pmu.cats.other;

import java.util.ArrayList;
import java.util.List;

import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;
import rs.etf.pmu.cats.db.entities.components.weapons.Chainsaw;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;
import rs.etf.pmu.cats.db.entities.components.wheels.BigWheels;
import rs.etf.pmu.cats.db.entities.components.wheels.Wheels;

public class RandomGenerator {

    private static DrawableComponent[] components = {
            new TankChassis(),
            new Blade(),
            new Chainsaw(),
            new Rocket(),
            new Stinger(),
            new BigWheels()
    };

    public static List<DrawableComponent> generateForRegistration(long player_id, CatsRoomDatabase catsRoomDatabase) {
        List<DrawableComponent> drawableComponents = new ArrayList<>();

        Chassis chassis = TankChassis.generateRandomTankChassis(player_id);
        chassis.inUse = true;
        drawableComponents.add(chassis);
        drawableComponents.add(Rocket.generateRandomRocket(player_id));
        if (Math.random() < 0.6) drawableComponents.add(BigWheels.generateRandomBigWheels(player_id));
        int randomNum = 1 + (int) (Math.random() * 4);
        drawableComponents.add(components[randomNum].generateRandom(player_id));

        for (DrawableComponent d : drawableComponents) d.insert(catsRoomDatabase);

        return drawableComponents;
    }

    public static List<DrawableComponent> generateNewBox(long player_id, final CatsRoomDatabase catsRoomDatabase){
        final List<DrawableComponent> drawableComponents = new ArrayList<>();
        int randomNum = 0;

        for (int i = 0; i < 3; i++) {
            randomNum = (int) (Math.random() * 6);
            drawableComponents.add(components[randomNum].generateRandom(player_id));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (DrawableComponent d : drawableComponents) d.insert(catsRoomDatabase);
            }
        }).start();

        return drawableComponents;
    }

    public static Chassis generateOpponent() {
        TankChassis tankChassis = TankChassis.generateRandomTankChassis(-1);
        for (Slot s : tankChassis.slots) {
            if (s instanceof WeaponSlot) {
                WeaponSlot weaponSlot = (WeaponSlot)s;
                int randomNum = 1 + (int) (Math.random() * 4);
                weaponSlot.setWeapon((Weapon) (components[randomNum].generateRandom(-1)));
            } else if (s instanceof WheelsSlot) {
                if (Math.random() < 0.5) {
                    WheelsSlot wheelsSlot = (WheelsSlot)s;
                    wheelsSlot.setWheels(BigWheels.generateRandomBigWheels(-1));
                }
            }
        }
        return tankChassis;
    }

}
