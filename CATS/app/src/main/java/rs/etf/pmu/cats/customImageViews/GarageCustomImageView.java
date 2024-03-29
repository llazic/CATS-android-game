package rs.etf.pmu.cats.customImageViews;


import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;

public class GarageCustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    public GarageCustomImageView(Context context) {
        super(context);
    }

    public GarageCustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GarageCustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Chassis chassis;

    public void updateView(Chassis chassis) {
        this.chassis = chassis;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        if (chassis != null) this.chassis.drawCentrallyWithoutSlots(canvas);
    }
}
