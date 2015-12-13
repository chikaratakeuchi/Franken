package oddtimeworks.com.franken;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Activity08 extends Activity {

    String num = "08";

    LinearLayout layout = null;

    ArrayList<String> fileList = null;
    Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        res = getResources();

        setContentView(res.getIdentifier("screen" + num, "layout", getPackageName()));

        initialize();
    }

    private void initialize() {
        layout = (LinearLayout)findViewById(res.getIdentifier("screen" + num, "id", getPackageName()));

        CreateScreen createScreen = new CreateScreen(num, this, layout, Activity08.this);
    }
}