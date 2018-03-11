package blogspot.justsimpleinfo.com.androidwebserver;

import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Lauro-PC on 3/9/2018.
 */

public class WebFileHandler {

    public static final String HTML_MIME_TYPE = "text/html";
    public static final String JAVASCRIPT_MIME_TYPE = "application/javascript";
    public static final String CSS_MIME_TYPE = "text/css";
    public static final String FILE_MIME_TYPE = "application/octet-stream";

    public static String detectMimeType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;

        } else if (fileName.endsWith(".html")) {

            return HTML_MIME_TYPE;
        } else if (fileName.endsWith(".js")) {


            return JAVASCRIPT_MIME_TYPE;

        } else if (fileName.endsWith(".css")) {

            return CSS_MIME_TYPE;

        } else {

            return FILE_MIME_TYPE;

        }
    }

    /**
     * get File
     * @param fileName
     * @param mAssetManager
     * @return
     */
    public static byte[] getFile(String fileName,AssetManager mAssetManager){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream input = null;

        try {

            String htmlReponse = "";

            if(WebFileHandler.detectMimeType(fileName) == HTML_MIME_TYPE){
                /**
                 * html file is requested
                 */
                String indexHtmlFile = WebFileHandler.readHtmlFileString("index.html",mAssetManager,input);
                String htmlRequested = WebFileHandler.readHtmlFileString(fileName,mAssetManager,input);

                htmlReponse = indexHtmlFile.replaceAll("<!--CONTENT HERE IF NOT HOME!-->",htmlRequested);

                return htmlReponse.getBytes();
            }

            /**
             * if not html file requested
             */
            input = mAssetManager.open(fileName);
            byte[] buffer = new byte[1024];
            int size;
            while (-1 != (size = input.read(buffer))) {
                byteArrayOutputStream.write(buffer, 0, size);
            }
            byteArrayOutputStream.flush();
            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();

            return null;

        }finally {

            try{

                if(input != null){
                    input.close();
                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }


    }

    /**
     * Read html file
     * @param fileName
     * @param mAssetManager
     * @param inputStream
     * @return
     * @throws IOException
     */
    protected static String readHtmlFileString(String fileName,AssetManager mAssetManager, InputStream inputStream) throws IOException {
        inputStream = mAssetManager.open(fileName);
        BufferedReader indexBufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder htmlStringBuilder = new StringBuilder();
        String htmlFileString;
        while ((htmlFileString = indexBufferedReader.readLine()) != null) {
            htmlStringBuilder.append(htmlFileString).append('\n');
        }

        return htmlStringBuilder.toString();
    }

}
