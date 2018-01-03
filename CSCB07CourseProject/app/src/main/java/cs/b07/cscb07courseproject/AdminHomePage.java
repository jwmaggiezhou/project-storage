package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import applicationsystem.ApplicationPage;
import applicationsystem.Constants;
import exceptions.AccountAlreadyExistException;
import exceptions.NoSuchClientException;

public class AdminHomePage extends AppCompatActivity {

    private ApplicationPage app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home_page);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(getString(R.string.welcome));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            InternalStorage.saveData(this.getApplicationContext(), app.getDatabase());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        return true;
    }

    public void accessClientInfo(View view) {
        Intent intent = new Intent(this, EditClient.class);
        intent.putExtra("@string/application_page", app);
        startActivityForResult(intent,this.getResources().getInteger(R.integer.ADMIN_HOME_PAGE));
    }

    public void accessFlightInfo(View view) {
        Intent intent = new Intent(this, EditFlight.class);
        intent.putExtra("@string/application_page", app);
        startActivityForResult(intent,this.getResources().getInteger(R.integer.ADMIN_HOME_PAGE));
    }

    public void uploadFiles(View view) {
        Intent intent = new Intent(this, Uploading.class);
        intent.putExtra("@string/application_page", app);
        startActivityForResult(intent,this.getResources().getInteger(R.integer.ADMIN_HOME_PAGE));
    }

    public void bookingSystem(View view) {
        Intent intent = new Intent(this, BookingSystem.class);
        intent.putExtra("@string/application_page", app);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.ADMIN_HOME_PAGE));
    }

    @Override
    public void onBackPressed() {}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.getResources().getInteger(R.integer.ADMIN_HOME_PAGE)) {
            app = (ApplicationPage) data.getSerializableExtra("@string/application_page");

        }
    }
    public void createAdmin(View view){
        String email = ((EditText) findViewById(R.id.admin_signup_email)).getText().toString();
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            displayToastMessage(getString(R.string.error_invalid_email));
            return;
        }
        String password = ((EditText) findViewById(R.id.admin_signup_password)).getText().toString();
        if (email.matches(getString(R.string.empty_str))) {
            displayToastMessage(getString(R.string.no_input) + getString(R.string.email_address));
        } else if (password.matches(getString(R.string.empty_str))) {
            displayToastMessage(getString(R.string.no_input) + getString(R.string.password));
        } else {
            try {
                app.createAdminAccount(email,password);
                displayToastMessage(getString(R.string.admin_account_created));
            } catch (AccountAlreadyExistException ex) {
                displayToastMessage(/*ex.getMessage()*/"account already exist, try another one");
            }
        }
    }

    public void displayToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}
