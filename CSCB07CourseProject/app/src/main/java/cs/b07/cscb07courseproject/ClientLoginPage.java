package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import applicationsystem.ApplicationPage;
import exceptions.NoSuchClientException;
import applicationsystem.LoginService;
import users.Client;

public class ClientLoginPage extends AppCompatActivity {
    private LoginService loginService;
    private ApplicationPage applicationPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login_page);
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra("@string/login_service");
        loginService = (LoginService) obj;
    }

    public void clientLogin(View view) {
        String email = ((EditText) findViewById(R.id.email_address)).getText().toString();
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,getString(R.string.error_invalid_email),Toast.LENGTH_SHORT).show();
            return;
        }
        String password = ((EditText) findViewById(R.id.password)).getText().toString();
        if (loginService.isExistingClient(email)) {
            try{
                loginService.loadingUserInfo(email, password);
                ApplicationPage app = loginService.launchApplicationPage();
                Intent intent = new Intent(this, ClientHomePage.class);
                intent.putExtra("@string/application_page", app);
                intent.putExtra("@string/email_address", email);
                startActivity(intent);

            } catch(NoSuchClientException nsce) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ClientLoginPage.this);
                alert.setTitle(getString(R.string.unable_to_login));
                alert.setMessage(getString(R.string.user_pw_mismatch));
                alert.setPositiveButton(getString(R.string.ok), null);
                alert.setCancelable(true);
                alert.create().show();
            }
        } else {
            displayExceptionMessage(getString(R.string.no_such_client));
        }
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
