package project.android.com.second_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class reciever extends BroadcastReceiver {
    public reciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,"Intent Recieved",Toast.LENGTH_SHORT).show();
    }
}
