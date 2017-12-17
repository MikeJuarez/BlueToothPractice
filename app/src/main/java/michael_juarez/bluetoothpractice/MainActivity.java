package michael_juarez.bluetoothpractice;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    Button mBTOnOff;
    BluetoothAdapter mBluetoothAdapter;
    TextView mainActivityStatusTextView;

    // Create a BroadcastReceiver for ACTION_FOUND.
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);

                String message = "";
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        message = "State Off";
                        Log.d(TAG, "onReceive: State OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        message = "State Turning Off";
                        Log.d(TAG, "onReceive: STATE_TURNING_OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        message = "State On";
                        Log.d(TAG, "onReceive: STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        message = "State Turning On";
                        Log.d(TAG, "onReceive: STATE_TURNING_ON");
                        break;
                }

                mainActivityStatusTextView.setText(message);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBTOnOff = (Button) findViewById(R.id.mainActivityButtonOnOff);
        mainActivityStatusTextView = (TextView) findViewById(R.id.mainActivityStatusTextView);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mBTOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableDisableBT();
            }
        });
    }

    private void enableDisableBT() {
        if (mBluetoothAdapter == null)
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        } else if (mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.disable();
            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mReceiver, BTIntent);
        }
    }


}
