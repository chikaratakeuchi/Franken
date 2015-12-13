package oddtimeworks.com.franken;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

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
}