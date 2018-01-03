package cs.b07.cscb07courseproject;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import applicationsystem.ApplicationPage;
import applicationsystem.Constants;
import exceptions.NoSeatsAvailableException;
import exceptions.NoSuchClientException;
import travel.Itinerary;


public class SearchItineraries extends AppCompatActivity {

    private ApplicationPage app;
    private String email;
    private boolean is_admin;
    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int date) {
            calendar.set(year, month, date, 0, 0, 0);
            changeText();
        }
    };

    private EditText departure_text, origin_text, destination_text, book_to;
    private ListView view_list_itineraries;
    private Button book_itinerary;
    private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static DateFormat datetime = new SimpleDateFormat("yyyy-MM-dd hh:mm", Locale.getDefault());
    private ArrayList itineraries;
    private ToggleButton sort_order;

    private int itinerary_position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itineraries);

        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        email = intent.getStringExtra("@string/email_address");
        is_admin = intent.getBooleanExtra("@string/is_admin", false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.search_itineraries));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        departure_text = (EditText) findViewById(R.id.set_dep);
        origin_text = (EditText) findViewById(R.id.set_origin);
        destination_text = (EditText) findViewById(R.id.set_destination);
        departure_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SearchItineraries.this, datePicker, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        book_itinerary = (Button) findViewById(R.id.book_itinerary);
        book_to = (EditText) findViewById(R.id.book_to);
        sort_order = (ToggleButton) findViewById(R.id.order);

        view_list_itineraries = (ListView) findViewById(R.id.list_itinerary);
        view_list_itineraries.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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

    public void searchItineraries(View view) {
        book_itinerary.setVisibility(View.GONE);

        Date dep = null;
        try {
            dep = date.parse(departure_text.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        itineraries = app.getItineraries(dep, origin_text.getText().toString(), destination_text.getText().toString(), Constants.MAX_LAYOVER, Constants.MIN_LAYOVER);
        LinearLayout sorts = (LinearLayout) findViewById(R.id.sort_buttons);
        if (itineraries.size() != 0) {
            sorts.setVisibility(View.VISIBLE);
        } else {
            sorts.setVisibility(View.GONE);
        }
        showItineraries(itineraries);
    }

    private void showItineraries(ArrayList itin) {
        view_list_itineraries.setAdapter(new ArrayAdapter<String>(this, R.layout.flight_list_layout, itin));

        view_list_itineraries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (is_admin) {
                    book_to.setVisibility(View.VISIBLE);
                    book_itinerary.getLayoutParams().width = LinearLayout.LayoutParams.WRAP_CONTENT;
                }
                book_itinerary.setVisibility(View.VISIBLE);
                itinerary_position = position;
            }
        });
    }

    public void bookItinerary(View view){
        try {
            String booking_email = "";
            if (is_admin) {
                booking_email = book_to.getText().toString();
            } else {
                booking_email = email;
            }
            app.bookItinerary(booking_email, (Itinerary) view_list_itineraries.getAdapter().getItem(itinerary_position));
            displayExceptionMessage(getString(R.string.booking_complete));
        } catch (NoSuchClientException ex) {
            displayExceptionMessage(getString(R.string.no_such_client));
        } catch (NoSeatsAvailableException e) {
            displayExceptionMessage(getString(R.string.itinerary_no_seats));
        }
    }

    public void sortCost(View view) {
        book_itinerary.setVisibility(View.GONE);
        ArrayList costItineraries = null;
        if (sort_order.isChecked()) {
            costItineraries = app.sortItinerariesByCost(itineraries);
        } else {
            costItineraries = app.sortItinerariesByCostDescending(itineraries);
        }
        showItineraries(costItineraries);
    }

    public void sortTravelTime(View view) {
        book_itinerary.setVisibility(View.GONE);
        ArrayList traveltimeItineraries;
        if (sort_order.isChecked()) {
            traveltimeItineraries = app.sortItinerariesByTravelTime(itineraries);
        } else {
            traveltimeItineraries = app.sortItinerariesByTravelTimeDescending(itineraries);
        }
        showItineraries(traveltimeItineraries);
    }


    private void changeText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        departure_text.setText(dateFormat.format(calendar.getTime()));
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("@string/application_page", app);
        setResult(this.getResources().getInteger(R.integer.SEARCH_ITINERARIES), backIntent);
        finish();
    }
}

