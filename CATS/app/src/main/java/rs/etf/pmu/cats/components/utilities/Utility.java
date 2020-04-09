package rs.etf.pmu.cats.components.utilities;

import android.graphics.Canvas;

public abstract class Utility {

    protected int health, energy;

    public abstract void draw(Canvas canvas, int x, int y);

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getEnergy() { return energy; }

    public void setEnergy(int energy) { this.energy = energy; }
}
