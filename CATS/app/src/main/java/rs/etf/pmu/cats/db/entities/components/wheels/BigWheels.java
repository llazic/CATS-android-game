package rs.etf.pmu.cats.db.entities.components.wheels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Stinger;

@Entity(tableName = "big_wheels_table")
public class BigWheels extends Wheels {

    public static Bitmap bigWheelsBitmap;
    public static Drawable bigWheelsDrawable;

    public BigWheels(){}

    @Override
    public double getSizeConst() { return 0.95; }

    @Override
    public Bitmap getBitmap() { return bigWheelsBitmap; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.bigWheelsDAO().insert(this);
        return this.id;
    }

    @Override
    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.bigWheelsDAO().update(this);
    }

    @Override
    public void drawCentrally(Canvas canvas) {
        double ratio = canvas.getHeight() * 0.5 / getBitmap().getHeight();
        int newWidth = (int) (getBitmap().getWidth() * ratio);
        int newHeight = (int) (getBitmap().getHeight() * ratio);

        Bitmap resized = Bitmap.createScaledBitmap(getBitmap(), newWidth, newHeight, true);

        int vehicleX = (int) (canvas.getWidth() * 0.5 - newWidth * 0.5);
        int vehicleY = (int) (canvas.getHeight() * 0.5 - newHeight * 0.5);

        canvas.drawBitmap(resized, vehicleX, vehicleY, null);
    }

    @Override
    public Drawable getDrawable() {
        return bigWheelsDrawable;
    }

    public static BigWheels generateRandomBigWheels(long player_id) {
        BigWheels randomComponent = new BigWheels();
        randomComponent.health = 50 + (int) (Math.random() * 101);
        randomComponent.player_id = player_id;
        return randomComponent;
    }

    public DrawableComponent generateRandom(long player_id) {
        return BigWheels.generateRandomBigWheels(player_id);
    }
}
