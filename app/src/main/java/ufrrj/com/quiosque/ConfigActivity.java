package ufrrj.com.quiosque;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ConfigActivity extends AppCompatActivity {

    private TextView name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        Intent intent = getIntent();

        name = (TextView) findViewById(R.id.config_name);
        name.setText(intent.getStringExtra("nome"));

    }
}
