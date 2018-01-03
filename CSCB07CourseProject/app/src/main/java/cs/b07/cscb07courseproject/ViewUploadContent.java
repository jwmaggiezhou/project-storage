package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.CheckableImageButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import applicationsystem.ApplicationPage;
import database.Database;
import exceptions.NoSuchClientException;

public class ViewUploadContent extends AppCompatActivity {
    private ApplicationPage app;
    private File file;
    private File pwFile;
    private boolean for_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_upload_content);
        Intent intent = getIntent();
        app = (ApplicationPage) intent.getSerializableExtra("@string/application_page");
        file = (File) intent.getSerializableExtra("@string/file");
        for_client = (Boolean) intent.getSerializableExtra("@string/for_client");
        if(for_client){
            pwFile = (File) intent.getSerializableExtra("@string/pw_file");
        }
        try {
            Scanner scan = new Scanner(file);
            TextView textView = (TextView) findViewById(R.id.file_content);
            while (scan.hasNextLine()) {
                textView.append(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            AlertDialog.Builder alert = new AlertDialog.Builder(ViewUploadContent.this);
            alert.setMessage(getString(R.string.file_not_exist));
            alert.create().show();
            final Intent backIntent = new Intent(this, Uploading.class);
            backIntent.putExtra("@string/application_page", app);
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                        startActivity(backIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }


    public void save(View view) throws IOException {

        String path = file.getPath();

        if (for_client) {
            String pwPath = pwFile.getPath();
            try {
                app.uploadClientInfo(path);
                app.uploadPasswordInfo(pwPath);
            } catch (ParseException e) {
                displayAlert(getString(R.string.incorrect_date),"yyyy-MM-dd");
            } catch (NoSuchClientException e) {
                displayAlert(getString(R.string.unable_upload), getString(R.string.one_more_pw_not_match));
            } catch (FileNotFoundException e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(ViewUploadContent.this);
                alert.setMessage(getString(R.string.file_not_exist));
                alert.create().show();
                final Intent backIntent = new Intent(this, Uploading.class);
                backIntent.putExtra("@string/application_page", app);
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            startActivity(backIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();

            }
        } else {
            try {
                app.uploadFlightInfo(path);
            } catch (ParseException e) {
                displayAlert(getString(R.string.incorrect_date_time),"yyyy-MM-dd hh:mm");
            }
        }
        Toast.makeText(this, R.string.success_msg, Toast.LENGTH_SHORT).show();
        final Intent backIntent = new Intent(this, Uploading.class);
        backIntent.putExtra("@string/application_page", app);
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500); // As I am using LENGTH_LONG in Toast
                    startActivity(backIntent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    private void displayAlert(String title, String msg){
        AlertDialog.Builder alert = new AlertDialog.Builder(ViewUploadContent.this);
        alert.setTitle(title);
        alert.setMessage(msg);
        alert.setPositiveButton(getString(R.string.ok), null);
        alert.setCancelable(true);
        alert.create().show();

    }
    @Override
    public void onBackPressed() {
        Intent backIntent = new Intent();
        backIntent.putExtra("@string/application_page", app);
        setResult(this.getResources().getInteger(R.integer.VIEW_UPLOAD_CONTENT), backIntent);
        finish();
    }

}
