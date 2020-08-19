package rs.etf.pmu.cats.db.entities.components;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;

import rs.etf.pmu.cats.db.CatsRoomDatabase;

public interface DrawableComponent {
    void drawCentrally(Canvas canvas);
    Drawable getDrawable();
    boolean isInUse();
    int getEnergy();
    int getHealth();
    int getPower();
    long insert(CatsRoomDatabase catsRoomDatabase);
    void update(CatsRoomDatabase catsRoomDatabase);
    DrawableComponent generateRandom(long player_id);
}
