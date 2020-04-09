package rs.etf.pmu.cats.components.slots;

import android.content.res.Resources;
import android.graphics.Canvas;

import rs.etf.pmu.cats.components.wheels.Wheels;

public class WheelsSlot extends Slot{

    private Wheels wheels;

    public WheelsSlot(int x, int y){
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas, Resources resources, int vehicleX, int vehicleY, double ratio) {

    }

    @Override
    public int getWidth() { return 40; }

    @Override
    public int getHeight() { return 40; }

    @Override
    public boolean isOccupied() { return wheels != null; }

    public Wheels getWheels() { return wheels; }

    public void setWheels(Wheels wheels) { this.wheels = wheels; }
}
