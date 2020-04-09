package rs.etf.pmu.cats.components.chassis;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.LinkedList;

import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.components.slots.Slot;
import rs.etf.pmu.cats.components.slots.WeaponSlot;

public class TankChassis extends Chassis{
    //da li je dovoljno dobra raspodela?
    //dodati random broj slotova
    //dodati da nema preklapanja ni medju slotovima
    public TankChassis(Resources resources){
        this.slots = new LinkedList<>();

        Bitmap chassisBitmap = BitmapFactory.decodeResource(resources, R.drawable.tank_chassis);
        Bitmap slotBitmap = BitmapFactory.decodeResource(resources, R.drawable.weapon_slot);

        int slotHalfWidth = (int)(slotBitmap.getWidth() * 0.5);
        int slotHalfHeight = (int)(slotBitmap.getHeight() * 0.5);

        int x, y;
        for (int i = 0; i < 100; i++) {
            do {
                x = (int) (Math.random() * chassisBitmap.getWidth());
                y = (int) (Math.random() * chassisBitmap.getHeight());

                try {
                    int pixel = chassisBitmap.getPixel(x - slotHalfWidth, y - slotHalfHeight);
                    if (pixel == Color.TRANSPARENT || pixel == Color.BLACK) continue;
                    pixel = chassisBitmap.getPixel(x - slotHalfWidth, y + slotHalfHeight);
                    if (pixel == Color.TRANSPARENT || pixel == Color.BLACK) continue;
                    pixel = chassisBitmap.getPixel(x + slotHalfWidth, y - slotHalfHeight);
                    if (pixel == Color.TRANSPARENT || pixel == Color.BLACK) continue;
                    pixel = chassisBitmap.getPixel(x + slotHalfWidth, y + slotHalfHeight);
                    if (pixel == Color.TRANSPARENT || pixel == Color.BLACK) continue;

                    break;
                } catch (java.lang.IllegalArgumentException e) {}
            } while (true);

            slots.add(new WeaponSlot(x, y));

            System.out.println("x = " + x + ", y = " + y);
        }
//        for (int i = 0; i < coordinates.length; i++)
//            this.slots.add(new WeaponSlot(coordinates[i][0], coordinates[i][1]));
    }

    @Override
    public void draw(Canvas canvas, Resources resources, int x, int y) {
        Paint paint = new Paint();
        paint.setColor(Color.BLUE);
        Bitmap b = BitmapFactory.decodeResource(resources, R.drawable.tank_chassis);

        double ratio = canvas.getHeight() * 1.0 / b.getHeight();
        int newWidth = (int) (b.getWidth() * ratio);
        int newHeight = (int) (b.getHeight() * ratio);

        Bitmap resized = Bitmap.createScaledBitmap(b, newWidth, newHeight, true);

        int vehicleX = (int)(canvas.getWidth() * 0.5 - newWidth * 0.5);
        int vehicleY = (int)(canvas.getHeight() * 0.5 - newHeight * 0.5);

        canvas.drawBitmap(resized, vehicleX, vehicleY, null);

        for (Slot s : this.slots){
            s.draw(canvas, resources, vehicleX, vehicleY, ratio);
        }
    }

    @Override
    public Bitmap getBitmap(Resources resources) {
        return BitmapFactory.decodeResource(resources, R.drawable.tank_chassis);
    }
}
