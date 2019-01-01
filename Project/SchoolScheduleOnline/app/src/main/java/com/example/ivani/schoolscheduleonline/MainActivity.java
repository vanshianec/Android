package com.example.ivani.schoolscheduleonline;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private Button showTimetable;
    private Button chooseGrade;
    private boolean isDialogCancelled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //display fullscreen background(no notification bar and action bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        checkIfDialogIsShowing(savedInstanceState);

        mQueue = Volley.newRequestQueue(getApplicationContext());
        showTimetable = findViewById(R.id.showProgram);
        chooseGrade = findViewById(R.id.chooseGrade);

        showTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Timetable.class);
                startActivity(intent);
            }
        });

        chooseGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayChooseGradeDialog();
            }
        });

    }


    @Override
    public void onBackPressed() {
        // exit app on back pressed when in main activity
        moveTaskToBack(true);
    }

    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isDialogCancelled", isDialogCancelled);
    }


    private void checkIfDialogIsShowing(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            isDialogCancelled = savedInstanceState.getBoolean("isDialogCancelled");
        }
        else{
            isDialogCancelled = true;
        }
        if(!isDialogCancelled){
            displayChooseGradeDialog();
        }
    }


    private void displayChooseGradeDialog() {
        AlertDialog.Builder builder = createBuilder();
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        setAlertDialogSettings(dialog);
        dialog.show();
    }

    private void setAlertDialogSettings(AlertDialog dialog) {
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                isDialogCancelled = true;
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                isDialogCancelled = false;
            }
        });
        ListView listView = dialog.getListView();
        listView.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
        listView.setDividerHeight(2);
        //remove last divider
        listView.setOverscrollFooter(new ColorDrawable(Color.TRANSPARENT));
    }

    @NonNull
    private AlertDialog.Builder createBuilder() {
        final String[] grades = {"5а", "5б", "6а", "6б", "7а", "7б", "8а", "8б", "8в", "8г", "8д", "8е",
                "9а", "9б", "9в", "9г", "9д", "9е", "10а", "10б", "10в", "10г", "10д", "10е",
                "11а", "11б", "11в", "11г", "11д", "11е", "12а", "12б", "12в", "12г", "12д", "12е"};
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.custom_text_view, grades);
        // setup the alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                //take the selected grade info and send it to the database
                //after that the database will return the result from the selected grade and the shared preferences will save it
                takeSelectedGradeFromDatabase(grades[index]);
            }
        });
        return builder;
    }

    public void takeSelectedGradeFromDatabase(final String grade) {
        //send request to the database
        String url = "https://liverpoolynwa.000webhostapp.com/get_data.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //save result from database in shared preferences
                        saveInSharedResponse(response);
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Няма връзка със сървъра. Моля опитайте по-късно.", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() {
                //send key to php server to select given table
                //after that the php server will return a table from the database in JSON format
                Map<String, String> MyData = new HashMap<>();
                MyData.put("androidKey", grade);
                return MyData;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mQueue.add(request);
        showLoadingDialogUntilResponse();
    }

    private void showLoadingDialogUntilResponse() {
        final Dialog loadingDialog = createLoadingDialog();
        mQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {
            @Override
            public void onRequestFinished(Request<String> request) {
                if (loadingDialog.isShowing())
                    loadingDialog.dismiss();
            }
        });
    }

    @NonNull
        private Dialog createLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(R.layout.progress_bar);
        final Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    private void saveInSharedResponse(String response) {
        SharedPreferences m = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = m.edit();
        editor.putString("Response", response);
        editor.apply();
    }

}
