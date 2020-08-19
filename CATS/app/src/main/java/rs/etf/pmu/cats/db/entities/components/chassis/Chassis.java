package rs.etf.pmu.cats.db.entities.components.chassis;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.WheelsSlot;
import rs.etf.pmu.cats.db.entities.components.wheels.Wheels;

@Entity(tableName = "chassis_table")
public class Chassis implements DrawableComponent {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    public long player_id;

    @NonNull
    public int health;

    @NonNull
    public int energy;

    @NonNull
    public boolean inUse;

    @Ignore
    public List<Slot> slots;

    public long insert(CatsRoomDatabase catsRoomDatabase) {
        return -1;
    }

    public void update(CatsRoomDatabase catsRoomDatabase) {
    }

    public Bitmap getBitmap() {
        return null;
    }

    public Drawable getDrawable() {
        return null;
    }

    public void draw(Canvas canvas, Resources resources, int x, int y, boolean drawSlots) {
    }

    protected double getSizeConst() {
        return -1;
    }

    @Override
    public void drawCentrally(Canvas canvas) {
        drawCentrally(canvas, true);
    }

    public void drawCentrallyWithoutSlots(Canvas canvas) {
        drawCentrally(canvas, false);
    }

    public void drawCentrally(Canvas canvas, boolean withSlots) {
        double ratio = canvas.getHeight() * getSizeConst() / getBitmap().getHeight();
        int newWidth = (int) (getBitmap().getWidth() * ratio);
        int newHeight = (int) (getBitmap().getHeight() * ratio);

        Bitmap resized = Bitmap.createScaledBitmap(getBitmap(), newWidth, newHeight, true);

        int vehicleX = (int) (canvas.getWidth() * 0.5 - newWidth * 0.5);
        int vehicleY = (int) (canvas.getHeight() * 0.5 - newHeight * 0.5);

        canvas.drawBitmap(resized, vehicleX, vehicleY, null);

        if (withSlots) for (Slot s : this.slots) s.draw(canvas, vehicleX, vehicleY, ratio);
        else for (Slot s : this.slots) s.drawWithoutSlot(canvas, vehicleX, vehicleY, ratio);
    }

    @Override
    public boolean isInUse() {
        return inUse;
    }

    public boolean isSlotPoint(float x, float y) {
        for (Slot s : slots) {
            if (s.contains(x, y)) return true;
        }
        return false;
    }

    public void removeFromUse() {
        this.inUse = false;
        for (Slot s : slots) {
            s.removeFromUse();
        }
    }

    public boolean canSetNewComponent(DrawableComponent drawableComponent, float x, float y) {
        for (Slot s : slots) {
            if (s.canSetNewComponent(drawableComponent, x, y)) return true;
        }
        return false;
    }

    public DrawableComponent setNewComponentAndReturnOld(DrawableComponent drawableComponent, float x, float y) {
        for (Slot s : slots) {
            if (s.canSetNewComponent(drawableComponent, x, y)) {
                return s.setNewComponentAndReturnOld(drawableComponent, x, y);
            }
        }
        return null;
    }

    public DrawableComponent getSelectedComponentAndRemoveItFromUse(float x, float y) {
        for (Slot s : slots) {
            if (s.contains(x, y)) return s.getSelectedComponentAndRemoveItFromUse(x, y);
        }
        return null;
    }

    public void handleTouchDuringFight(float x, float y) {
        for (Slot s : slots) {
            if (s.contains(x, y)) {
                if (s instanceof WheelsSlot) shouldMove = !shouldMove;
                else s.handleTouchDuringFight();
            }
        }
    }

    @Override
    public int getEnergy() {
        return energy;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public int getPower() {
        return 0;
    }

    @Ignore
    private double fightRatio;
    @Ignore
    private int fightWidth, fightHeight, fightVehicleX, fightVehicleY;
    @Ignore
    private Bitmap fightBitmap;
    @Ignore
    public boolean hasWheels;
    @Ignore
    public int hitPointX, excavatorHitPoint;
    @Ignore
    private float[][] hitPoints;
    @Ignore
    private double[] widthConsts = {0.6, 0.7, 0.8, 0.88, 0.97, 0.97, 0.95, 0.9, 0.85, 0.8};
    @Ignore
    private double[] widthConstsReversed = {0.4, 0.3, 0.2, 0.12, 0.03, 0.03, 0.05, 0.1, 0.15, 0.2};
    @Ignore
    public boolean shouldMove = true;

    public void drawForFight(Canvas canvas, int x, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(canvas, false);
        }

        int fightVehicleXNow = fightVehicleX + (hasWheels ? x : 0);
        for (int i = 0; i < hitPoints.length; i++)
            hitPoints[i][0] = (float) (fightVehicleXNow + fightBitmap.getWidth() * widthConsts[i]);

        hitPointX = fightVehicleXNow + fightWidth - (int) (0.005 * canvas.getWidth());
        excavatorHitPoint = fightVehicleXNow;

        canvas.drawBitmap(fightBitmap, fightVehicleXNow, fightVehicleY, null);
        for (Slot s : this.slots) {
            s.drawForFight(canvas, fightVehicleXNow, fightVehicleY, fightRatio, firstTime);
        }
    }

    public void drawReverse(Canvas canvas, int x, boolean firstTime) {
        if (firstTime) {
            firstTimeCalculations(canvas, true);
        }

        int fightVehicleXNow = fightVehicleX - (hasWheels ? x : 0);
        for (int i = 0; i < hitPoints.length; i++)
            hitPoints[i][0] = (float) (fightVehicleXNow - fightWidth + fightBitmap.getWidth() * widthConstsReversed[i]);

        hitPointX = fightVehicleXNow - fightWidth + (int) (0.005 * canvas.getWidth());
        excavatorHitPoint = fightVehicleXNow;

        Matrix matrix = new Matrix();
        matrix.postScale(-1, 1);
        matrix.postTranslate(fightVehicleXNow, fightVehicleY);

        canvas.drawBitmap(fightBitmap, matrix, null);
        for (Slot s : this.slots)
            s.drawReverse(canvas, fightVehicleXNow - fightWidth, fightVehicleY, fightRatio, firstTime);
    }

    public boolean hits(float x, float y, boolean reverse) {
        if (hitPoints == null) return false;
        float last = fightVehicleY;
        for (int i = 0; i < hitPoints.length; i++) {
            if (y >= last && y <= hitPoints[i][1] &&
                    ((!reverse && x <= hitPoints[i][0]) || (reverse && x >= hitPoints[i][0])))
                return true;
            last = hitPoints[i][1];
        }
        return false;
    }

    private void firstTimeCalculations(Canvas canvas, boolean reverse) {
        double ratio = canvas.getHeight() * getSizeConst() * 0.5 / getBitmap().getHeight();
        int newWidth = (int) (getBitmap().getWidth() * ratio);
        int newHeight = (int) (getBitmap().getHeight() * ratio);

        Bitmap resized = Bitmap.createScaledBitmap(getBitmap(), newWidth, newHeight, true);

        int vehicleX = 0;
        if (reverse) vehicleX = (int) (canvas.getWidth() * 0.7 + newWidth);
        else vehicleX = (int) (canvas.getWidth() * 0.3 - newWidth);
        int vehicleY = (int) (canvas.getHeight() - newHeight);

        for (Slot s : this.slots) {
            if (s instanceof WheelsSlot) {
                if (s.isOccupied()) {
                    hasWheels = true;
                    Wheels w = ((WheelsSlot) s).getWheels();
                    int wheelsHeight = (int) (w.getBitmap().getHeight() * w.getSizeConst() * ratio);
                    vehicleY = (int) (vehicleY - wheelsHeight * 0.35);
                } else {
                    hasWheels = false;
                }
                break;
            }
        }
        fightRatio = ratio;
        fightWidth = newWidth;
        fightHeight = newHeight;
        fightVehicleX = vehicleX;
        fightVehicleY = vehicleY;
        fightBitmap = resized;

        int numberOfLines = 10;
        hitPoints = new float[numberOfLines][2];
        double heightConst = fightBitmap.getHeight() * 1. / numberOfLines;
        for (int i = 0; i < numberOfLines; i++) {
            hitPoints[i][1] = (float) ((i + 1) * heightConst + fightVehicleY);
        }
    }

    public DrawableComponent generateRandom(long player_id) {
        return null;
    }
}
