package rs.etf.pmu.cats.db.entities.components.slots;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import androidx.room.Entity;
import androidx.room.Ignore;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.utilities.Utility;

@Entity(tableName = "utility_slot_table")
public class UtilitySlot extends Slot {

    @Ignore
    private Utility utility;

    public static Bitmap utilitySlotBitmap;
    public static Drawable utilitySlotDrawable;

    public UtilitySlot(int x, int y){
        super(x, y);
    }

    @Override
    protected void drawComponent(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {}

    @Override
    protected void drawComponentForFight(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime) {}

    @Override
    protected void drawComponentReverse(Canvas canvas, int topLeftX, int topLeftY, double ratio, boolean firstTime) {}

    @Override
    protected void drawExtra(Canvas canvas, Bitmap resized, int vehicleTopLeftX, int newX, int newWidth, int vehicleTopLeftY, int newY, int newHeight, double ratio, double density) { /*empty*/ }

    @Override
    protected double getSizeConst() { return 0.4; }

    @Override
    public int getWidth() { return utilitySlotBitmap.getWidth(); }

    @Override
    public int getHeight() { return utilitySlotBitmap.getHeight(); }

    @Override
    public boolean isOccupied() {
        return utility != null;
    }

    @Override
    public Bitmap getBitmap() {
        return utilitySlotBitmap;
    }

    public Utility getUtility() {
        return utility;
    }

    public void setUtility(Utility utility) {
        this.utility = utility;
    }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.utilitySlotDAO().insert(this);
        return this.id;
    }

    @Override
    public Drawable getDrawable() {
        return utilitySlotDrawable;
    }

    @Override
    public void removeFromUse() {
        if (utility != null) {
            utility.removeFromUse();
            utility = null;
        }
    }

    @Override
    public boolean canSetNewComponent(DrawableComponent drawableComponent, float x, float y){
        return drawableComponent instanceof Utility && contains(x, y);
    }

    @Override
    public DrawableComponent setNewComponentAndReturnOld(DrawableComponent drawableComponent, float x, float y){
        if (canSetNewComponent(drawableComponent, x, y)) {
            if (this.utility != null) {
                utility.removeFromUse();
            }
            Utility oldUtility = utility;
            this.utility = (Utility) drawableComponent;
            this.utility.slot_id = this.id;
            return oldUtility;
        }
        return null;
    }

    @Override
    public DrawableComponent getSelectedComponentAndRemoveItFromUse(float x, float y) {
        if (contains(x, y)) {
            Utility oldUtility = this.utility;
            removeFromUse();
            return oldUtility;
        }
        return null;
    }

    @Override
    public int getHealth() {
        return utility != null ? utility.health : 0;
    }

    @Override
    public void handleTouchDuringFight() {}
}
