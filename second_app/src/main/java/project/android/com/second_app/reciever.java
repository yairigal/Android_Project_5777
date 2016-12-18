package project.android.com.second_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class reciever extends BroadcastReceiver {
    public reciever() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Toast.makeText(context,"Intent Received",Toast.LENGTH_SHORT).show();
        Log.d("second app: ", "broadcast received");
        context.startActivity(new Intent(context,MainActivity.class));
    }
}
