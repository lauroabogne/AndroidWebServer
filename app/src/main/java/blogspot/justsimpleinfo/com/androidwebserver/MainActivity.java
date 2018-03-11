package blogspot.justsimpleinfo.com.androidwebserver;

import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,Server.ServerListener {


    TextView mStatusTextView;
    EditText mPortEditText;
    Button mStartServerButton;
    Button mStopServerButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.mStatusTextView = this.findViewById(R.id.status_textview);
        this. mPortEditText = this.findViewById(R.id.port_edittext);
        this.mStartServerButton = this.findViewById(R.id.start_server_btn);
        this.mStartServerButton.setOnClickListener(this);

        this.mStopServerButton = this.findViewById(R.id.stop_server_btn);
        this.mStopServerButton.setOnClickListener(this);

    }

    Server mServer;


    @Override
    public void onClick(View view) {



        if(mServer ==null){


            mServer =  new Server(this,this);

        }

        if(view.getId() == R.id.start_server_btn){

           mServer.startServer();

        }else{
            mServer.stopServer();
        }



    }


    @Override
    public void onStart(final String ipAddress, final int port) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mStatusTextView.setGravity(Gravity.CENTER);
                mStatusTextView.setText("Running\r\n"+ipAddress+":"+port);
            }
        });
    }

    @Override
    public void onStop(String reason) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                mStatusTextView.setGravity(Gravity.CENTER);
                mStatusTextView.setText("Not Running");
            }
        });

    }
}
