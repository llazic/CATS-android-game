package rs.etf.pmu.cats.db.entities.components.slots;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import rs.etf.pmu.cats.MyApplication;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;
import rs.etf.pmu.cats.db.entities.components.wheels.Wheels;

@Entity(tableName = "wheels_slot_table")
public class WheelsSlot extends Slot {

    @Ignore
    private Wheels wheels;

    @NonNull
    public int wheelbase;

    public static Bitmap wheelsSlotBitmap;

    public WheelsSlot(int x, int y, int wheelbase){
        super(x, y);
        this.wheelbase = wheelbase;
    }

    @Ignore
    private int latestWheelBase;

    @Override
    protected void drawExtra(Canvas canvas, Bitmap resized, int vehicleTopLeftX, int newX, int newWidth, int vehicleTopLeftY, int newY, int newHeight, double ratio, double density) {
        int newWheelBase = (int) (ratio * wheelbase * density);
        latestWheelBase = newWheelBase;
        canvas.drawBitmap(resized, (float)(vehicleTopLeftX + newX + newWheelBase - newWidth * 0.5), (float)(vehicleTopLeftY + newY - newHeight * 0.5), null);
    }

    @Override
    protected void drawComponent(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {
        this.wheels.draw(canvas, vehicleTopLeftX, vehicleTopLeftY, x, y, wheelbase, ratio);
    }

    @Override
    protected void drawComponentForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio, boolean firstTime) {
        this.setLatestMeasures(canvas, vehicleTopLeftX, vehicleTopLeftY, ratio);
        int newWheelBase = (int) (ratio * wheelbase * MyApplication.DENSITY);
        latestWheelBase = newWheelBase;
        this.wheels.drawForFight(canvas, vehicleTopLeftX, vehicleTopLeftY, x, y, wheelbase, ratio, firstTime);
    }

    @Override
    protected void drawComponentReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio, boolean firstTime) {
        this.wheels.drawReverse(canvas, vehicleTopLeftX, vehicleTopLeftY, TankChassis.tankChassisBitmap.getWidth() - x, y, wheelbase, ratio, firstTime);
    }

    @Override
    protected double getSizeConst() {
        return 0.8;
    }

    @Override
    public int getWidth() { return wheelsSlotBitmap.getWidth(); }

    @Override
    public int getHeight() { return wheelsSlotBitmap.getHeight(); }

    @Override
    public boolean isOccupied() { return wheels != null; }

    @Override
    public Bitmap getBitmap() { return wheelsSlotBitmap; }

    public Wheels getWheels() { return wheels; }

    public void setWheels(Wheels wheels) { this.wheels = wheels; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.wheelsSlotDAO().insert(this);
        return this.id;
    }

    @Override
    public Drawable getDrawable() {
        return null;
    }

    @Override
    public boolean contains(float x, float y) {
        double left = latestX + latestWheelBase;
        double right = latestX + latestWheelBase + latestWidth;
        double top = latestY;
        double bottom = latestY + latestHeight;
        return super.contains(x, y) || (x >= left && x <= right && y >= top && y <= bottom);
    }

    @Override
    public void removeFromUse() {
        if (wheels != null) {
            wheels.removeFromUse();
            wheels = null;
        }
    }

    @Override
    public boolean canSetNewComponent(DrawableComponent drawableComponent, float x, float y) {
        return drawableComponent instanceof Wheels && contains(x, y);
    }

    @Override
    public DrawableComponent setNewComponentAndReturnOld(DrawableComponent drawableComponent, float x, float y) {
        if (canSetNewComponent(drawableComponent, x, y)) {
            if (this.wheels != null) {
                wheels.removeFromUse();
            }
            Wheels oldWheels = wheels;
            this.wheels = (Wheels) drawableComponent;
            this.wheels.slot_id = this.id;
            return oldWheels;
        }
        return null;
    }

    @Override
    public DrawableComponent getSelectedComponentAndRemoveItFromUse(float x, float y) {
        if (contains(x, y)) {
            Wheels oldWheels = this.wheels;
            removeFromUse();
            return oldWheels;
        }
        return null;
    }

    @Override
    public int getHealth() {
        return wheels != null ? wheels.health : 0;
    }

    @Override
    public void handleTouchDuringFight() {}
}
