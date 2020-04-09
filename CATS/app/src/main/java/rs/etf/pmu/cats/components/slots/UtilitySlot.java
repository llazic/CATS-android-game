package rs.etf.pmu.cats.components.slots;

import android.content.res.Resources;
import android.graphics.Canvas;

import rs.etf.pmu.cats.components.utilities.Utility;

public class UtilitySlot extends Slot {

    private Utility utility;

    public UtilitySlot(int x, int y){
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas, Resources resources, int vehicleX, int vehicleY, double ratio) {

    }

    @Override
    public int getWidth() { return 10; }

    @Override
    public int getHeight() { return 10; }

    @Override
    public boolean isOccupied() {
        return utility != null;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }
}
