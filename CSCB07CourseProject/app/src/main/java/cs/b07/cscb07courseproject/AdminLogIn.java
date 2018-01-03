package cs.b07.cscb07courseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import applicationsystem.ApplicationPage;
import applicationsystem.LoginService;
import exceptions.NoSuchClientException;

public class AdminLogIn extends AppCompatActivity {
    private LoginService loginService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_log_in);
        Intent intent = getIntent();
        Object obj = intent.getSerializableExtra("@string/login_service");
        loginService = (LoginService) obj;
    }

    public void LoginAdmin(View view) {
        String email = ((EditText) findViewById(R.id.email_address)).getText().toString();
        if(! android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this,getString(R.string.error_invalid_email),Toast.LENGTH_SHORT).show();
            return;
        }
        String password = ((EditText) findViewById(R.id.password)).getText().toString();

            if (loginService.isExistingAdmin(email)) {
                try{
                    loginService.loadingUserInfo(email, password);
                    ApplicationPage app = loginService.launchApplicationPage();
                    Intent intent = new Intent(this, AdminHomePage.class);
                    intent.putExtra("@string/application_page", app);
                    startActivity(intent);

                } catch(NoSuchClientException nsce) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(AdminLogIn.this);
                    alert.setTitle(getString(R.string.unable_to_login));
                    alert.setMessage(getString(R.string.user_pw_mismatch));
                    alert.setPositiveButton(getString(R.string.ok), null);
                    alert.setCancelable(true);
                    alert.create().show();
                }
            } else {
                Toast.makeText(this, getString(R.string.nonexisting_user_msg), Toast.LENGTH_SHORT).show();

            }

    }
}