package android.task.noteapplication.screens.splashscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.task.noteapplication.R;
import android.task.noteapplication.model.services.UserSharedPerferences;
import android.task.noteapplication.screens.homescreen.HomeActivity;
import android.task.noteapplication.screens.loginscreen.LoginActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "ITS SPLASH";
    private ImageView splashImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        //show gif image in imageView
        Glide.with(this).asGif().load(R.drawable.note_splash).into(splashImageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Log.w(TAG, "Not Logged In");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
            }
        },3800);
    }
    private void initComponents(){
        splashImageView = findViewById(R.id.splashImageView);
    }

}
