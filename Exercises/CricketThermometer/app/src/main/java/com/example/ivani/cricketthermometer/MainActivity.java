package com.example.ivani.cricketthermometer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button button = null;
    EditText editText = null;
    TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView2);
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        textView.setVisibility(View.GONE);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(editText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter a number in the field", Toast.LENGTH_SHORT).show();
                }
                else if (editText.getText().toString().length() > 3){
                    Toast.makeText(getApplicationContext(), "Please enter 3 or less digits in the field", Toast.LENGTH_SHORT).show();
                }
                else{
                    int chirps = Integer.parseInt(editText.getText().toString().trim());
                    double temperature = chirps / 3.0 + 4;
                    String result = String.format(getString(R.string.result),temperature);
                    textView.setText(result);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });







    }
}
