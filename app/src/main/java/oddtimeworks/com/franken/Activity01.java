package oddtimeworks.com.franken;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Activity01 extends Activity {

    String num = "01";
    String path;

    LinearLayout layout = null;

    ArrayList<String> fileList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.screen01);

        initialize();
    }

    private void initialize() {
        FileManager fileManager = FileManager.getInstance();
        fileManager.initialze();

        fileManager.addFile("0101.png");
        fileManager.addFile("0102_02.png");
        fileManager.addFile("0103_03.png");
        fileManager.addFile("0201_03.png");
        fileManager.addFile("0202.png");
        fileManager.addFile("0203.png");
        fileManager.addFile("0204_01.png");
        fileManager.addFile("0301.png");

        layout = (LinearLayout)findViewById(R.id.screen01);

        CreateScreen createScreen = new CreateScreen(num, this, layout, Activity01.this);
    }

    private void doPost() {
        String url = "http://jedi.imodeip3.nttdocomo.co.jp/provider/uiux/takeuchi/prototype/filelist.php";
        String requestJSON = "{'param','dir'}";

        HttpURLConnection conn = null;

        try{
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(requestJSON.getBytes().length);
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            conn.connect();

            DataOutputStream os = new DataOutputStream(conn.getOutputStream());
            os.write(requestJSON.getBytes("UTF-8"));
            os.flush();
            os.close();

            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                StringBuffer responseJSON = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while((inputLine = reader.readLine()) != null){
                    responseJSON.append(inputLine);
                }
            }


        }catch(IOException e) {

        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }



    }
}