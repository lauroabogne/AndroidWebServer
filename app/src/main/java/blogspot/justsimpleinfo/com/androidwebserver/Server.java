package blogspot.justsimpleinfo.com.androidwebserver;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by Lauro-PC on 3/9/2018.
 */

public class Server implements Runnable {

    private final static String TAG = "Server";
    private boolean doServerRunning = false;
    private int mPort = 4000;
    private ServerSocket mServerSocket;
    BrowserRequestHandler mBrowserRequestHandler;
    ServerListener mServerListener;

    Server(int port, Context context,ServerListener serverListener){

        this.mPort = port;
        this.mBrowserRequestHandler = new BrowserRequestHandler(context);
        this.mServerListener = serverListener;
    }

    Server(Context context,ServerListener serverListener){

        mBrowserRequestHandler = new BrowserRequestHandler(context);
        this.mServerListener = serverListener;
    }

    public void startServer(int port){

        mPort = port;

        if(doServerRunning){
            /**
             * if server is running do nothing
             */
            return;
        }
        /**
         * start thread to start server listening to client
         */
        doServerRunning = true;
        new Thread(this).start();


    }

    public void startServer(){

        if(doServerRunning){
            /**
             * if server is running do nothing
             */
            return;
        }
        /**
         * start thread to start server listening to client
         */
        doServerRunning = true;
        new Thread(this).start();


    }
    public void stopServer(){


            try {

                doServerRunning = false;

                    if (mServerSocket != null) {
                        mServerSocket.close();
                        mServerSocket = null;

                    }


            } catch (Exception e) {

                Log.e(TAG, "Erro stoping server ", e);

            }finally {

                mServerListener.onStop("stop");

            }



    }

    @Override
    public void run() {




        try {

            this.mServerListener.onStart(getLocalIpAddress(),mPort);
            mServerSocket = new ServerSocket(mPort);
            while (doServerRunning) {

                Socket socket = mServerSocket.accept();
                mBrowserRequestHandler.handlerBrowserRequest(socket);

                socket.close();


            }



        } catch (SocketException e) {
            Log.e(TAG, "Server has error", e);
        } catch (IOException e) {
            Log.e(TAG, "Server has error", e);
        } catch (Exception e) {
            Log.e(TAG, "Server has error.", e);
        }



    }
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    interface ServerListener{
        void onStart(String ipAddress, int port);
        void onStop(String reason);
    }
}
