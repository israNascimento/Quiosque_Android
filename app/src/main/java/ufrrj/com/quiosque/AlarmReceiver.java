package ufrrj.com.quiosque;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ufrrj.com.quiosque.web.LoginTask;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by israel on 10/06/17.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private Context context;
    @Override
    public void onReceive(final Context context, Intent intent) {
        this.context = context;
        String matricula = intent.getStringExtra("matricula");
        String senha = intent.getStringExtra("senha");
        new LoginTask(new LoginTask.asyncResponse() {
            @Override
            public void getResult(String result) { /* Para pegar o resultado da AsyncTask*/
                Log.d("Resultado", ""+result);
                if(result == null) {
                    return;
                }
                int indexIni = result.indexOf("\"bloco_noticias\"");
                if(indexIni == -1) {
                    Log.d("Web", "Usuário ou senha inválido");
                    Toast.makeText(context, "Verifique seus dados", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                int lastIndex = result.indexOf("</div>", indexIni);
                String resultFormated = result.substring(indexIni, lastIndex);

                List<String> novidades = getNovidades(resultFormated);
                NotificationManager manager = (NotificationManager) context.getSystemService
                        (Context.NOTIFICATION_SERVICE);

                for (String s: novidades) {
                    if (Build.VERSION.SDK_INT >= 24) {
                        s = Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY).toString();
                    } else {
                        s = Html.fromHtml(s).toString();
                    }
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(android.R.drawable.arrow_up_float)
                            .setContentTitle("Novidades do quiosque")
                            .setContentText(s);

                    manager.notify(1, builder.build());
                }
            }
        }).execute(matricula, senha);
    }

    private List<String> getNovidades(String html){
        /****COMEÇA A TRATAR AS NOVIDADES INDIVIDUALMENTE****/
        int start;
        int end = 0;
        List<String> mList = new ArrayList<String>();
        while(html.indexOf("<a", end) > 0) {
            start = html.indexOf("<a", end);
            end = html.indexOf("</a>", start);
            String aux = html.substring(start, end);
            aux = aux.substring(aux.indexOf(">")+1);
            mList.add(aux);
        }
        return mList;
    }
}