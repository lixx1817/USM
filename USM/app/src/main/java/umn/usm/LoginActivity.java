package umn.usm;

import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;
import java.net.*;
import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;

import org.json.JSONObject;


public class LoginActivity extends ActionBarActivity {
    Button btLogin;
    Button btRegister;
    Button passReset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;

    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_USERNAME = "uname";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inputEmail=(EditText)findViewById(R.id.email);
        inputPassword=(EditText)findViewById(R.id.pword);
        btRegister=(Button)findViewById(R.id.registerbtn);
        btLogin=(Button)findViewById(R.id.login);
        passReset=(Button)findViewById(R.id.passres);
        loginErrorMsg=(TextView)findViewById(R.id.loginErrorMsg);

        passReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent= new Intent(v.getContext(),PasswordReset.class); //starts the password reset activity
                startActivityForResult(myIntent,0);
                finish(); //call onDestroy();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent=new Intent(v.getContext(),Register.class);
                startActivityForResult(myIntent,0);
                finish();
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener(){
            @Override
        public void onClick(View v){
                if(!inputEmail.getText().toString().equals("") && !inputPassword.getText().toString().equals("")) {

                }
                else if(inputEmail.getText().toString().equals("")) Toast.makeText(getApplicationContext(),R.string.inputEmail_empty,Toast.LENGTH_SHORT).show();
                else if(inputPassword.getText().toString().equals("")) Toast.makeText(getApplicationContext(),R.string.inputPassword_empty, Toast.LENGTH_SHORT).show();
            }
        });
    }
    //Async task that checks internet connection
    private class NetCheck extends AsyncTask
    {
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            nDialog=new ProgressDialog(LoginActivity.this);
            nDialog.setTitle("Checking Internet Connection");
            nDialog.setMessage("Loading...");
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params){
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo netInfo=cm.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()) {
                try {
                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    urlc.setConnectTimeout(3000);
                    urlc.connect();
                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                new ProcessLogin().execute();
            }
            else{
                nDialog.dismiss();
                loginErrorMsg.setText("Error in Network Connection");
            }
        }


    }
    private class ProcessLogin extends AsyncTask{
        private ProgressDialog pDialog;
        String email,password;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            inputEmail=(EditText) findViewById(R.id.email);
            inputPassword=(EditText)findViewById(R.id.pword);
            email=inputEmail.getText().toString();
            pDialog=new ProgressDialog(Login.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.loginUser(email, password);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if (json.getString(KEY_SUCCESS) != null) {
                    String res = json.getString(KEY_SUCCESS);
                    if(Integer.parseInt(res) == 1){
                        pDialog.setMessage("Loading User Space");
                        pDialog.setTitle("Getting Data");
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                        /**
                         * Clear all previous data in SQlite database.
                         **/
                        UserFunctions logout = new UserFunctions();
                        logout.logoutUser(getApplicationContext());
                        db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                        /**
                         *If JSON array details are stored in SQlite it launches the User Panel.
                         **/
                        Intent upanel = new Intent(getApplicationContext(), Main.class);
                        upanel.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        startActivity(upanel);
                        /**
                         * Close Login Screen
                         **/
                        finish();
                    }else{
                        pDialog.dismiss();
                        loginErrorMsg.setText("Incorrect username/password");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    public void NetAsync(View view){
        new NetCheck().execute();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
