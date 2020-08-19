package rs.etf.pmu.cats.db.entities.components.chassis;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

import androidx.room.Entity;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.UtilitySlot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;

@Entity(tableName = "tank_chassis_table")
public class TankChassis extends Chassis {

    private static Point[] pointsArray = {
            new Point(1000, 350), new Point(350, 420), new Point(250, 300), new Point(350, 180),
            new Point(550, 420), new Point(450, 300), new Point(550, 180), new Point(750, 420),
            new Point(650, 300), new Point(850, 300), new Point(750, 200), new Point(900, 420)};

    public static Bitmap tankChassisBitmap;
    public static Drawable tankChassisDrawable;

    public TankChassis() {
        this.slots = new ArrayList<>();
    }

    @Override
    public void draw(Canvas canvas, Resources resources, int x, int y, boolean drawSlots) {}

    public static TankChassis generateRandomTankChassis(long player_id) {
        TankChassis tankChassis = new TankChassis();

        tankChassis.player_id = player_id;

        int health = 1000 + (int) (Math.random() * 1001);
        tankChassis.health = health;

        int energy = 5 + (int) (Math.random() * 25);
        tankChassis.energy = energy;

        tankChassis.slots = new ArrayList<>();
        WheelsSlot wheelsSlot = new WheelsSlot(380, 575, 400);
        //ovo ukloniti
        //wheelsSlot.setWheels(new BigWheels());
        tankChassis.slots.add(wheelsSlot);

        ArrayList<Point> availablePoints = new ArrayList<>();
        for (Point p : pointsArray) {
            availablePoints.add(p);
            //slots.add(new WeaponSlot(p.x, p.y, resources));
        }

        generateWeaponSlots(availablePoints, tankChassis.slots);
        generateUtilitySlots(availablePoints, tankChassis.slots);

        for (Slot s : tankChassis.slots) {
            s.chassis_id = tankChassis.id;
        }

        return tankChassis;
    }

    private static void generateWeaponSlots(ArrayList<Point> availablePoints, List<Slot> slots) {
        int numberOfSlots;
        double randomNumber = Math.random();
        //60% 1 slot, 30% 2 slots, 10% 3 slots
        if (randomNumber < 0.6) numberOfSlots = 1;
        else if (randomNumber < 0.9) numberOfSlots = 2;
        else numberOfSlots = 3;

        System.out.println(numberOfSlots + " weapon slots");

        for (int i = 0; i < numberOfSlots; i++) {
            int index = (int) (Math.random() * availablePoints.size());
            Point p = availablePoints.get(index);

            WeaponSlot weaponSlot = new WeaponSlot(p.x, p.y);
            //ovo ukloniti
            //weaponSlot.setWeapon(new Stinger());

            slots.add(weaponSlot);
            availablePoints.remove(index);
        }
    }

    private static void generateUtilitySlots(ArrayList<Point> availablePoints, List<Slot> slots) {
        double randomNumber = Math.random();
        //70% 1 slot, 30% 0 slots
        if (randomNumber > 0.7) return;

        System.out.println("1 utility slot");

        int index = (int) (Math.random() * availablePoints.size());
        Point p = availablePoints.get(index);
        slots.add(new UtilitySlot(p.x, p.y));
        availablePoints.remove(index);
    }

    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.tankChassisDAO().insert(this);
        for (Slot s : slots) {
            s.chassis_id = this.id;
            s.insert(catsRoomDatabase);
        }
        return this.id;
    }

    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.tankChassisDAO().update(this);
    }

    @Override
    public Bitmap getBitmap() {
        return tankChassisBitmap;
    }

    @Override
    public Drawable getDrawable() {
        return tankChassisDrawable;
    }

    @Override
    protected double getSizeConst() { return 0.65; }

    public DrawableComponent generateRandom(long player_id) {
        return TankChassis.generateRandomTankChassis(player_id);
    }
}
