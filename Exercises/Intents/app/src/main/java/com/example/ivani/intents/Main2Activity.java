package com.example.ivani.intents;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity implements View.OnClickListener {


    EditText name = null;
    EditText phone = null;
    EditText web = null;
    EditText location = null;
    ImageView sad = null;
    ImageView neutral = null;
    ImageView happy = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editNumber);
        web = findViewById(R.id.editWeb);
        location = findViewById(R.id.editLocation);
        sad = findViewById(R.id.imageView);
        neutral = findViewById(R.id.imageView2);
        happy = findViewById(R.id.imageView3);

        sad.setOnClickListener(this);
        neutral.setOnClickListener(this);
        happy.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if(name.getText().toString().isEmpty() || phone.getText().toString().isEmpty() ||
                web.getText().toString().isEmpty() || location.getText().toString().isEmpty()){
            Toast.makeText(this,"Please enter all fields",Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent();
            intent.putExtra("name",name.getText().toString().trim());
            intent.putExtra("phone",phone.getText().toString().trim());
            intent.putExtra("web",web.getText().toString().trim());
            intent.putExtra("location",location.getText().toString().trim());

            if(v.getId() == R.id.imageView){
                intent.putExtra("mood","sad");
            }
            else if (v.getId() == R.id.imageView2){
                intent.putExtra("mood","neutral");
            }
            else{
                intent.putExtra("mood","happy");
            }
            setResult(RESULT_OK,intent);
            Main2Activity.this.finish();
        }
    }
}
