package umn.usm;

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


public class LoginActivity extends ActionBarActivity {
    Button btLogin;
    Button btRegister;
    Button passReset;
    EditText inputEmail;
    EditText inputPassword;
    private TextView loginErrorMsg;
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
