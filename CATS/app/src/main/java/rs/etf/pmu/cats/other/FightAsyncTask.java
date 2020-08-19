package rs.etf.pmu.cats.other;

import android.os.AsyncTask;

import rs.etf.pmu.cats.customImageViews.FightCustomImageView;

public class FightAsyncTask extends AsyncTask<Void, Integer, Void> {

    private FightCustomImageView fightCustomImageView;

    public FightAsyncTask(FightCustomImageView fightCustomImageView) { this.fightCustomImageView = fightCustomImageView; }

    private static final long MAX_GAME_DURATION_MS = 15000;
    public static final long REFRESH_TIME_MS = 30;
    public static final long ROCKET_INTERVAL_MS = 1500;
    private boolean running = true;

    public void stop() {
        running = false;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        long newTime, nextTime, beginTime, endTime;

        beginTime = System.currentTimeMillis();
        endTime = beginTime + MAX_GAME_DURATION_MS;
        nextTime = beginTime + REFRESH_TIME_MS;
        while (running){
            newTime = System.currentTimeMillis();

            if (newTime >= nextTime) {
                //fightInfo.msPassed += timePassed;
                if (newTime > endTime)  {
                    fightCustomImageView.setTimeRanOut(true);
                    fightCustomImageView.incExcavatorX();
                }
                if ((newTime - beginTime) % ROCKET_INTERVAL_MS == 0 && newTime != beginTime){
                    fightCustomImageView.resetRocketOffset();
                    fightCustomImageView.setShouldRocketLaunch(true);
                }
                fightCustomImageView.incX();
                nextTime += REFRESH_TIME_MS;
            }
        }
        return null;
    }
}