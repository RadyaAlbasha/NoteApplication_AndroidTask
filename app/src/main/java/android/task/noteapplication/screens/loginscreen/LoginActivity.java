package android.task.noteapplication.screens.loginscreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.task.noteapplication.R;
import android.task.noteapplication.model.services.UserSharedPerferences;
import android.task.noteapplication.screens.homescreen.HomeActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "android.task.noteapplication.screens.loginscreen.EXTRA_ID";
    public static final String EXTRA_NAME = "android.task.noteapplication.screens.loginscreen.EXTRA_NAME";
    public static final String EXTRA_IMG = "android.task.noteapplication.screens.loginscreen.EXTRA_IMG";
    public static final int LOGIN_REQUEST = 3;

    private static final String TAG = "";
    LoginButton loginFB;
    MaterialRippleLayout fbRibble;
    private CallbackManager mCallbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initComponents();

        initFB_Btn();
    }

    private void initComponents(){
        //my custom Facebook button
        fbRibble = findViewById(R.id.fbRipple);

        //The original Facebook button
        loginFB = findViewById(R.id.buttonFacebookLogin);
    }

    private void initFB_Btn(){

        //convert the click on my custom button to a click on the facecbook button
        fbRibble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // loginFB.performClick();
                if(checkNetworkConnectionStatuse()){
                    loginFB.performClick();
                }else{
                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle(R.string.no_internet_title);
                    alert.setMessage(R.string.no_internet_msg);
                    alert.setPositiveButton(R.string.ok,null);
                    alert.show();
                }


            }
        });

        // Initialize Facebook Login button

        mCallbackManager = CallbackManager.Factory.create();

        loginFB.setReadPermissions(Arrays.asList("email", "public_profile"));
        checkLoginStatus();
        loginFB.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                //sing in fb
                // App code
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // App code
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // App code
            }
        });
    }

    private boolean checkNetworkConnectionStatuse(){
        boolean internetConnected;
        //boolean wifiConnected;
        //boolean mobileConnected;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeInfo = connectivityManager.getActiveNetworkInfo();
        if(activeInfo != null && activeInfo.isConnected()){//connected with either mobile or wifi
          /*  wifiConnected = activeInfo.getType() == ConnectivityManager.TYPE_WIFI;
            mobileConnected = activeInfo.getType() == ConnectivityManager.TYPE_MOBILE;

            if(wifiConnected){//wifi connected

            }else if(mobileConnected){//mobile connected

            }*/
            return true;

        }else{// no internet connection
            /*AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
            alert.setTitle("No Internet Connection");
            alert.setMessage("Make sure that Wi-Fi or mobile data turned on, then try again.");
            alert.setPositiveButton("OK",null);
            alert.show();*/
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            if(currentAccessToken == null){
                Toast.makeText(LoginActivity.this, R.string.user_logged_out, Toast.LENGTH_SHORT).show();
            }else {
                loadUserProfile(currentAccessToken);
            }
        }
    };

    private void loadUserProfile(AccessToken newAccessToken){
        GraphRequest request = GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                try {
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");
                    String email = object.getString("email");
                    String id = object.getString("id");
                    saveUid(id);
                    String img_url = "https://graph.facebook.com/"+id+"/picture?type=normal";

                    updateUI(first_name,last_name,img_url, id);
                    // can put in view components here
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void checkLoginStatus(){
        if(AccessToken.getCurrentAccessToken() != null)
        {
            if(checkNetworkConnectionStatuse()){//true
                loadUserProfile(AccessToken.getCurrentAccessToken());
            }else{
                Toast.makeText(this, R.string.no_internet_title, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
                LoginActivity.this.startActivityForResult(intent,LOGIN_REQUEST);
            }

        }
    }
    public void updateUI(String first_name,String last_name,String img_url, String id) {
        String full_name = first_name+" "+last_name;
        Intent intent = new Intent(LoginActivity.this , HomeActivity.class);
        intent.putExtra(EXTRA_NAME,full_name);
        intent.putExtra(EXTRA_IMG,img_url);
        intent.putExtra(EXTRA_ID,id);
        LoginActivity.this.startActivityForResult(intent,LOGIN_REQUEST);
    }

    public void saveUid(String uid){
        String FILENAME = "uidfile.txt";
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(uid.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
