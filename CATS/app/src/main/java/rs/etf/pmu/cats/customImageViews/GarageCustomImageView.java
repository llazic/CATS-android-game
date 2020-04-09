package rs.etf.pmu.cats.customImageViews;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import rs.etf.pmu.cats.components.chassis.TankChassis;

public class GarageCustomImageView extends ImageView {

    public GarageCustomImageView(Context context) {
        super(context);
    }

    public GarageCustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GarageCustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        Paint canvasPaint = new Paint();
//        canvasPaint.setColor(Color.TRANSPARENT);
//        canvas.drawRect(0, 0, getWidth(), getHeight(), canvasPaint);

        //getResources();

        TankChassis rectangleChassis = new TankChassis(getResources());
        rectangleChassis.draw(canvas, getResources(), 200, getHeight() - 150);

        //if (canvas.get)
    }
}
