package project.android.com.second_app.controller;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import project.android.com.second_app.R;

public class MainActivity extends AppCompatActivity {

    TextView tv = null;
    Cursor account = null;
    Uri uri = Uri.parse("content://project.android.com.android5777_9254_6826.model.backend.Provider"+"/Accounts");
    ContentResolver provider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button broadcast = (Button) findViewById(R.id.button);
        tv = (TextView) findViewById(R.id.textView);
        provider = getContentResolver();
        broadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                func();
            }
        });
    }

    public void func() {

        try {
            new AsyncTask<Void,Void,Void>(){
                @Override
                protected Void doInBackground(Void... params) {
                    account = provider.query(uri,null,null,null,null);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }
            }.execute();

        } catch (Exception e) {
            Log.d("Error",e.getMessage());
        }

    }

    public void broadcastIntent(View view) {
        Intent intent = new Intent();
        intent.setAction("com.project.CHECK_DATABASE");
        sendBroadcast(intent);
    }
}
