package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import applicationsystem.ApplicationPage;
import applicationsystem.LoginService;
import exceptions.AccountAlreadyExistException;
import users.Client;

public class SignUpPage extends AppCompatActivity {

    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_page);
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra("@string/login_service");
        loginService = (LoginService) obj;
    }

    public void createUser(View view){
        String email = ((EditText) findViewById(R.id.email_address)).getText().toString();
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,getString(R.string.error_invalid_email),Toast.LENGTH_SHORT).show();
            return;
        }
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (email.matches(getString(R.string.empty_str))) {
            displayExceptionMessage(getString(R.string.no_input) + getString(R.string.email_address));
        } else if (password.matches(getString(R.string.empty_str))) {
            displayExceptionMessage(getString(R.string.no_input) + getString(R.string.password));
        } else {
            try {
                loginService.createClientAccount(email, password);
                ApplicationPage applicationPage = loginService.launchApplicationPage();
                Intent intent = new Intent(this, EditPersonalInfo.class);
                intent.putExtra("@string/application_page", applicationPage);
                intent.putExtra("@string/email_address", email);
                startActivity(intent);
            } catch (AccountAlreadyExistException ex) {
                displayExceptionMessage(ex.getMessage());
            }
        }
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
