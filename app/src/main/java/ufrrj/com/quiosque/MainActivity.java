package ufrrj.com.quiosque;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

        /*AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        manager.getNextAlarmClock()*/
    }

    private void Login(final String matricula, final String password) {
        Log.d("Login", "Matricula: "+matricula+" Senha: "+password);
        new LoginTask(new LoginTask.asyncResponse() {
            @Override
            public void getResult(String result) { /* Para pegar o resultado da AsyncTask*/
                Log.d("Resultado", ""+result);
                Context context = MainActivity.this;
                int index = result.indexOf("bloco_noticias");
                if(index == -1) {
                    Log.d("Web", "Usuário ou senha inválido");
                    Toast.makeText(context, "Matrícula ou senha inválidos", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
/*                int infoStart = result.indexOf("info_us");
                int infoEnd = result.indexOf("</div>", infoStart);
                String infos = result.substring(infoStart, infoEnd);

                int nameStart = infos.indexOf("/>");
                int nameEnd = infos.indexOf("<br", nameStart);
                String name = infos.substring(nameStart+4, nameEnd); //Retorna - />\t\t{NOME} Por isso +4
                Toast.makeText(context, name, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, ConfigActivity.class);
                intent.putExtra("nome", name);
                intent.putExtra("matricula", matricula);
                intent.putExtra("senha", password);
                startActivity(intent);
*/

                AlarmManager manager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
                Intent i = new Intent(context, AlarmReceiver.class);
                i.putExtra("matricula", matricula);
                i.putExtra("senha", password);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, i, 0);
                manager.setRepeating(AlarmManager.ELAPSED_REALTIME, 60000, 60*1000, pendingIntent);
            }
        }).execute(matricula, password);
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
