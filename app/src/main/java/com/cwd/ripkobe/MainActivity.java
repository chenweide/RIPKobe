package com.cwd.ripkobe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.cwd.ripkobe.game.ShootGame;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShootGame shootGame = new ShootGame(this);
        setContentView(shootGame);
    }
}
