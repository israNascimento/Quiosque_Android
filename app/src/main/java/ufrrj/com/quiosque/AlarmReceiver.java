package ufrrj.com.quiosque;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import ufrrj.com.quiosque.web.LoginTask;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by israel on 10/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        new LoginTask(new LoginTask.asyncResponse() {
            @Override
            public void getResult(String result) { /* Para pegar o resultado da AsyncTask*/
                Log.d("Resultado", ""+result);
                int indexIni = result.indexOf("\"bloco_noticias\"");
                if(indexIni == -1) {
                    Log.d("Web", "Usuário ou senha inválido");
                    Toast.makeText(context, "Verifique seus dados", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                int lastIndex = result.indexOf("</div>", indexIni);
                String novidades = result.substring(indexIni, lastIndex);

                Toast.makeText(context, "Feito: "+result.substring(indexIni, lastIndex),
                        Toast.LENGTH_SHORT).show();

            }
        }).execute("123456", "878554");
    }
}