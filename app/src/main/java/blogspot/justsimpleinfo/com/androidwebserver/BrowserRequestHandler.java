package blogspot.justsimpleinfo.com.androidwebserver;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Date;

/**
 * Created by Lauro-PC on 3/9/2018.
 */

public class BrowserRequestHandler {

    private String TAG = "BrowserRequestHandler";
    private AssetManager mAssetManager;
    private Context mContext;

    public BrowserRequestHandler(Context context){
        this.mContext = context;
        this.mAssetManager = context.getResources().getAssets();
    }
    public void handlerBrowserRequest(Socket socket){

        PrintStream output = null;
        BufferedReader reader = null;

        try{


            String route = null;

            // Read HTTP headers and get url requested
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;

            while (!TextUtils.isEmpty(line = reader.readLine())) {

                if (line.startsWith("GET /")) {

                    int start = line.indexOf('/') + 1;
                    int end = line.indexOf(' ', start);
                    route = line.substring(start, end);
                    break;
                }
            }


            byte[] contentBytes;

            if(route == null || route.isEmpty()){
                /**
                 * if route is empty serve home.html
                 */
                route = "home.html";

            }

            /**
             *
             */
            route = processUrl(route);


            if(route.startsWith("news") || route.startsWith("contact") || route.startsWith("about")){


                route = route+".html";

                contentBytes = WebFileHandler.getFile(route,mAssetManager);

            }else{

                contentBytes = WebFileHandler.getFile(route,mAssetManager);
            }


            output = new PrintStream(socket.getOutputStream());

            /**
             * set header and content type
             */
            output.println("HTTP/1.0 200 OK");
            output.println("Content-Type:"+ WebFileHandler.detectMimeType(route));


            output.println();
            output.write(contentBytes);
            output.flush();

        }catch (Exception e){

            try {
                if (null != output) {
                    output.close();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }


    }

    /**
     *
     * @param url
     * @return
     */
    private String processUrl(String url){
        String urlTrim = url.trim();
        String splitedURL[] = urlTrim.split("/");

        if(splitedURL.length >= 2 &&(urlTrim.endsWith(".js") || urlTrim.endsWith(".css")) ){


            return splitedURL[splitedURL.length - 1];

        }

        return splitedURL[0];

    }
}
