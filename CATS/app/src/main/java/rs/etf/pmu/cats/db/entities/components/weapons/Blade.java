package rs.etf.pmu.cats.db.entities.components.weapons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "blade_table")
public class Blade extends Weapon {

    public static Bitmap bladeBitmap;
    public static Drawable bladeDrawable;

    public Blade(){ }

    @Override
    protected double getSizeConst() { return 0.7; }

    @Override
    public Bitmap getBitmap() { return bladeBitmap; }

    public void setBitmap(Bitmap bladeBitmap) { Blade.bladeBitmap = bladeBitmap; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.bladeDAO().insert(this);
        return this.id;
    }

    @Override
    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.bladeDAO().update(this);
    }

    @Override
    public Drawable getDrawable() {
        return bladeDrawable;
    }

    @Ignore
    public float rotationAngle;

    @Ignore
    public boolean shouldRotate = true;

    public void drawForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            super.firstTimeCalculations(x, y, ratio);
        }

        int cosConst = (int) (fightWidth * Math.cos(Math.toRadians(360 - rotationAngle)));
        int sinConst = (int) (fightWidth * Math.sin(Math.toRadians(360 - rotationAngle)));
        hitPointX = vehicleTopLeftX + fightXConst + cosConst;
        hitPointY = vehicleTopLeftY + fightYConst + (int) (fightHeight * 0.5) - sinConst;// - (int) (fightWidth * Math.sin(360 - rotationAngle));

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationAngle, (float)(fightHeight * 0.5), (float)(fightHeight * 0.5));
        matrix.postTranslate(fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY);

        canvas.drawBitmap(fightBitmap, matrix, null);
    }

    public void drawReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(x, y, ratio);
        }
        int cosConst = (int) (fightWidth * Math.cos(Math.toRadians(360 - rotationAngle)));
        int sinConst = (int) (fightWidth * Math.sin(Math.toRadians(360 - rotationAngle)));
        hitPointX = fightXConst + vehicleTopLeftX + fightHeight - cosConst;
        hitPointY = fightYConst + vehicleTopLeftY + (int) (fightHeight * 0.5) - sinConst;

        Matrix matrix = new Matrix();
        matrix.postRotate((180 - rotationAngle) % 360, (float)(fightHeight * 0.5), (float)(fightHeight * 0.5));
        matrix.postTranslate(fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY);

        canvas.drawBitmap(fightBitmap, matrix, null);
    }

    public static Blade generateRandomBlade(long player_id) {
        Blade randomComponent = new Blade();
        randomComponent.power = 5000 + (int) (Math.random() * 2001);
        randomComponent.health = 100 + (int) (Math.random() * 101);
        randomComponent.energy = 5 + (int) (Math.random() * 4);
        randomComponent.player_id = player_id;
        return randomComponent;
    }

    public DrawableComponent generateRandom(long player_id) {
        return Blade.generateRandomBlade(player_id);
    }

    @Override
    public void handleTouchDuringFight() {
        this.shouldRotate = true;
    }
}