package rs.etf.pmu.cats.db.entities.components.weapons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "stinger_table")
public class Stinger extends Weapon {

    public static Bitmap stingerBitmap;
    public static Drawable stingerDrawable;

    public Stinger(){ }

    @Override
    protected double getSizeConst() { return 0.7; }

    @Override
    public Bitmap getBitmap() { return stingerBitmap; }

    public void setBitmap(Bitmap stingerBitmap) { Stinger.stingerBitmap = stingerBitmap; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.stingerDAO().insert(this);
        return this.id;
    }

    @Override
    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.stingerDAO().update(this);
    }

    @Override
    public Drawable getDrawable() {
        return stingerDrawable;
    }

    public static Stinger generateRandomStinger(long player_id) {
        Stinger randomComponent = new Stinger();
        randomComponent.power = 300 + (int) (Math.random() * 401);
        randomComponent.health = 100 + (int) (Math.random() * 101);
        randomComponent.energy = 5 + (int) (Math.random() * 4);
        randomComponent.player_id = player_id;
        return randomComponent;
    }

    public DrawableComponent generateRandom(long player_id) {
        return Stinger.generateRandomStinger(player_id);
    }
}
