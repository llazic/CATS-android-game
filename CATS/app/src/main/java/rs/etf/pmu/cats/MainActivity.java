package rs.etf.pmu.cats;

import androidx.appcompat.app.AppCompatActivity;
import rs.etf.pmu.cats.db.CatsRoomDatabase;
import rs.etf.pmu.cats.db.entities.Player;

import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private CatsRoomDatabase catsRoomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFullscreenMode();
        //insertIntoDB();
    }

    private void setFullscreenMode(){
        //hide navigation and status bar
        final View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void insertIntoDB(){
        this.catsRoomDatabase = CatsRoomDatabase.getDatabase(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                catsRoomDatabase.playerDAO().insert(new Player("qweqwe"));
                catsRoomDatabase.playerDAO().insert(new Player("Paja1231sn"));
                catsRoomDatabase.playerDAO().insert(new Player("Tupq12312wn"));
            }
        }).start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        setFullscreenMode();
    }
}
