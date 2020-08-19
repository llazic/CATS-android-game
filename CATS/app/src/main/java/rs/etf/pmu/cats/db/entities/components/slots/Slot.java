package rs.etf.pmu.cats.db.entities.components.slots;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import rs.etf.pmu.cats.MyApplication;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "slot_table")
public abstract class Slot {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long chassis_id;

    @NonNull
    public int x;

    @NonNull
    public int y;

    public Slot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void drawWithoutSlot(Canvas canvas, int topLeftX, int topLeftY, double ratio) {
        this.setLatestMeasures(canvas, topLeftX, topLeftY, ratio);
        if (this.isOccupied()) this.drawComponent(canvas, topLeftX, topLeftY, ratio);
    }

    public void drawForFight(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime) {
        this.setLatestMeasures(canvas, topLeftX, topLeftY, ratio);
        if (this.isOccupied()) this.drawComponentForFight(canvas, topLeftX, topLeftY, ratio, firstTime);
    }

    public void drawReverse(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime) {
        if (this.isOccupied()) this.drawComponentReverse(canvas, topLeftX, topLeftY, ratio, firstTime);
    }

    //vehicle starting point
    public void draw(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {
        if (this.isOccupied()) this.drawComponent(canvas, vehicleTopLeftX, vehicleTopLeftY, ratio);
        else this.drawSlot(canvas, vehicleTopLeftX, vehicleTopLeftY, ratio);
    }

    @Ignore
    protected double latestWidth, latestHeight, latestX, latestY;

    private void drawSlot(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {
        double sizeRatio = ratio * getSizeConst();
        int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
        int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

        int newX = (int) (ratio * x * MyApplication.DENSITY);
        int newY = (int) (ratio * y * MyApplication.DENSITY);

        Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
        canvas.drawBitmap(resized, (float) (vehicleTopLeftX + newX - newWidth * 0.5), (float) (vehicleTopLeftY + newY - newHeight * 0.5), null);

        //only for WheelsSlot
        this.drawExtra(canvas, resized, vehicleTopLeftX, newX, newWidth, vehicleTopLeftY, newY, newHeight, ratio, MyApplication.DENSITY);

        latestHeight = newHeight;
        latestWidth = newWidth;
        latestX = vehicleTopLeftX + newX - newWidth * 0.5;
        latestY = vehicleTopLeftY + newY - newHeight * 0.5;
    }

    protected void setLatestMeasures(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {
        double sizeRatio = ratio * getSizeConst();
        int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
        int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

        int newX = (int) (ratio * x * MyApplication.DENSITY);
        int newY = (int) (ratio * y * MyApplication.DENSITY);

        latestHeight = newHeight;
        latestWidth = newWidth;
        latestX = vehicleTopLeftX + newX - newWidth * 0.5;
        latestY = vehicleTopLeftY + newY - newHeight * 0.5;
    }


    //only for WheelsSlot
    protected abstract void drawExtra(Canvas canvas, Bitmap resized, int vehicleTopLeftX, int newX, int newWidth, int vehicleTopLeftY, int newY, int newHeight, double ratio, double density);

    protected abstract void drawComponent(Canvas canvas, int topLeftX, int topLeftY, double ratio);

    protected abstract void drawComponentForFight(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime);

    protected abstract void drawComponentReverse(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime);

    //scale drawable
    protected abstract double getSizeConst();

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isOccupied();

    public abstract Bitmap getBitmap();

    public boolean doOverlap(Slot slot) {
        Point l1 = new Point((int) (x - this.getWidth() * 1. / 2), (int) (y - this.getHeight() * 1. / 2));
        Point r1 = new Point((int) (x + this.getWidth() * 1. / 2), (int) (y + this.getHeight() * 1. / 2));

        Point l2 = new Point((int) (slot.getX() - slot.getWidth() * 1. / 2), (int) (slot.getY() - slot.getHeight() * 1. / 2));
        Point r2 = new Point((int) (slot.getX() + slot.getWidth() * 1. / 2), (int) (slot.getY() + slot.getHeight() * 1. / 2));

        // If one rectangle is on left side of other
        if (l1.x > r2.x || l2.x > r1.x)
            return false;

        // If one rectangle is above other
        if (l1.y > r2.y || l2.y > r1.y)
            return false;

        return true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public abstract long insert(CatsRoomDatabase catsRoomDatabase);

    public abstract Drawable getDrawable();

    public boolean contains(float x, float y) {
        double left = latestX;
        double right = latestX + latestWidth;
        double top = latestY;
        double bottom = latestY + latestHeight;
        return x >= left && x <= right && y >= top && y <= bottom;
    }

    public abstract void removeFromUse();

    public abstract boolean canSetNewComponent(DrawableComponent drawableComponent, float x, float y);

    public abstract DrawableComponent setNewComponentAndReturnOld(DrawableComponent drawableComponent, float x, float y);

    public abstract DrawableComponent getSelectedComponentAndRemoveItFromUse(float x, float y);

    public abstract int getHealth();

    public abstract void handleTouchDuringFight();
}
