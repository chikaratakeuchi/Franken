package oddtimeworks.com.franken;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
public class MainActivity extends Activity {

    String num = "01";
    String path;

    LinearLayout layout = null;
    EditText editText;
    TextView textView2;
    Spinner spinner;
    Button button;

    ArrayList<String> fileList = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.main);

        //initialize();
        setUiOperation();
    }

    private void downloadFiles() {
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
    }

    private void setUiOperation() {
        editText = (EditText)findViewById(R.id.editText);
        textView2 = (TextView)findViewById(R.id.textView2);
        spinner = (Spinner)findViewById(R.id.spinner);
        button = (Button)findViewById(R.id.button);

        editText.addTextChangedListener(watchHandler);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";
                try{
                    str = editText.getText().toString();
                    FileOutputStream out = openFileOutput("url.data", MODE_PRIVATE);
                    out.write(str.getBytes());
                }catch( IOException e ){
                    e.printStackTrace();
                }

                str = "";



                doPost((String) spinner.getSelectedItem());

                Log.d("LOCAL DATA",str);
            }
        });


    }

    private String doPost(String param) {
        StringBuilder responseJSON = new StringBuilder();;

        String url = "";
        try{
            FileInputStream in = openFileInput( "url.data" );
            BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
            url = "";
            String tmp;
            while( (tmp = reader.readLine()) != null ){
                url = url + tmp + "\n";
            }
            reader.close();
        }catch( IOException e ){
            e.printStackTrace();
        }
        //String url = "http://jedi.imodeip3.nttdocomo.co.jp/provider/uiux/takeuchi/prototype/filelist.php";
        String requestJSON = "{'param',param}";

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while((inputLine = reader.readLine()) != null){
                    responseJSON.append(inputLine);
                }
            }


        }catch(IOException e) {
            e.getStackTrace();
        }finally{
            if(conn != null){
                conn.disconnect();
            }
        }

        return responseJSON.toString();
    }

    private TextWatcher watchHandler = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            String responseJSON = doPost("dir");
            try{
                JSONObject json = new JSONObject(responseJSON);
            }catch(JSONException e){

            }


            ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item);
            adapter.add("Sample0");
            adapter.add("Sample1");
            adapter.add("Sample2");
            adapter.add("Sample3");
            adapter.add("Sample4");
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

            spinner.setAdapter(adapter);
        }
    };
}