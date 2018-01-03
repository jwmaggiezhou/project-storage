package cs.b07.cscb07courseproject;

import android.app.Application;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ScrollView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;

import applicationsystem.ApplicationPage;
import applicationsystem.Constants;
import exceptions.NoSuchClientException;

public class EditClient extends AppCompatActivity {

    private ApplicationPage app;
    private EditText clientEmailInput, firstName, lastName, address, creditcardNumber, new_pass, confirm_pass;
    boolean[] toEdit = new boolean[6];
    private DatePicker expirydate;
    private String clientEmail;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_client);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");

        firstName = (EditText)findViewById(R.id.new_first_name);
        lastName = (EditText)findViewById(R.id.new_last_name);
        address = (EditText) findViewById(R.id.new_address);
        creditcardNumber = (EditText) findViewById(R.id.new_cred_num);
        expirydate = (DatePicker) findViewById(R.id.new_expiry_date);
        new_pass = (EditText) findViewById(R.id.new_password);
        confirm_pass = (EditText) findViewById(R.id.confirm_password);
        scrollView = (ScrollView) findViewById(R.id.edit_client_scrollview);
        for (int i = 0; i < toEdit.length; i++) {
            toEdit[i] = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.edit_client_info));
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

    public void verifyClient(View view) {
        clientEmail = ((EditText)findViewById(R.id.client_email_address)).getText().toString();
        try {
            app.viewClient(clientEmail);
            scrollView.setVisibility(View.VISIBLE);
            refreshClientInfo();
        } catch (NoSuchClientException e) {
            scrollView.setVisibility(View.GONE);
            displayExceptionMessage(e.getMessage());
        }
    }

    public void refreshClientInfo() throws NoSuchClientException {
        firstName.setVisibility(View.GONE);
        lastName.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        creditcardNumber.setVisibility(View.GONE);
        expirydate.setVisibility(View.GONE);
        new_pass.setVisibility(View.GONE);
        confirm_pass.setVisibility(View.GONE);

        TextView textView = (TextView) findViewById(R.id.curr_first_name);
        textView.setText(app.viewClientInfo(clientEmail, Constants.FIRSTNAMES));

        textView = (TextView) findViewById(R.id.curr_last_name);
        textView.setText(app.viewClientInfo(clientEmail, Constants.LASTNAME));

        textView = (TextView) findViewById(R.id.curr_address);
        textView.setText(app.viewClientInfo(clientEmail, Constants.ADDRESS));

        textView = (TextView) findViewById(R.id.curr_cred_num);
        textView.setText(app.viewClientInfo(clientEmail, Constants.CREDITCARD));

        textView = (TextView) findViewById(R.id.curr_expiry_date);
        textView.setText(app.viewClientInfo(clientEmail, Constants.EXPIRYDATE));
    }

    public void editFirstName(View view) throws NoSuchClientException {
        toEdit[0] = !(toEdit[0]);
        if (toEdit[0]) {
            firstName.setVisibility(View.VISIBLE);

        } else {
            firstName.setVisibility(View.GONE);
        }
    }

    public void editLastName(View view) throws NoSuchClientException {
        toEdit[1] = !(toEdit[1]);
        if (toEdit[1]) {
            lastName.setVisibility(View.VISIBLE);

        } else {
            lastName.setVisibility(View.GONE);
        }
    }

    public void editAddress(View view) throws NoSuchClientException {
        toEdit[2] = !(toEdit[2]);
        if (toEdit[2]) {
            address.setVisibility(View.VISIBLE);

        } else {
            address.setVisibility(View.GONE);
        }
    }

    public void editCreditCardNumber(View view) throws NoSuchClientException {
        toEdit[3] = !(toEdit[3]);
        if (toEdit[3]) {
            creditcardNumber.setVisibility(View.VISIBLE);

        } else {
            creditcardNumber.setVisibility(View.GONE);
        }
    }

    public void editExpiryDate(View View) {
        toEdit[4] = !(toEdit[4]);
        if (toEdit[4]) {
            expirydate.setVisibility(View.VISIBLE);


        } else {
            expirydate.setVisibility(View.GONE);
        }
    }

    public void editPassword(View view) {
        toEdit[5] = !(toEdit[5]);
        if (toEdit[5]) {
            new_pass.setVisibility(View.VISIBLE);
            confirm_pass.setVisibility(View.VISIBLE);
        } else {
            new_pass.setVisibility(View.GONE);
            confirm_pass.setVisibility(View.GONE);
        }
    }

    public void setClientInfo(View view) {

        try {
            if (toEdit[5]) {
                String newPass = new_pass.getText().toString();
                String confirmPass = confirm_pass.getText().toString();
                if (newPass.equals(confirmPass)) {
                    app.editClientInfo(clientEmail, Constants.PASSWORD, newPass);
                } else {
                    displayExceptionMessage(getString(R.string.password_not_match));
                    return;
                }
            }
            if (toEdit[0]) {
                app.editClientInfo(clientEmail, Constants.FIRSTNAMES, firstName.getText().toString());
            }
            if (toEdit[1]) {
                app.editClientInfo(clientEmail, Constants.LASTNAME, lastName.getText().toString());
            }
            if (toEdit[2]) {
                app.editClientInfo(clientEmail, Constants.ADDRESS, address.getText().toString());
            }
            if (toEdit[3]) {
                app.editClientInfo(clientEmail, Constants.CREDITCARD, creditcardNumber.getText().toString());
            }
            if (toEdit[4]) {
                String dateString = expirydate.getYear() + getString(R.string.date_separator) + (expirydate.getMonth()+1) + getString(R.string.date_separator) + expirydate.getDayOfMonth();
                app.editClientInfo(clientEmail, Constants.EXPIRYDATE, dateString);
            }
        } catch (NoSuchClientException | ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, EditClient.class);
        intent.putExtra("@string/application_page", app);
        //intent.putExtra("@string/email_address", email);
        startActivity(intent);
        finish();
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, AdminHomePage.class);
        backIntent.putExtra("@string/application_page", app);
        startActivity(backIntent);
        finish();
    }
}
