package rs.etf.pmu.cats.db.entities.components.weapons;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import rs.etf.pmu.cats.MyApplication;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "weapon_table")
public abstract class Weapon implements DrawableComponent {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long player_id;

    public Long slot_id = null;

    @NonNull
    public int health;

    @NonNull
    public int energy;

    @NonNull
    public int power;

    public void draw(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio) {
        double sizeRatio = ratio * getSizeConst();
        int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
        int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

        int newX = (int) (ratio * x * MyApplication.DENSITY);
        int newY = (int) (ratio * y * MyApplication.DENSITY);

        Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
        canvas.drawBitmap(resized, (int)(vehicleTopLeftX + newX - newHeight * 0.5), (int)(vehicleTopLeftY + newY - newHeight * 0.5), null);
    }


    public void drawCentrally(Canvas canvas) {
        double ratio = canvas.getHeight() * 0.25 / getBitmap().getHeight();
        int newWidth = (int) (getBitmap().getWidth() * ratio);
        int newHeight = (int) (getBitmap().getHeight() * ratio);

        Bitmap resized = Bitmap.createScaledBitmap(getBitmap(), newWidth, newHeight, true);

        int vehicleX = (int) (canvas.getWidth() * 0.5 - newWidth * 0.5);
        int vehicleY = (int) (canvas.getHeight() * 0.5 - newHeight * 0.5);

        canvas.drawBitmap(resized, vehicleX, vehicleY, null);
    }

    @Ignore
    protected double fightSizeRatio;
    @Ignore
    protected int fightWidth, fightHeight, fightX, fightY, fightXConst, fightYConst;
    @Ignore
    protected Bitmap fightBitmap;

    @Ignore
    public int hitPointX, hitPointY;

    @Ignore
    public int rotationAngle;

    public void drawForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(x, y, ratio);
        }

        hitPointX = fightXConst + vehicleTopLeftX + fightWidth;
        hitPointY = fightYConst + vehicleTopLeftY + (int) (fightHeight * 0.5);

        canvas.drawBitmap(fightBitmap, fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY, null);
    }

    public void drawReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(x, y, ratio);
        }

        hitPointX = fightXConst + vehicleTopLeftX - fightWidth + fightHeight;
        hitPointY = fightYConst + vehicleTopLeftY + (int) (fightHeight * 0.5);

        Matrix matrix = new Matrix();
        matrix.postRotate(180, (float)(fightHeight * 0.5), (float)(fightHeight * 0.5));
        matrix.postTranslate(fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY);

        canvas.drawBitmap(fightBitmap, matrix, null);
    }

    protected void firstTimeCalculations(int x, int y, double ratio) {
        double sizeRatio = ratio * getSizeConst();
        int newWidth = (int) (this.getBitmap().getWidth() * sizeRatio);
        int newHeight = (int) (this.getBitmap().getHeight() * sizeRatio);

        int newX = (int) (ratio * x * MyApplication.DENSITY);
        int newY = (int) (ratio * y * MyApplication.DENSITY);

        Bitmap resized = Bitmap.createScaledBitmap(this.getBitmap(), newWidth, newHeight, true);
        fightSizeRatio = sizeRatio;
        fightWidth = newWidth;
        fightHeight = newHeight;
        fightX = newX;
        fightY = newY;
        fightXConst = (int) (fightX - fightHeight * 0.5);
        fightYConst = (int) (fightY - fightHeight * 0.5);
        fightBitmap = resized;
    }

    public abstract Bitmap getBitmap();

    protected abstract double getSizeConst();

    public abstract long insert(CatsRoomDatabase catsRoomDatabase);

    public abstract void update(CatsRoomDatabase catsRoomDatabase);

    @Override
    public boolean isInUse() {
        return slot_id != null;
    }

    public void removeFromUse() {
        slot_id = null;
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getPower() {
        return power;
    }

    public void handleTouchDuringFight() {}
}
