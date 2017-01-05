package project.android.com.android5777_9254_6826.controller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import project.android.com.android5777_9254_6826.R;

/**
 * Created by Yair on 2017-01-05.
 */

public class StaticDeclarations {
    public static ProgressDialog progressDialog = null;
    public static void showProgress(Context ctx,String msg){
        if(progressDialog != null) {
            //wait until end.
            while(progressDialog.isShowing()){}
        }
        else
        {
            progressDialog = new ProgressDialog(ctx);
        }

        progressDialog = new ProgressDialog(ctx);
        progressDialog.setMessage(msg);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public static void hideProgress(){
        if(progressDialog != null){
            if(progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }

    //TODO set the activity of splash screen instead of the normal loading bar
    public static void showLoadingScreen(Context From,String toShow){
        Intent inte = new Intent(From,SplashScreen.class);
        inte.putExtra("text",toShow);
        From.startActivity(inte);
    }
    public static void hideLoadingScreen(){
        SplashScreen.hideSplashScreen();
    }

}
