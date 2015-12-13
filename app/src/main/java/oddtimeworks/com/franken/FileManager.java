package oddtimeworks.com.franken;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by takeuchichikara on 2015/12/13.
 */
public class FileManager {

    private static FileManager instance = null;

    private ArrayList<String> fileList01 = null;
    private ArrayList<String> fileList02 = null;
    private ArrayList<String> fileList03 = null;
    private ArrayList<String> fileList04 = null;
    private ArrayList<String> fileList05 = null;
    private ArrayList<String> fileList06 = null;
    private ArrayList<String> fileList07 = null;
    private ArrayList<String> fileList08 = null;
    private ArrayList<String> fileList09 = null;
    private ArrayList<String> fileList10 = null;

    private FileManager() {}

    public static FileManager getInstance() {
        if(instance == null) {
            instance = new FileManager();
        }
        return instance;
    }

    public void initialze() {
        fileList01 = new ArrayList<String>();
        fileList02 = new ArrayList<String>();
        fileList03 = new ArrayList<String>();
        fileList04 = new ArrayList<String>();
        fileList05 = new ArrayList<String>();
        fileList06 = new ArrayList<String>();
        fileList07 = new ArrayList<String>();
        fileList08 = new ArrayList<String>();
        fileList09 = new ArrayList<String>();
        fileList10 = new ArrayList<String>();
    }

    public void addFile(String filename) {
        try {
            if (filename.matches("01.*")) {
                fileList01.add(filename);
            } else if (filename.matches("02.*")) {
                fileList02.add(filename);
            } else if (filename.matches("03.*")) {
                fileList03.add(filename);
            } else if (filename.matches("04.*")) {
                fileList04.add(filename);
            } else if (filename.matches("05.*")) {
                fileList05.add(filename);
            } else if (filename.matches("06.*")) {
                fileList06.add(filename);
            } else if (filename.matches("07.*")) {
                fileList07.add(filename);
            } else if (filename.matches("08.*")) {
                fileList08.add(filename);
            } else if (filename.matches("09.*")) {
                fileList09.add(filename);
            } else if (filename.matches("10.*")) {
                fileList10.add(filename);
            }
        }catch(Exception e) {
            e.printStackTrace();
            Log.d("ERROR","ぬるぽ");
        }
    }

    public ArrayList<String> getFileList(String screen_num) {
        switch(screen_num) {
            case "01":
                return fileList01;
            case "02":
                return fileList02;
            case "03":
                return fileList03;
            case "04":
                return fileList04;
            case "05":
                return fileList05;
            case "06":
                return fileList06;
            case "07":
                return fileList07;
            case "08":
                return fileList08;
            case "09":
                return fileList09;
            case "10":
                return fileList10;
            default:
                return fileList01;
        }
    }

}
