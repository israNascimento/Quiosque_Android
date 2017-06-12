package ufrrj.com.quiosque.web;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by israel on 10/06/17.
 */

public class LoginTask extends AsyncTask<String, Void, String> {

    public interface asyncResponse {
        void getResult(String output);
    }

    public asyncResponse delegate= null;

    public LoginTask(asyncResponse response) {
        this.delegate = response;
    }

    @Override
    protected String doInBackground(String... params) {
        String matricula = params[0];
        String senha = params[1];

        /**PARA TESTAR...**/
        //return "<div class=\"bloco_noticias\"><p     class=\"tit_noticias\">Novidades</p><p class=\"item_noticias_cal\"><a href=\"calendario\">Está disponível 1 novo item no CALENDÁRIO.</a></p><p class=\"item_noticias_arq\"><a href=\"arquivo\">Está disponível 1 novo ARQUIVO.</a></p><p class=\"item_noticias_not\"><a href=\"nota\">Está disponível 1 nova NOTA.</a></p></div>";

        try {
            URL url = new URL("http://academico.ufrrj.br/quiosque/aluno/quiosque.php");
            Log.d("Web", "Conectando...");
            return Login(url, matricula, senha);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }


    String Login(URL url, String matricula, String senha) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();

            String conteudoPOST = "edtIdUs="+matricula+"" +
                    "&edtIdSen="+senha+"&btnIdOk=+Ok+";

            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Length", ""+conteudoPOST.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.d("Web", ""+connection.getRequestProperties().toString());

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            byte[] bytes = conteudoPOST.getBytes("UTF-8");
            dataOutputStream.write(bytes, 0, bytes.length);
            dataOutputStream.flush();
            dataOutputStream.close();

            connection.connect();

            Scanner scanner = new Scanner(connection.getInputStream());
            StringBuilder builder = new StringBuilder();

            while(scanner.hasNextLine()) {
                builder.append(scanner.nextLine());
            }
            Log.d("Web", builder.toString());
            connection.disconnect();
            Log.d("Kb", "Kb: "+builder.toString().getBytes().length);
            Log.d("Web", "Kb: "+connection.getHeaderFields().toString());
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        delegate.getResult(s);
    }
}
