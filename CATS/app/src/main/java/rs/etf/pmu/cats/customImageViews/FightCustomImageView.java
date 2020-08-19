package rs.etf.pmu.cats.customImageViews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.facebook.stetho.common.ArrayListAccumulator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import rs.etf.pmu.cats.R;
import rs.etf.pmu.cats.db.entities.components.chassis.Chassis;
import rs.etf.pmu.cats.db.entities.components.slots.Slot;
import rs.etf.pmu.cats.db.entities.components.slots.WeaponSlot;
import rs.etf.pmu.cats.db.entities.components.weapons.Blade;
import rs.etf.pmu.cats.db.entities.components.weapons.Rocket;
import rs.etf.pmu.cats.db.entities.components.weapons.Weapon;
import rs.etf.pmu.cats.other.FightAsyncTask;
import rs.etf.pmu.cats.other.FightEndable;
import rs.etf.pmu.cats.other.RandomGenerator;

public class FightCustomImageView extends androidx.appcompat.widget.AppCompatImageView {

    public FightCustomImageView(Context context) {
        super(context);
    }

    public FightCustomImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public FightCustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Chassis playerChassis, computerChassis;
    private int playerHealth, playerFullHealth, computerHealth, computerFullHealth;
    private List<Rocket> rockets;
    private List<Blade> computerBlades, playerBlades;

    private int lastX = 0, lastPlayerX = 0;
    private int x = 0, playerX = 0;
    private long timePassed;
    private int rocketXOffset = 0;
    private boolean shouldRocketLaunch;

    private boolean firstTime = true;
    private float playerHealthXStart, playerHealthXEnd, computerHealthXStart, computerHealthXEnd;
    private float healthYStart, healthYEnd;
    private float healthBarLength;
    private Paint healthPaint;
    private boolean timeRanOut = false;
    private int excavatorX = 0;

    private FightEndable fragment;

    private boolean ended = false, vehicleControl;

    public void setFightEndable(FightEndable fightEndable) {
        this.fragment = fightEndable;
    }

    public void setPlayerChassis(Chassis playerChassis, boolean vehicleControl) {
        this.vehicleControl = vehicleControl;
        this.playerChassis = playerChassis;

        this.playerChassis.shouldMove = !vehicleControl;

        rockets = new ArrayList<>();
        playerBlades = new ArrayList<>();
        computerBlades = new ArrayList<>();

        playerFullHealth += playerChassis.health;
        for (Slot s : playerChassis.slots) {
            playerFullHealth += s.getHealth();
            if (s instanceof WeaponSlot && ((WeaponSlot) s).getWeapon() != null && ((WeaponSlot) s).getWeapon() instanceof Rocket) {
                rockets.add((Rocket) (((WeaponSlot) s).getWeapon()));
            }
            if (s instanceof WeaponSlot && ((WeaponSlot) s).getWeapon() != null && ((WeaponSlot) s).getWeapon() instanceof Blade) {
                Blade blade = (Blade) (((WeaponSlot) s).getWeapon());
                blade.shouldRotate = !vehicleControl;
                blade.rotationAngle = 0;
                playerBlades.add(blade);
            }
        }
        playerHealth = playerFullHealth;

        computerChassis = RandomGenerator.generateOpponent();
        computerFullHealth += computerChassis.health;
        for (Slot s : computerChassis.slots) {
            computerFullHealth += s.getHealth();

            if (s instanceof WeaponSlot && ((WeaponSlot) s).getWeapon() != null && ((WeaponSlot) s).getWeapon() instanceof Rocket) {
                rockets.add((Rocket) (((WeaponSlot) s).getWeapon()));
            }
            if (s instanceof WeaponSlot && ((WeaponSlot) s).getWeapon() != null && ((WeaponSlot) s).getWeapon() instanceof Blade) {
                computerBlades.add((Blade) (((WeaponSlot) s).getWeapon()));
            }
        }
        computerHealth = computerFullHealth;

        for (Rocket r : rockets) {
            r.setShouldRocketLaunch(false);
            r.resetRocketOffset();
        }

        //
        System.out.println("Player health: " + playerHealth + ", Computer health = " + computerHealth);
        invalidate();
    }

    public void setShouldRocketLaunch(boolean shouldRocketLaunch) {
        this.shouldRocketLaunch = shouldRocketLaunch;
        for (Rocket r : rockets) {
            r.setShouldRocketLaunch(true);
            r.resetRocketOffset();
        }
    }

    public void resetRocketOffset() {
        rocketXOffset = 0;
    }

    public void incX() {
        for (Rocket r : rockets) {
            if (r.shouldRocketLaunch()) r.increaseRocketOffset(40);
        }
        for (Blade b : computerBlades) {
            b.rotationAngle = (b.rotationAngle + 360 - 5) % 360;
        }
        for (Blade b : playerBlades) {
            if (b.shouldRotate) b.rotationAngle = (b.rotationAngle + 360 - 5) % 360;
        }
        lastX = x;
        x += 5;
        if (!vehicleControl || playerChassis.shouldMove) {
            lastPlayerX = playerX;
            playerX += 5;
        }
        invalidate();
    }

    public void incExcavatorX() {
        excavatorX += 10;
    }

    public void setTimeRanOut(boolean timeRanOut) {
        this.timeRanOut = timeRanOut;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (ended) {
            Paint p = new Paint();
            p.setTypeface(getResources().getFont(R.font.game_font));
            p.setTextSize(200);
            p.setColor(computerHealth == 0 ? getResources().getColor(R.color.letterColor, null) : Color.RED);
            p.setTextAlign(Paint.Align.CENTER);
            int xPos = (getWidth() / 2);
            int yPos = (int) ((getHeight() / 2) - ((p.descent() + p.ascent()) / 2)) ;
            canvas.drawText(computerHealth == 0 ? "YOU WON" : "YOU LOST", xPos, yPos, p);
            return;
        }

        if (firstTime) {
            healthYStart = (float) (getHeight() * 0.05);
            healthYEnd = (float) (getHeight() * 0.15);

            playerHealthXStart = (float) (getWidth() * 0.05);
            playerHealthXEnd = (float) (getWidth() * 0.45);

            computerHealthXStart = (float) (getWidth() * 0.55);
            computerHealthXEnd = (float) (getWidth() * 0.95);

            healthBarLength = (float) (getWidth() * 0.4);

            healthPaint = new Paint();
            healthPaint.setColor(Color.RED);
        }

        checkStop(canvas);
        doDamage();
        if (this.timeRanOut) {
            drawExcavators(canvas);
            doExcavatorDamage();
        }
        drawHealthBars(canvas);

        if (computerChassis != null) this.computerChassis.drawReverse(canvas, x, firstTime);
        if (playerChassis != null) this.playerChassis.drawForFight(canvas, playerX, firstTime);

        checkWinner(canvas);

        firstTime = false;
    }

    private void drawExcavators(Canvas canvas) {
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        canvas.drawRect(0, 0, excavatorX, canvas.getHeight(), p);
        canvas.drawRect(canvas.getWidth() - excavatorX, 0, canvas.getWidth(), canvas.getHeight(), p);
    }

    private void checkWinner(Canvas canvas) {
        if (computerHealth == 0 || playerHealth == 0) {
            fragment.fightEnded(computerHealth == 0);
            ended = true;
            invalidate();
        }
    }

    private void doExcavatorDamage() {
        boolean playerDead = false, computerDead = false;
        if (playerChassis.excavatorHitPoint <= excavatorX) playerDead = true;
        if (computerChassis.excavatorHitPoint >= getWidth() - excavatorX) computerDead = true;

        if (playerDead && computerDead) {
            if (playerHealth >= computerHealth) computerHealth = 0;
            else playerHealth = 0;
        } else if (playerDead) {
            playerHealth = 0;
        } else if (computerDead) {
            computerHealth = 0;
        }
    }

    private void drawHealthBars(Canvas canvas) {
        playerHealthXEnd = playerHealthXStart + healthBarLength * playerHealth / playerFullHealth;
        computerHealthXStart = computerHealthXEnd - healthBarLength * computerHealth / computerFullHealth;
        canvas.drawRect(playerHealthXStart, healthYStart, playerHealthXEnd, healthYEnd, healthPaint);
        canvas.drawRect(computerHealthXStart, healthYStart, computerHealthXEnd, healthYEnd, healthPaint);
    }

    private void doDamage() {
        //player
        for (Slot s : playerChassis.slots) {
            if (s instanceof WeaponSlot) {
                Weapon w = ((WeaponSlot) s).getWeapon();
                if (w != null && computerChassis.hits(w.hitPointX, w.hitPointY, true)) {
                    if (w instanceof Rocket) {
                        Rocket r = (Rocket) w;
                        //there is a case where rocket isnt launched and its hitPointX is hitting the enemy
                        if (r.shouldRocketLaunch()) computerHealth -= w.power * 1. * FightAsyncTask.ROCKET_INTERVAL_MS / 1000.;
                        r.setShouldRocketLaunch(false);
                        r.resetRocketOffset();
                    } else {
                        computerHealth -= w.power * 1. * FightAsyncTask.REFRESH_TIME_MS / 1000.;
                    }
                }
            }
        }
        if (computerHealth < 0) computerHealth = 0;

        //computer
        for (Slot s : computerChassis.slots) {
            if (s instanceof WeaponSlot) {
                Weapon w = ((WeaponSlot) s).getWeapon();
                if (w != null && playerChassis.hits(w.hitPointX, w.hitPointY, false)) {
                    if (w instanceof Rocket) {
                        Rocket r = (Rocket) w;
                        if (r.shouldRocketLaunch()) playerHealth -= w.power * 1. * FightAsyncTask.ROCKET_INTERVAL_MS / 1000.;
                        r.setShouldRocketLaunch(false);
                        r.resetRocketOffset();
                    } else {
                        playerHealth -= w.power * 1. * FightAsyncTask.REFRESH_TIME_MS / 1000.;
                    }
                }
            }
        }
        if (playerHealth < 0) playerHealth = 0;
    }

    private void checkStop(Canvas canvas) {
        if ((playerChassis.hasWheels != computerChassis.hasWheels && playerChassis.hitPointX - 0.01 * canvas.getWidth() >= computerChassis.hitPointX) ||
                (playerChassis.hasWheels == computerChassis.hasWheels && playerChassis.hitPointX >= computerChassis.hitPointX)) {
            x = lastX;
            playerX = lastPlayerX;
        }
    }
}
