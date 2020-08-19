package rs.etf.pmu.cats.db.entities.components.weapons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import rs.etf.pmu.cats.MyApplication;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "rocket_table")
public class Rocket extends Weapon{

    public static Bitmap rocketLauncherBitmap;
    public static Drawable rocketLauncherDrawable;

    public static Bitmap rocketBitmap;
    public static Drawable rocketDrawable;

    public Rocket(){ }

    @Override
    protected double getSizeConst() { return 0.7; }

    @Override
    public Bitmap getBitmap() { return rocketLauncherBitmap; }

    public void setBitmap(Bitmap rocketBitmap) { Rocket.rocketLauncherBitmap = rocketBitmap; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.rocketDAO().insert(this);
        return this.id;
    }

    @Override
    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.rocketDAO().update(this);
    }

    @Override
    public Drawable getDrawable() { return rocketLauncherDrawable; }

    @Ignore
    private boolean shouldRocketLaunch;
    @Ignore
    private int rocketOffset, fightRocketXConst, fightRocketYConst, fightRocketWidth, fightRocketHeight;
    @Ignore
    private Bitmap fightRocketBitmap;

    @Override
    public void drawForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(x, y, ratio);
        }

        hitPointX = fightXConst + vehicleTopLeftX + fightRocketWidth + rocketOffset;
        hitPointY = fightYConst + vehicleTopLeftY + (int) (fightRocketHeight * 0.5);

        canvas.drawBitmap(fightRocketBitmap, vehicleTopLeftX + fightRocketXConst + rocketOffset, vehicleTopLeftY + fightRocketYConst, null);
        canvas.drawBitmap(fightBitmap, fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY, null);
    }

    @Override
    public void drawReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, int x, int y, double ratio, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(x, y, ratio);
        }

        hitPointX = fightXConst + vehicleTopLeftX - fightRocketWidth + fightRocketHeight - rocketOffset;
        hitPointY = fightYConst + vehicleTopLeftY + (int) (fightRocketHeight * 0.5);

        Matrix matrix = new Matrix();
        matrix.postRotate(180, (float)(fightHeight * 0.5), (float)(fightHeight * 0.5));
        matrix.postTranslate(fightXConst + vehicleTopLeftX, fightYConst + vehicleTopLeftY);

        Matrix rocketMatrix = new Matrix();
        rocketMatrix.postRotate(180, (float)(fightRocketHeight * 0.5), (float)(fightRocketHeight * 0.5));
        rocketMatrix.postTranslate(vehicleTopLeftX + fightRocketXConst - rocketOffset, vehicleTopLeftY + fightRocketYConst);

        canvas.drawBitmap(fightRocketBitmap, rocketMatrix, null);
        canvas.drawBitmap(fightBitmap, matrix, null);
    }

    @Override
    protected void firstTimeCalculations(int x, int y, double ratio) {
        super.firstTimeCalculations(x, y, ratio);
        double sizeRatio = ratio * getSizeConst();
        //rocket
        fightRocketWidth = (int) (rocketBitmap.getWidth() * sizeRatio);
        fightRocketHeight = (int) (rocketBitmap.getHeight() * sizeRatio);

        int newRocketX = (int) (ratio * x * MyApplication.DENSITY);
        int newRocketY = (int) (ratio * y * MyApplication.DENSITY);

        fightRocketXConst = (int) (newRocketX - fightRocketHeight * 0.5);
        fightRocketYConst = (int) (newRocketY - fightRocketHeight * 0.5);

        fightRocketBitmap = Bitmap.createScaledBitmap(rocketBitmap, fightRocketWidth, fightRocketHeight, true);
    }

    public boolean shouldRocketLaunch() {
        return shouldRocketLaunch;
    }

    public void setShouldRocketLaunch(boolean shouldRocketLaunch) {
        this.shouldRocketLaunch = shouldRocketLaunch;
    }

    public void resetRocketOffset() {
        this.rocketOffset = 0;
    }

    public void increaseRocketOffset(int xOffset) {
        this.rocketOffset += xOffset;
    }

    public static Rocket generateRandomRocket(long player_id) {
        Rocket randomComponent = new Rocket();
        randomComponent.power = 300 + (int) (Math.random() * 201);
        randomComponent.health = 100 + (int) (Math.random() * 101);
        randomComponent.energy = 5 + (int) (Math.random() * 4);
        randomComponent.player_id = player_id;
        return randomComponent;
    }

    public DrawableComponent generateRandom(long player_id) {
        return Rocket.generateRandomRocket(player_id);
    }
}