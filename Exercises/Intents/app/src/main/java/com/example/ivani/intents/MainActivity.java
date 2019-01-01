package com.example.ivani.intents;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageView moodView = null;
    ImageView phoneView = null;
    ImageView webView = null;
    ImageView locationView = null;
    Button createButton = null;
    String mood = "";
    String phoneNumber = "";
    String webAddress = "";
    String location = "";
    private static final int REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        moodView = findViewById(R.id.mood);
        phoneView = findViewById(R.id.phone);
        webView = findViewById(R.id.web);
        locationView = findViewById(R.id.location);
        createButton = findViewById(R.id.button);

        moodView.setVisibility(View.GONE);
        phoneView.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        locationView.setVisibility(View.GONE);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Main2Activity.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        phoneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+phoneNumber));
                startActivity(intent);
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this,"Invalid phone number",Toast.LENGTH_SHORT).show();
                }
            }
        });

        webView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://"+webAddress));
                    startActivity(intent);
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this,"Invalid web address",Toast.LENGTH_SHORT).show();
                }
            }
        });

        locationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:0,0?q="+location));
                    startActivity(intent);
                }
                catch(Exception e){
                    Toast.makeText(MainActivity.this,"Invalid location",Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK){

            moodView.setVisibility(View.VISIBLE);
            phoneView.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);
            locationView.setVisibility(View.VISIBLE);
            mood = data.getStringExtra("mood");
            phoneNumber = data.getStringExtra("phone");
            webAddress = data.getStringExtra("web");
            location = data.getStringExtra("location");

           if(mood.equals("happy")){
                moodView.setImageResource(R.drawable.ic_sentiment_satisfied_black_24dp);
            }
            else if (mood.equals("neutral")){
                moodView.setImageResource(R.drawable.ic_sentiment_neutral_black_24dp);
            }
            else{
                moodView.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp);
            }

        }
    }
}
