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
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

import applicationsystem.ApplicationPage;
import applicationsystem.Constants;
import exceptions.NoSuchClientException;


public class
ClientHomePage extends AppCompatActivity {

    private ApplicationPage app;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home_page);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        email = intent.getStringExtra("@string/email_address");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String firstName = app.viewAccountInfo(Constants.FIRSTNAMES);
        String lastName = app.viewAccountInfo(Constants.LASTNAME);

        getSupportActionBar().setTitle(getString(R.string.welcome) + firstName + getString(R.string.space) + lastName);
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

    public void editPersonalInfo(View view) {
        Intent intent = new Intent(this, EditPersonalInfo.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/email_address", email);
        intent.putExtra("@string/is_admin", false);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.CLIENT_HOME_PAGE));
    }

    public void searchFlights(View view) {
        Intent intent = new Intent(this, SearchFlights.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/email_address", email);
		intent.putExtra("@string/is_admin", false);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.CLIENT_HOME_PAGE));
    }
    public void searchItineraries(View view) {
        Intent intent = new Intent(this, SearchItineraries.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/email_address", email);
        intent.putExtra("@string/is_admin", false);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.CLIENT_HOME_PAGE));
    }

    public void viewItineraries(View view) {
        Intent intent = new Intent(this, ViewItineraries.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/email_address", email);
        intent.putExtra("@string/is_admin", false);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.CLIENT_HOME_PAGE));
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.getResources().getInteger(R.integer.CLIENT_HOME_PAGE)) {
            /*
            if (resultCode == this.getResources().getInteger(R.integer.SEARCH_ITINERARIES)) {
                app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
            } else if (resultCode == this.getResources().getInteger(R.integer.VIEW_ITINERARIES)) {
                app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
            }else if (resultCode == this.getResources().getInteger(R.integer.SEARCH_FLIGHTS)) {
                app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
            }else if (resultCode == this.getResources().getInteger(R.integer.PERSONAL_INFO)) {
                app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
            }*/
            app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
        }
    }

    @Override
    public void onBackPressed() {}

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
