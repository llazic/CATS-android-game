package rs.etf.pmu.cats.components.slots;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.components.weapons.Weapon;

public class WeaponSlot extends Slot {

    private Weapon weapon;

    public WeaponSlot(int x, int y){
        super(x, y);
    }

    @Override
    public void draw(Canvas canvas, Resources resources, int vehicleX, int vehicleY, double ratio) {
        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.weapon_slot);

        double sizeRatio = ratio * 0.4;
        int newWidth = (int) (b.getWidth() * sizeRatio);
        int newHeight = (int) (b.getHeight() * sizeRatio);

        int newX = (int) (ratio * x);
        int newY = (int) (ratio * y);

        Bitmap resized = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);
        canvas.drawBitmap(resized, (float)(vehicleX + newX - newWidth * 0.5), (float)(vehicleY + newY - newHeight * 0.5), null);
    }

    @Override
    public int getWidth() { return 20; }

    @Override
    public int getHeight() { return 20; }

    @Override
    public boolean isOccupied() { return weapon != null; }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }
}
