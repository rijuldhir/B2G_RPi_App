package com.example.android.pimon;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static android.R.id.edit;


public class MainActivity extends AppCompatActivity {

    int cred,cyellow,cwhite,cgreen; //Variables of Toggle Button to Show
    String IP;     //TO store IP of Raspberry Pi3
    EditText edit;
    //NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ToggleButton red = (ToggleButton) findViewById(R.id.red);  // Referencing The buttons of XML
        final ToggleButton green = (ToggleButton) findViewById(R.id.green);
        final ToggleButton yellow = (ToggleButton) findViewById(R.id.yellow);
        final ToggleButton white = (ToggleButton) findViewById(R.id.white);
        Button button = (Button)findViewById(R.id.gray);
        Button reset = (Button) findViewById(R.id.reset);
        edit = (EditText) findViewById(R.id.edittext);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                        // Setting OnClick Listener
                new Background_get().execute("red="+0+"&yellow="+0+"&green="+0+"&white="+0);  // Calling execute method in AsynTask which uses doinbackground Method
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nred,nyellow,ngreen,nwhite; // variables to find gray code
                nred = cred;
                nwhite = cwhite ^ cgreen;
                ngreen = cyellow ^ cgreen;
                nyellow = cred ^ cyellow ;
                IP = edit.getText().toString();  //Finding IP from Edit Text
                Toast.makeText(MainActivity.this, "cred = "+nred+"cyellow = "+nyellow+"cgreen = "+ngreen+"cwhite = "+nwhite, Toast.LENGTH_SHORT).show();
                new Background_get().execute("red="+ nred +"&yellow="+nyellow +"&green="+ngreen+"&white="+nwhite); // Calling execute method in AsynTask which uses doinbackground Method
            }
        });

        red.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {      // Setting Toogle Variables on Button click in APP
                if(red.isChecked())
                {
                    //Toast.makeText(MainActivity.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                    cred=1;
                }
                else {
                  //  Toast.makeText(MainActivity.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                    cred=0;
                }
            }
        });
        green.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    // Setting Toogle Variables on Button click in APP

                if(green.isChecked())
                {
                    cgreen=1;
                    //Toast.makeText(MainActivity.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                }
                else {
                    cgreen = 0;
                  //  Toast.makeText(MainActivity.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }
            }
        });
        yellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {    // Setting Toogle Variables on Button click in APP

                if(yellow.isChecked())
                {
                    cyellow=1;
                    //Toast.makeText(MainActivity.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                }
                else {
                    cyellow=0;
                    //Toast.makeText(MainActivity.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }
            }
        });
        white.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {     // Setting Toogle Variables on Button click in APP

                if(white.isChecked())
                {
                    cwhite=1;
                    //Toast.makeText(MainActivity.this, "Toggle button is on", Toast.LENGTH_LONG).show();
                }
                else {
                    cwhite=0;
                    //Toast.makeText(MainActivity.this, "Toggle button is Off", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private class Background_get extends AsyncTask<String, Void, String> {    //Asynctask Used
        @Override
        protected String doInBackground(String... params) {       //Method doInBackground New Thread
            try {

                URL url = new URL("http://" + IP + "/?" + params[0]);           // URL is formed
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();  // URL Connection

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();     //BufferReader to read
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    result.append(inputLine).append("\n");
                in.close();                 //Closing Connection
                connection.disconnect();
                return result.toString();

            } catch (IOException e) {           // Exception Handling
                e.printStackTrace();
            }
            return null;
        }
    }
}


