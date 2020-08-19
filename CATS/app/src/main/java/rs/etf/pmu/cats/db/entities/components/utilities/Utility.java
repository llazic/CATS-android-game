package rs.etf.pmu.cats.db.entities.components.utilities;

import android.graphics.Canvas;
import android.graphics.Matrix;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;

@Entity(tableName = "utility_table")
public abstract class Utility implements DrawableComponent {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long player_id;

    public Long slot_id = null;

    @NonNull
    public int health;

    @NonNull
    public int energy;

    public abstract void draw(Canvas canvas, int x, int y);

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getEnergy() { return energy; }

    public void setEnergy(int energy) { this.energy = energy; }

    public abstract long insert(CatsRoomDatabase catsRoomDatabase);

    public abstract void update(CatsRoomDatabase catsRoomDatabase);

    @Override
    public boolean isInUse() {
        return slot_id != null;
    }

    public void removeFromUse(){
        slot_id = null;
    }

    @Override
    public int getPower() {
        return 0;
    }

    public abstract void drawForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio, boolean firstTime);
}
