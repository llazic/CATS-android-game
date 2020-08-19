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
import rs.etf.pmu.cats.db.entities.components.chassis.TankChassis;
import rs.etf.pmu.cats.db.entities.components.utilities.Utility;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;

@Entity(tableName = "weapon_slot_table")
public class WeaponSlot extends Slot {

    @Ignore
    private Weapon weapon;

    public static Bitmap weaponSlotBitmap;

    public WeaponSlot(int x, int y){
        super(x, y);
    }

    @Override
    protected void drawComponent(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio) {
        this.weapon.draw(canvas, vehicleTopLeftX, vehicleTopLeftY, x, y, ratio);
    }

    @Override
    protected void drawComponentForFight(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio, boolean firstTime) {
        this.weapon.drawForFight(canvas, vehicleTopLeftX, vehicleTopLeftY, x, y, ratio, firstTime);
    }

    @Override
    protected void drawComponentReverse(Canvas canvas, int vehicleTopLeftX, int vehicleTopLeftY, double ratio, boolean firstTime) {
        this.weapon.drawReverse(canvas, vehicleTopLeftX, vehicleTopLeftY, TankChassis.tankChassisBitmap.getWidth() - x, y, ratio, firstTime);
    }

    @Override
    protected void drawExtra(Canvas canvas, Bitmap resized, int vehicleTopLeftX, int newX, int newWidth, int vehicleTopLeftY, int newY, int newHeight, double ratio, double density) { /*empty*/ }

    @Override
    protected double getSizeConst() {
        return 0.4;
    }

    @Override
    public int getWidth() { return weaponSlotBitmap.getWidth(); }

    @Override
    public int getHeight() { return weaponSlotBitmap.getHeight(); }

    @Override
    public Bitmap getBitmap() { return weaponSlotBitmap; }

    @Override
    public boolean isOccupied() { return weapon != null; }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override
    public long insert(CatsRoomDatabase catsRoomDatabase) {
        this.id = catsRoomDatabase.weaponSlotDAO().insert(this);
        return this.id;
    }

    @Override
    public Drawable getDrawable() {
        return null;
    }

    @Override
    public void removeFromUse() {
        if (weapon != null) {
            weapon.removeFromUse();
            weapon = null;
        }
    }

    @Override
    public boolean canSetNewComponent(DrawableComponent drawableComponent, float x, float y) {
        return drawableComponent instanceof Weapon && contains(x, y);
    }

    @Override
    public DrawableComponent setNewComponentAndReturnOld(DrawableComponent drawableComponent, float x, float y) {
        if (canSetNewComponent(drawableComponent, x, y)) {
            if (this.weapon != null) {
                weapon.removeFromUse();
            }
            Weapon oldWeapon = weapon;
            this.weapon = (Weapon) drawableComponent;
            this.weapon.slot_id = this.id;
            return oldWeapon;
        }
        return null;
    }

    @Override
    public DrawableComponent getSelectedComponentAndRemoveItFromUse(float x, float y) {
        if (contains(x, y)) {
            Weapon oldWeapon = this.weapon;
            removeFromUse();
            return oldWeapon;
        }
        return null;
    }

    @Override
    public int getHealth() {
        return weapon != null ? weapon.health : 0;
    }

    @Override
    public void handleTouchDuringFight() {
        if (this.weapon != null) this.weapon.handleTouchDuringFight();
    }
}
