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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import applicationsystem.ApplicationPage;
import exceptions.NoSuchClientException;

public class ViewItineraries extends AppCompatActivity {

    private ApplicationPage app;
    private String email;
    private boolean is_admin;
    private LinearLayout view_for_admin;
    private EditText to_view;
    private ListView view_list_itineraries;
    private List itineraries;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_itineraries);

        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        email = intent.getStringExtra("@string/email_address");
        is_admin = intent.getBooleanExtra("@string/is_admin", false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.view_itineraries));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        view_for_admin = (LinearLayout) findViewById(R.id.view_for_admin);
        to_view = (EditText) findViewById(R.id.to_view);
        view_list_itineraries = (ListView) findViewById(R.id.list_itinerary);

        if (is_admin) {
            view_for_admin.setVisibility(View.VISIBLE);
        } else {
            try {
                itineraries = app.getClientItineraries(email);
                showItineraries(itineraries);
            } catch (NoSuchClientException e) {
                e.printStackTrace();
            }
        }
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

    private void showItineraries(List itin) {
        view_list_itineraries.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itin));
    }

    public void viewItineraries(View view) {
        try {
            itineraries = app.getClientItineraries(to_view.getText().toString());
            showItineraries(itineraries);
        } catch (NoSuchClientException e) {
            displayExceptionMessage(getString(R.string.no_such_client));
        }
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("@string/application_page", app);
        setResult(this.getResources().getInteger(R.integer.VIEW_ITINERARIES), backIntent);
        finish();
    }
}
