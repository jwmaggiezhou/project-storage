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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;

import applicationsystem.Constants;
import applicationsystem.ApplicationPage;
import exceptions.NoSuchClientException;

public class EditPersonalInfo extends AppCompatActivity {

    private ApplicationPage app;
    private EditText firstName, lastName, address, creditcardNumber, new_pass, confirm_pass;
    private boolean[] toEdit = new boolean[7];
    private String email;
    private DatePicker expirydate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal_info);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        email = intent.getStringExtra("@string/email_address");
        firstName = (EditText)findViewById(R.id.new_first_name);
        lastName = (EditText)findViewById(R.id.new_last_name);
        address = (EditText) findViewById(R.id.new_address);
        creditcardNumber = (EditText) findViewById(R.id.new_cred_num);
        expirydate = (DatePicker) findViewById(R.id.new_expiry_date);
        new_pass = (EditText) findViewById(R.id.new_password);
        confirm_pass = (EditText) findViewById(R.id.confirm_password);

        for (int i = 0; i < toEdit.length; i++) {
            toEdit[i] = false;
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.set_personal_info));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        TextView textView0 = (TextView) findViewById(R.id.curr_first_name);
        textView0.setTextSize(20);
        textView0.setText(app.viewAccountInfo(Constants.FIRSTNAMES));

        TextView textView1 = (TextView) findViewById(R.id.curr_last_name);
        textView1.setTextSize(20);
        textView1.setText(app.viewAccountInfo(Constants.LASTNAME));

        TextView textView2 = (TextView) findViewById(R.id.curr_address);
        textView2.setTextSize(20);
        textView2.setText(app.viewAccountInfo(Constants.ADDRESS));

        TextView textView3 = (TextView) findViewById(R.id.curr_cred_num);
        textView3.setTextSize(20);
        textView3.setText(app.viewAccountInfo(Constants.CREDITCARD));

        TextView textView4 = (TextView) findViewById(R.id.curr_expiry_date);
        textView4.setTextSize(20);
        textView4.setText(app.viewAccountInfo(Constants.EXPIRYDATE));
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

    public void editFirstName(View view) {
        toEdit[0] = !(toEdit[0]);
        if (toEdit[0]) {
            firstName.setVisibility(View.VISIBLE);
        } else {
            firstName.setVisibility(View.GONE);
        }
    }

    public void editLastName(View view) {
        toEdit[1] = !(toEdit[1]);
        if (toEdit[1]) {
            lastName.setVisibility(View.VISIBLE);

        } else {
            lastName.setVisibility(View.GONE);
        }
    }

    public void editAddress(View view) {
        toEdit[2] = !(toEdit[2]);
        if (toEdit[2]) {
            address.setVisibility(View.VISIBLE);

        } else {
            address.setVisibility(View.GONE);
        }
    }

    public void editCreditCardNumber(View view) {
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

    public void setPersonalInfo(View view) {

        try {
            if (toEdit[5]) {
                String newPass = new_pass.getText().toString();
                String confirmPass = confirm_pass.getText().toString();
                if (newPass.equals(confirmPass)) {
                    app.editAccountInfo(Constants.PASSWORD, newPass);
                } else {
                    displayExceptionMessage(getString(R.string.password_not_match));
                    return;
                }
            }
            if (toEdit[0]) {
                app.editAccountInfo(Constants.FIRSTNAMES, firstName.getText().toString());
            }
            if (toEdit[1]) {
                app.editAccountInfo(Constants.LASTNAME, lastName.getText().toString());
            }
            if (toEdit[2]) {
                app.editAccountInfo(Constants.ADDRESS, address.getText().toString());
            }
            if (toEdit[3]) {
                app.editAccountInfo(Constants.CREDITCARD, creditcardNumber.getText().toString());
            }
            if (toEdit[4]) {
                String dateString = expirydate.getYear() + getString(R.string.date_separator) + (expirydate.getMonth()+1) + getString(R.string.date_separator) + expirydate.getDayOfMonth();
                app.editAccountInfo(Constants.EXPIRYDATE, dateString);
            }
        } catch (NoSuchClientException | ParseException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, ClientHomePage.class);
        intent.putExtra("@string/application_page", app);
        intent.putExtra("@string/email_address", email);
        startActivity(intent);
    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent(this, ClientHomePage.class);
        backIntent.putExtra("@string/application_page", app);
        backIntent.putExtra("@string/email_address", email);
        startActivity(backIntent);
        finish();
    }

}
