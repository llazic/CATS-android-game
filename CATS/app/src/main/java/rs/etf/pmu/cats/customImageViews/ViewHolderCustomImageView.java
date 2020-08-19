package rs.etf.pmu.cats.customImageViews;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import rs.etf.pmu.cats.db.entities.components.DrawableComponent;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;

public class ViewHolderCustomImageView extends AppCompatImageView {

    public ViewHolderCustomImageView(Context context) {
        super(context);
    }

    public ViewHolderCustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewHolderCustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private DrawableComponent drawableComponent;

    public void updateView(DrawableComponent drawableComponent) {
        this.drawableComponent = drawableComponent;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (drawableComponent != null) this.drawableComponent.drawCentrally(canvas);
    }
}
