package rs.etf.pmu.cats.components.slots;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import rs.etf.pmu.cats.MainActivity;
import rs.etf.pmu.cats.R;

public abstract class Slot {

    //coordinates from vehicle starting point
    protected int x, y;

    public Slot(int x, int y){
        this.x = x;
        this.y = y;
    }

    //vehicle starting point
    public abstract void draw(Canvas canvas, Resources resources, int vehicleX, int vehicleY, double ratio);

    public abstract int getWidth();

    public abstract int getHeight();

    public abstract boolean isOccupied();
}
