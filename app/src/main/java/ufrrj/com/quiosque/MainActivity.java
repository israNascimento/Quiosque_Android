package ufrrj.com.quiosque;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ufrrj.com.quiosque.web.LoginTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button signin;
    private EditText matricula, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        matricula = (EditText) findViewById(R.id.login_matricula);
        password = (EditText) findViewById(R.id.login_password);
        signin = (Button) findViewById(R.id.login_signin);
        signin.setOnClickListener(this);
    }

    private void Login(String matricula, String password) {
        Log.d("Login", "Matricula: "+matricula+" Senha: "+password);
        new LoginTask().execute(matricula, password);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_signin:
                Login(matricula.getText().toString(), password.getText().toString());
                break;
        }
    }
}
