package oddtimeworks.com.franken;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by takeuchichikara on 2015/12/13.
 */
public class CreateScreen {

    Context context;
    String num;
    LinearLayout layout = null;
    Activity activity;

    ArrayList<String> fileList = null;

    public CreateScreen(String num, Context context, LinearLayout layout, Activity activity) {
        this.num = num;
        this.context = context;
        this.layout = layout;
        this.activity = activity;

        initialize();
    }

    private void initialize() {

        FileManager fileManager = FileManager.getInstance();

        fileList = fileManager.getFileList(num);

        for(String filename : fileList) {
            ImageView imageView = new ImageView(context);
            imageView.setAdjustViewBounds(true);

            try {
                ImageGetTask task = new ImageGetTask(layout);
                task.execute("http://bctake6.s323.xrea.com/prototype/test1/", filename);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }


    }

    class ImageGetTask extends AsyncTask<String,Void,Bitmap> {
        private LinearLayout layout;
        private ImageButton imageButton;
        private String filename;
        private Intent i;

        public ImageGetTask(LinearLayout layout) {
            this.layout = layout;
            imageButton = new ImageButton(context);

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap image;
            filename = params[1];

            try {
                URL imageUrl = new URL(params[0] + params[1]);
                InputStream imageIs;
                imageIs = imageUrl.openStream();
                image = BitmapFactory.decodeStream(imageIs);

                return image;
            } catch (MalformedURLException e) {
                return null;
            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageButton.setImageBitmap(result);
            imageButton.setAdjustViewBounds(true);
            imageButton.setPadding(0,0,0,0);
            imageButton.setBackgroundColor(Color.TRANSPARENT);

            if(filename.matches(".*_.*")){
                i = new Intent();
                i.setClassName("oddtimeworks.com.franken","oddtimeworks.com.franken.Activity" + filename.substring(5,7));

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(i);
                    }
                });
            }

            layout.addView(imageButton);
        }
    }
}
