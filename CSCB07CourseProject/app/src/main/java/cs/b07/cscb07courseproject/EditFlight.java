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
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;

import applicationsystem.ApplicationPage;
import applicationsystem.Constants;
import exceptions.NoSuchClientException;
import exceptions.NoSuchFlightException;

public class EditFlight extends AppCompatActivity {

    private ApplicationPage app;
    private EditText flightNumInput, airline, origin, destination, cost, capacity;
    private boolean[] toEdit = new boolean[9];
    private String flightNum;
    private DatePicker departureDate, arrivalDate;
    private TimePicker departureTime, arrivalTime;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        flightNum = intent.getStringExtra("@string/flight_number");
        airline = (EditText) findViewById(R.id.new_airline);
        origin = (EditText) findViewById(R.id.new_origin);
        destination = (EditText) findViewById(R.id.new_destination);
        departureDate = (DatePicker) findViewById(R.id.new_departure_date);
        departureTime = (TimePicker) findViewById(R.id.new_departure_time);
        arrivalDate = (DatePicker) findViewById(R.id.new_arrival_date);
        arrivalTime = (TimePicker) findViewById(R.id.new_arrival_time);
        cost = (EditText) findViewById(R.id.new_cost);
        capacity = (EditText)findViewById(R.id.new_capacity);
        scrollView = (ScrollView) findViewById(R.id.edit_flight_scrollview);
        for (int i = 0; i < toEdit.length; i++) {
            toEdit[i] = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_flight_info));
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

    public void verifyFlight(View view){
        flightNumInput = (EditText) findViewById(R.id.flight_number);
        flightNum = flightNumInput.getText().toString();
        try {
            app.viewFlight(flightNum);
            scrollView.setVisibility(View.VISIBLE);
            refreshFlightInfo();

        } catch (NoSuchFlightException e) {
            scrollView.setVisibility(View.GONE);
            displayExceptionMessage(e.getMessage());
        }
    }

    public void refreshFlightInfo() throws NoSuchFlightException {

        airline.setVisibility(View.GONE);
        origin.setVisibility(View.GONE);
        destination.setVisibility(View.GONE);
        departureDate.setVisibility(View.GONE);
        departureTime.setVisibility(View.GONE);
        arrivalDate.setVisibility(View.GONE);
        arrivalTime.setVisibility(View.GONE);
        cost.setVisibility(View.GONE);
        capacity.setVisibility(View.GONE);

        TextView textView = (TextView) findViewById(R.id.curr_airline);
        textView.setText(app.viewFlightInfo(flightNum, Constants.AIRLINE));

        textView = (TextView) findViewById(R.id.curr_origin);
        textView.setText(app.viewFlightInfo(flightNum, Constants.ORIGIN));

        textView = (TextView) findViewById(R.id.curr_destination);
        textView.setText(app.viewFlightInfo(flightNum, Constants.DESTINATION));

        textView = (TextView) findViewById(R.id.curr_departure_date);
        String dep = app.viewFlightInfo(flightNum, Constants.DEPARTUREDATETIME);
        String[] splitdep = dep.split(" ");

        textView.setText(splitdep[0]);

        textView = (TextView) findViewById(R.id.curr_departure_time);
        textView.setText(splitdep[1]);

        textView = (TextView) findViewById(R.id.curr_arrival_date);
        String arr = app.viewFlightInfo(flightNum, Constants.ARRIVALDATETIME);
        String[] splitarr = arr.split(" ");
        textView.setText(splitarr[0]);

        textView = (TextView) findViewById(R.id.curr_arrival_time);
        textView.setText(splitarr[1]);

        textView = (TextView) findViewById(R.id.curr_cost);
        textView.setText(app.viewFlightInfo(flightNum, Constants.COST));

        textView = (TextView) findViewById(R.id.curr_capacity);
        textView.setText(app.viewFlightInfo(flightNum, Constants.CAPACITY));

    }

    public void editAirline(View view) throws NoSuchFlightException {
        toEdit[0] = !(toEdit[0]);
        if (toEdit[0]) {
            airline.setVisibility(View.VISIBLE);
        } else {
            airline.setVisibility(View.GONE);
        }
    }

    public void editOrigin(View view) throws NoSuchFlightException {
        toEdit[1] = !(toEdit[1]);
        if (toEdit[1]) {
            origin.setVisibility(View.VISIBLE);

        } else {
            origin.setVisibility(View.GONE);
        }
    }

    public void editDestination(View view) throws NoSuchFlightException {
        toEdit[2] = !(toEdit[2]);
        if (toEdit[2]) {
            destination.setVisibility(View.VISIBLE);

        } else {
            destination.setVisibility(View.GONE);
        }
    }

    public void editDepartureDate(View View) throws NoSuchFlightException {
        toEdit[3] = !(toEdit[3]);
        if (toEdit[3]) {
            departureDate.setVisibility(View.VISIBLE);


        } else {
            departureDate.setVisibility(View.GONE);
        }
    }

    public void editDepartureTime(View View) throws NoSuchFlightException {
        toEdit[4] = !(toEdit[4]);
        if (toEdit[4]) {
            departureTime.setVisibility(View.VISIBLE);


        } else {
            departureTime.setVisibility(View.GONE);
        }
    }

    public void editArrivalDate(View View) throws NoSuchFlightException {
        toEdit[5] = !(toEdit[5]);
        if (toEdit[5]) {
            arrivalDate.setVisibility(View.VISIBLE);

        } else {
            arrivalDate.setVisibility(View.GONE);
        }
    }

    public void editArrivalTime(View View) throws NoSuchFlightException {
        toEdit[6] = !(toEdit[6]);
        if (toEdit[6]) {
            arrivalTime.setVisibility(View.VISIBLE);


        } else {
            arrivalTime.setVisibility(View.GONE);
        }
    }

    public void editCost(View view) throws NoSuchFlightException {
        toEdit[7] = !(toEdit[7]);
        if (toEdit[7]) {
            cost.setVisibility(View.VISIBLE);

        } else {
            cost.setVisibility(View.GONE);
        }
    }
    public void editCapacity(View view) throws NoSuchFlightException {
        toEdit[8] = !(toEdit[8]);
        if (toEdit[8]) {
            capacity.setVisibility(View.VISIBLE);

        } else {
            capacity.setVisibility(View.GONE);
        }
    }

    public void setFlightInfo(View view) throws NoSuchFlightException {

        try {
            if (toEdit[0]) {
                app.editFlightInfo(flightNum, Constants.AIRLINE, airline.getText().toString());
            }
            if (toEdit[1]) {
                app.editFlightInfo(flightNum, Constants.ORIGIN, origin.getText().toString());
            }
            if (toEdit[2]) {
                app.editFlightInfo(flightNum, Constants.DESTINATION, destination.getText().toString());
            }
            if (toEdit[3]) {
                String dateString = departureDate.getYear() + getString(R.string.date_separator) + (departureDate.getMonth() + 1) + getString(R.string.date_separator) + departureDate.getDayOfMonth();
                app.editFlightInfo(flightNum, Constants.DEPARTUREDATE, dateString);
            }
            if (toEdit[4]) {
                String timeString = departureTime.getHour() + getString(R.string.time_separator) + departureTime.getMinute();
                app.editFlightInfo(flightNum, Constants.DEPARTURETIME, timeString);
            }
            if (toEdit[5]) {
                String dateString = arrivalDate.getYear() + getString(R.string.date_separator) + (arrivalDate.getMonth() + 1) + getString(R.string.date_separator) + arrivalDate.getDayOfMonth();
                app.editFlightInfo(flightNum, Constants.ARRIVALDATE, dateString);
            }
            if (toEdit[6]) {
                String timeString = arrivalTime.getHour() + getString(R.string.time_separator) + arrivalTime.getMinute();
                app.editFlightInfo(flightNum, Constants.ARRIVALTIME, timeString);
            }
            if (toEdit[7]) {
                app.editFlightInfo(flightNum, Constants.COST, cost.getText().toString());
            }
            if (toEdit[8]) {
                app.editFlightInfo(flightNum, Constants.CAPACITY, capacity.getText().toString());
            }
        } catch (NoSuchFlightException | ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, EditFlight.class);
        intent.putExtra("@string/application_page", app);
        //intent.putExtra("@string/email_address", email);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, AdminHomePage.class);
        backIntent.putExtra("@string/application_page", app);
        startActivity(backIntent);
        finish();
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
