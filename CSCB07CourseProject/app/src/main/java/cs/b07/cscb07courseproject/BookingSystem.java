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

import java.io.IOException;

import applicationsystem.ApplicationPage;

public class BookingSystem extends AppCompatActivity {

    private ApplicationPage app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_system);

        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.booking_system));
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

    public void searchFlights(View view) {
        Intent intent = new Intent(this, SearchFlights.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/is_admin", true);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.BOOKING_SYSTEM));
    }

    public void searchItineraries(View view) {
        Intent intent = new Intent(this, SearchItineraries.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/is_admin", true);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.BOOKING_SYSTEM));
    }

    public void viewItineraries(View view) {
        Intent intent = new Intent(this, ViewItineraries.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/is_admin", true);
        startActivityForResult(intent, this.getResources().getInteger(R.integer.BOOKING_SYSTEM));
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("@string/application_page", app);
        setResult(this.getResources().getInteger(R.integer.BOOKING_SYSTEM), backIntent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.getResources().getInteger(R.integer.BOOKING_SYSTEM)) {
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
}
