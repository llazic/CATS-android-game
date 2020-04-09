package rs.etf.pmu.cats.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table",
        indices = {@Index(value = "nickname", unique = true)})
public class Player {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    private int gamesWon;

    @NonNull
    private int gamesLost;

    @NonNull
    private boolean musicPlaying;

    @NonNull
    private boolean vehicleControl;

    @NonNull
    private String nickname;

    public Player(String nickname){
        this.nickname = nickname;
        this.gamesWon = 0;
        this.gamesLost = 0;
        this.musicPlaying = true;
        this.vehicleControl = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getGamesLost() {
        return gamesLost;
    }

    public void setGamesLost(int gamesLost) {
        this.gamesLost = gamesLost;
    }

    public boolean isMusicPlaying() {
        return musicPlaying;
    }

    public void setMusicPlaying(boolean musicPlaying) {
        this.musicPlaying = musicPlaying;
    }

    public boolean isVehicleControl() {
        return vehicleControl;
    }

    public void setVehicleControl(boolean vehicleControl) {
        this.vehicleControl = vehicleControl;
    }
}
