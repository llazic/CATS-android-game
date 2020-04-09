package rs.etf.pmu.cats.components.chassis;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import java.util.List;

import rs.etf.pmu.cats.components.slots.Slot;

public abstract class Chassis {
    protected int health, energy;
    protected List<Slot> slots;

    public abstract void draw(Canvas canvas, Resources resources, int x, int y);

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getEnergy() { return energy; }

    public void setEnergy(int energy) { this.energy = energy; }

    public List<Slot> getSlots() { return slots; }

    public void setSlots(List<Slot> slots) { this.slots = slots; }

    public abstract Bitmap getBitmap(Resources resources);
}
