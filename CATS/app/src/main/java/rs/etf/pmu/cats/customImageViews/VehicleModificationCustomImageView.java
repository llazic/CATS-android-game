package rs.etf.pmu.cats.customImageViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;

public class VehicleModificationCustomImageView extends AppCompatImageView {

    public VehicleModificationCustomImageView(Context context) {
        super(context);
    }

    public VehicleModificationCustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VehicleModificationCustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Chassis chassis;

    public void updateView(Chassis chassis) {
        this.chassis = chassis;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (chassis != null) this.chassis.drawCentrally(canvas);
    }
}
