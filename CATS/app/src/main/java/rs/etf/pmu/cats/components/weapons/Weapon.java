package rs.etf.pmu.cats.components.weapons;

import android.graphics.Canvas;

public abstract class Weapon {

    protected int health, energy, power;

    public abstract void draw(Canvas canvas, int x, int y);

    public int getHealth() { return health; }

    public void setHealth(int health) { this.health = health; }

    public int getEnergy() { return energy; }

    public void setEnergy(int energy) { this.energy = energy; }

    public int getPower() { return power; }

    public void setPower(int power) { this.power = power; }
}
