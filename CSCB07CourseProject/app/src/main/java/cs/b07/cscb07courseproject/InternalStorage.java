package cs.b07.cscb07courseproject;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import applicationsystem.Constants;
import database.Database;

/**
 * Created by Mathu on 2016-11-29.
 */

public class InternalStorage {

    private static String datapath = Constants.DATAPATH;

    public static void saveData(Context context, Database database) throws IOException {
        FileOutputStream fileOutputStream = context.openFileOutput(datapath, Activity.MODE_PRIVATE);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(database);
        fileOutputStream.getFD().sync();
        objectOutputStream.close();
        fileOutputStream.close();

    }

    public static Database loadData(Context context) throws ClassNotFoundException {

        Database database = null;
        try {
            FileInputStream fileInputStream = context.getApplicationContext().openFileInput(datapath);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            database = (Database) objectInputStream.readObject();
        } catch (IOException e) {
            database = new Database();
        }
        if (database == null) {
            database = new Database();
        }
        return database;
    }
}
