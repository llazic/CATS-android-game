package rs.etf.pmu.cats.db.entities.components.wheels;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import rs.etf.pmu.cats.MyApplication;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "wheels_table")
public abstract class Wheels implements DrawableComponent {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long player_id;

    public Long slot_id = null;

    @NonNull
    public int health;

    public void draw(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, int wheelbase, double ratio) {
        double sizeRatio = ratio * this.getSizeConst();
        int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
        int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

        int newX = (int) (ratio * x * MyApplication.DENSITY);
        int newY = (int) (ratio * y * MyApplication.DENSITY);
        int newWheelBase = (int) (ratio * wheelbase * MyApplication.DENSITY);

        Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
        canvas.drawBitmap(resized, (float)(vehicleTopLeftX + newX - newWidth * 0.5), (float)(vehicleTopLeftY + newY - newHeight * 0.5), null);
        canvas.drawBitmap(resized, (float)(vehicleTopLeftX + newX + newWheelBase - newWidth * 0.5), (float)(vehicleTopLeftY + newY - newHeight * 0.5), null);
    }

    @Ignore
    private double fightSizeRatio;
    @Ignore
    private int fightWidth, fightHeight, fightX, fightY, fightWheelBase, fightXConst, fightYConst;
    @Ignore
    private Bitmap fightBitmap;

    public void drawForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, int wheelbase, double ratio, boolean firstTime) {
        if (firstTime) {
            double sizeRatio = ratio * getSizeConst();
            int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
            int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

            int newX = (int) (ratio * x * MyApplication.DENSITY);
            int newY = (int) (ratio * y * MyApplication.DENSITY);
            int newWheelBase = (int) (ratio * wheelbase * MyApplication.DENSITY);

            Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
            fightSizeRatio = sizeRatio;
            fightWidth = newWidth;
            fightHeight = newHeight;
            fightX = newX;
            fightY = newY;
            fightXConst = (int)(fightX - fightHeight * 0.5);
            fightYConst = (int)(fightY - fightHeight * 0.5);
            fightWheelBase = newWheelBase;
            fightBitmap = resized;
        }

        canvas.drawBitmap(fightBitmap, fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY, null);
        canvas.drawBitmap(fightBitmap, fightXConst + vehicleTopLeftX + fightWheelBase, fightYConst + vehicleTopLeftY, null);
    }

    public void drawReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, int wheelbase, double ratio, boolean firstTime) {
        if (firstTime) {
            double sizeRatio = ratio * getSizeConst();
            int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
            int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

            int newX = (int) (ratio * x * MyApplication.DENSITY);
            int newY = (int) (ratio * y * MyApplication.DENSITY);
            int newWheelBase = (int) (ratio * wheelbase * MyApplication.DENSITY);

            Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
            fightSizeRatio = sizeRatio;
            fightWidth = newWidth;
            fightHeight = newHeight;
            fightX = newX;
            fightY = newY;
            fightXConst = (int)(fightX - fightHeight * 0.5);
            fightYConst = (int)(fightY - fightHeight * 0.5);
            fightWheelBase = newWheelBase;
            fightBitmap = resized;
        }

        Matrix rightMatrix = new Matrix();
        rightMatrix.preScale(-1, 1);
        rightMatrix.setTranslate(fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY);

        Matrix leftMatrix = new Matrix();
        leftMatrix.preScale(-1, 1);
        leftMatrix.setTranslate(fightXConst + vehicleTopLeftX - fightWheelBase, fightYConst + vehicleTopLeftY);

        canvas.drawBitmap(fightBitmap, rightMatrix, null);
        canvas.drawBitmap(fightBitmap, leftMatrix, null);
    }

    public abstract double getSizeConst();

    public abstract Bitmap getBitmap();

    @Override
    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public abstract long insert(CatsRoomDatabase catsRoomDatabase);

    public abstract void update(CatsRoomDatabase catsRoomDatabase);

    @Override
    public boolean isInUse() { return slot_id != null; }

    public void removeFromUse() {
        slot_id = null;
    }

    @Override
    public int getEnergy() { return 0; }

    @Override
    public int getPower() { return 0; }
}
