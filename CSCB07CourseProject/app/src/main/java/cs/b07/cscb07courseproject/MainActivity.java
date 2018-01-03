package cs.b07.cscb07courseproject;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import database.Database;
import exceptions.*;

import applicationsystem.*;
import exceptions.AccountAlreadyExistException;

public class MainActivity extends AppCompatActivity {

    private LoginService loginService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loginService = new LoginService();
        try {
            loginService.setDatabase(InternalStorage.loadData(this.getApplicationContext()));
            if(!loginService.isExistingAdmin(getString(R.string.default_admin_email))){
                loginService.createDefaultAdmin(getString(R.string.default_admin_email),
                        getString(R.string.default_admin_password));

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void signupPage(View view) {
        Intent intent = new Intent(this, SignUpPage.class);
        intent.putExtra("@string/login_service", loginService);
        startActivity(intent);
    }

    public void clientLogInPage(View view) {
        Intent intent = new Intent(this, ClientLoginPage.class);
        intent.putExtra("@string/login_service", loginService);
        startActivity(intent);
    }

    public void adminLogIn(View view) {
        Intent intent = new Intent(this, AdminLogIn.class);
        intent.putExtra("@string/login_service", loginService);
        startActivity(intent);

    }

    public void displayExceptionMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}