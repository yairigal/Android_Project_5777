package project.android.com.android5777_9254_6826.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class service extends Service {
    Backend db;
    private final int timeToSleep = 10000;
    Thread background;

    public service() {
        db = FactoryDatabase.getDatabase();
        running = true;

        background = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    checkForChange();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    stopCheck();
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        background.start();
        //Toast.makeText(getApplicationContext(),"hi2",Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }

    public void onDestroy() {
        super.onDestroy();
        stopCheck();
    }

    boolean running = true;

    /**
     * The thread function , this function checks from a new database update and send a
     * broadcast.
     * @throws InterruptedException if there was an error from the database.
     */
    private void checkForChange() throws InterruptedException {
        while (running) {
            if (db.ifNewAttractionAdded() || db.ifNewBusinessAdded()) {
                Log.d("service","broadcast send");
                broadcastIntent();
            }
            Log.d("service: ", "running");
            Thread.sleep(timeToSleep);
        }
    }
    /**
     * Broadcast an intent for the broadcast receiver to catch
     */
    public void broadcastIntent() {
        Intent intent = new Intent();
        intent.setAction("com.project.CHECK_DATABASE");
        sendBroadcast(intent);
        Log.d("service: ", "broadcast sent");
    }

    /**
     * Stops the service
     */
    private void stopCheck() {
        running = false;
    }
}
