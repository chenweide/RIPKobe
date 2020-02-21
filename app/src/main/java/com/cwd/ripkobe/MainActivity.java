package com.cwd.ripkobe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.cwd.ripkobe.game.ShootGame;
import com.cwd.ripkobe.game.listener.OnShootGameListener;

public class MainActivity extends AppCompatActivity {

    private SoundPool soundPool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShootGame shootGame = new ShootGame(this);
        setContentView(shootGame);
        shootGame.setOnShootGameListener(new OnShootGameListener() {
            @Override
            public void onGoal() {
                playGoalAudio();
            }

            @Override
            public void onClick() {
                playClickAudio();
            }
        });
        initSoundPool();
    }

    private void initSoundPool(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(5);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }
    }

    private void playGoalAudio(){
        playAudio(R.raw.goal);
    }

    private void playClickAudio(){
        playAudio(R.raw.xiuxiu);
    }

    private void playAudio(int res){
        final int audioId = soundPool.load(this, res, 1);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPool.play(audioId,1,1,1,0,1);
            }
        });
    }
}
