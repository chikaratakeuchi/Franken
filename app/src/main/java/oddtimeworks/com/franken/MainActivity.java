package oddtimeworks.com.franken;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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


        setUiOperation();
    }

    private void downloadFiles() {
        //task.execute(spinner.getSelectedItem().toString());
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.execute(spinner.getSelectedItem().toString());
    }

    private void setUiOperation() {
        editText = (EditText)findViewById(R.id.editText);
        textView2 = (TextView)findViewById(R.id.textView2);
        spinner = (Spinner)findViewById(R.id.spinner);
        button = (Button)findViewById(R.id.button);

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
            editText.setText(url);
        }catch( IOException e ){
            editText.setText("");
        }

        //editText.addTextChangedListener(watchHandler);
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    setSpinner();

                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
                }
                return false;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadFiles();
            }
        });

        setSpinner();
    }

    private void setSpinner() {
        //task.execute("dir");
        AsyncHttpRequest task = new AsyncHttpRequest(this);
        task.execute("dir");
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
            //setSpinner();
        }
    };

    private class AsyncHttpRequest extends AsyncTask<String, Void, String> {

        private Activity mainActivity;

        public AsyncHttpRequest(Activity activity) {
            this.mainActivity = activity;
        }

        @Override
        protected String doInBackground(String... params) {
            //return doPost(params[0]);

            Log.d("EVENT","called doPost");
            Log.d("EVENT",params[0]);
            StringBuilder responseJSON = new StringBuilder();

            String url = "";
            try{
                //Log.d("EVENT","url data saving");
                FileInputStream in = openFileInput( "url.data" );
                BufferedReader reader = new BufferedReader( new InputStreamReader( in , "UTF-8") );
                url = "";
                String tmp;
                while( (tmp = reader.readLine()) != null ){
                    url = url + tmp + "\n";
                }
                reader.close();
                //Log.d("EVENT","url data saved");
            }catch( IOException e ){
                url = "https://jedi.imodeip3.nttdocomo.co.jp/provider/uiux/takeuchi/prototype/franken.php";
                e.printStackTrace();
            }
            String requestJSON = "{\"param\":\"" + params[0] + "\"}";
            //Log.d("EVENT",requestJSON);

            HttpURLConnection conn = null;

            try{
                //Log.d("EVENT","start access");
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

                //Log.d("EVENT","end access");
            }catch(IOException e) {
                e.getStackTrace();
            }finally{
                if(conn != null){
                    conn.disconnect();
                }
            }

            //Log.d("EVENT",responseJSON.toString());

            return responseJSON.toString();
        }

        protected void onPostExecute(String responseJSON) {
            Log.d("EVENT",responseJSON);

            // レスポンスにdirが含まれていたらディレクトリ一覧と判断し、
            // レスポンスの中身をスピナーに設定する。
            ArrayAdapter adapter = new ArrayAdapter(mainActivity, android.R.layout.simple_spinner_item);
            try{
                JSONObject json = new JSONObject(responseJSON);
                JSONArray dirs = json.getJSONArray("dirs");

                Log.d("EVENT","dirs length = " + dirs.length());

                for(int i=1;i<dirs.length();i++){
                    JSONObject jsonObject = dirs.getJSONObject(i);
                    Log.d("EVENT",dirs.getJSONObject(i).getString("name"));
                    //fileManager.addFile(files.getJSONObject(i).getString("name"));
                    adapter.add(dirs.getJSONObject(i).getString("name"));
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                spinner.setAdapter(adapter);
            }catch(JSONException e){
                Log.d("EVENT","json parse failed");
            }

            // レスポンスの中にdirがなければファイル一覧と判断し、
            // ファイルをサーバから取得する。
            try{
                JSONObject json = new JSONObject(responseJSON);
                JSONArray files = json.getJSONArray("files");

                Log.d("EVENT","files length = " + files.length());

                FileManager fileManager = FileManager.getInstance();
                fileManager.initialze();

                for(int i=1;i<files.length();i++){
                    JSONObject jsonObject = files.getJSONObject(i);
                    Log.d("EVENT",files.getJSONObject(i).getString("name"));
                    fileManager.addFile(files.getJSONObject(i).getString("name"));
                }
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

                spinner.setAdapter(adapter);

                Intent i = new Intent();
                i.setClassName("oddtimeworks.com.franken","oddtimeworks.com.franken.Activity01");
                mainActivity.startActivity(i);
            }catch(JSONException e){
                Log.d("EVENT","json parse failed");
            }
        }
    }
}