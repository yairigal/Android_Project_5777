package project.android.com.android5777_9254_6826.model.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import project.android.com.android5777_9254_6826.controller.LoginActivity;

/**
 * Created by Yair on 2016-12-20.
 */

public abstract class LoadingTask<A,B,C> extends AsyncTask<A,B,C> {

    ProgressDialog progDailog;
    @Override
    protected C doInBackground(A... params) {
        C c = backgroundAction();
        return c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        showLoadingCircle(getApplicationContext());
        preAction();
    }

    private void showLoadingCircle(Context ctx) {
        progDailog = new ProgressDialog(ctx);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }

    @Override
    protected void onProgressUpdate(B... values) {
        super.onProgressUpdate(values);
        onProgress();
    }

    @Override
    protected void onPostExecute(C aVoid) {
        super.onPostExecute(aVoid);
        progDailog.dismiss();
        postAction();
    }

    protected abstract C backgroundAction(A...params);
    protected abstract void onProgress();
    protected abstract void preAction();
    protected abstract void postAction();
    protected abstract Context getApplicationContext();
}
