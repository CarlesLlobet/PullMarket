package xyz.carlesllobet.pullmarket.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import xyz.carlesllobet.pushmarket.Domain.PlayGifView;
import xyz.carlesllobet.pushmarket.R;

/**
 * Created by CarlesLlobet on 10/03/2016.
 */
public class GifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gif);

        PlayGifView pGif = (PlayGifView) findViewById(R.id.viewGif);
        pGif.setImageResource(R.drawable.pushmarket_gif);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        }, 1350);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
}