package rs.etf.pmu.cats.db.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "player_table",
        indices = {@Index(value = "nickname", unique = true)})
public class Player {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public int gamesWon;

    public int gamesLost;

    public boolean musicPlaying;

    public boolean vehicleControl;

    @NonNull
    public String nickname;

    //public Long[] boxTimes = {null, null, null, null};
    public Long box0Time, box1Time, box2Time, box3Time;

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
