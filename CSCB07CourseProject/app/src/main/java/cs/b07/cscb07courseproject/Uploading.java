package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import applicationsystem.ApplicationPage;
import exceptions.NoSuchClientException;
import exceptions.NotValidInputException;

public class Uploading extends AppCompatActivity {
    private static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm");
    private ApplicationPage app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploading);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle(getString(R.string.upload_info));
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

    public void uploadClientFiles(View view){
        String clientFileName = ((EditText) findViewById(R.id.client_file_name)).getText().toString();
        String pwFileName = ((EditText) findViewById(R.id.password_file_name)).getText().toString();
        if(clientFileName.matches("") || pwFileName.matches("")){
            Toast.makeText(this, getString(R.string.missing_input), Toast.LENGTH_SHORT).show();
            return;
        }
        /*File pwFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), pwFileName);
 		File clientFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), clientFileName);*/
        File userdata = this.getApplicationContext().getFilesDir();
        File clientFile = new File(userdata, clientFileName);
        File pwFile = new File(userdata, pwFileName);
        Intent intent = new Intent(this, ViewUploadContent.class);
        intent.putExtra("@string/file", clientFile);
        intent.putExtra("@string/pw_file", pwFile);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/for_client",true);
        startActivityForResult(intent,this.getResources().getInteger(R.integer.UPLOADING));
    }

    public void uploadFlightFile(View view){
        /*
        String fileName = ((EditText) findViewById(R.id.flight_file_name)).getText().toString();
        File flightFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
        */
        EditText fileName = (EditText) findViewById(R.id.flight_file_name);
        String fileNameText = fileName.getText().toString();
        File userdata = this.getApplicationContext().getFilesDir();
        File flightFile = new File(userdata, fileNameText);
        Intent intent = new Intent(this, ViewUploadContent.class);
        intent.putExtra("@string/file", flightFile);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/for_client",false);
        startActivityForResult(intent,this.getResources().getInteger(R.integer.UPLOADING));
    }

    public void enterFlightInfo(View view) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.flight_info);
        if (layout.getVisibility() == View.VISIBLE) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
    }

    public void saveFlightInfo(View view) {
        String flightNumber = ((EditText) findViewById(R.id.flight_number)).getText().toString();
        String departure = ((EditText) findViewById(R.id.departure_date)).getText().toString();
        String arrival = ((EditText) findViewById(R.id.arrival_date)).getText().toString();
        Date departureD;
        Date arrivalD;
        try {
            departureD = dateTime.parse(departure);
            arrivalD = dateTime.parse(arrival);
            String airLine = ((EditText) findViewById(R.id.new_airline)).getText().toString();
            String origin = ((EditText) findViewById(R.id.Origin)).getText().toString();
            String destination = ((EditText) findViewById(R.id.Destination)).getText().toString();
            String price = ((EditText) findViewById(R.id.price)).getText().toString();
            String numSeat = ((EditText) findViewById(R.id.numSeat)).getText().toString();
            if (flightNumber.matches("") || airLine.matches("") || origin.matches("") || destination.matches("")) {
                Toast.makeText(this, getString(R.string.missing_input), Toast.LENGTH_SHORT).show();
            } else {
                app.addFlight(flightNumber, departureD, arrivalD, airLine, origin, destination, Double.valueOf(price), Integer.valueOf(numSeat));
                Toast.makeText(this, R.string.success_msg, Toast.LENGTH_SHORT).show();
                final Intent backIntent = new Intent(this, Uploading.class);
                backIntent.putExtra("@string/application_page", app);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1500); // As I am using LENGTH_LONG in Toast
                            startActivity(backIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
                }
        }catch(ParseException | NumberFormatException ex){
                Toast.makeText(this, getString(R.string.incorrect_input_format), Toast.LENGTH_SHORT).show();
            }
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, AdminHomePage.class);
        backIntent.putExtra("@string/application_page", app);
        startActivity(backIntent);
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == this.getResources().getInteger(R.integer.UPLOADING)) {
            app = (ApplicationPage) data.getSerializableExtra("@string/application_page");
        }
    }

}

