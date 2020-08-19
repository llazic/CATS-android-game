package rs.etf.pmu.cats.db.entities.components.weapons;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;

@Entity(tableName = "chainsaw_table")
public class Chainsaw extends Weapon {

    public static Bitmap chainsawBitmap;
    public static Drawable chainsawDrawable;

    public Chainsaw(){ }

    @Override
    protected double getSizeConst() { return 0.7; }

    @Override
    public Bitmap getBitmap() { return chainsawBitmap; }

    public void setBitmap(Bitmap chainsawBitmap) { Chainsaw.chainsawBitmap = chainsawBitmap; }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.chainsawDAO().insert(this);
        return this.id;
    }

    @Override
    public void update(CatsRoomDatabase catsRoomDatabase) {
        catsRoomDatabase.chainsawDAO().update(this);
    }

    @Override
    public Drawable getDrawable() {
        return chainsawDrawable;
    }

    public static Chainsaw generateRandomChainsaw(long player_id) {
        Chainsaw randomComponent = new Chainsaw();
        randomComponent.power = 800 + (int) (Math.random() * 401);
        randomComponent.health = 100 + (int) (Math.random() * 101);
        randomComponent.energy = 5 + (int) (Math.random() * 4);
        randomComponent.player_id = player_id;
        return randomComponent;
    }

    public DrawableComponent generateRandom(long player_id) {
        return Chainsaw.generateRandomChainsaw(player_id);
    }
}
