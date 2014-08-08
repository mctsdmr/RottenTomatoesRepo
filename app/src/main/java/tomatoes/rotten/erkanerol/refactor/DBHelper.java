package tomatoes.rotten.erkanerol.refactor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import BackEnd.Container;
import BackEnd.Movie;

/**
 * Created by erkanerol on 8/5/14.
 */
public class DBHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "Favorites";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME="favorites";
    String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ( object BLOB )";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
           db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS books");
        this.onCreate(db);
    }


    public ArrayList<Movie> read(){

        SQLiteDatabase db = this.getWritableDatabase();


        String query = "SELECT  * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        Log.v("cursor",cursor.getColumnName(0).toString());
        if(cursor.getCount()>0){
            cursor.moveToFirst();
            byte[] serialized=cursor.getBlob(0);
            Container container=(Container)deserializeObject(serialized);

            return container.movies;
        }
        else{
            return new ArrayList<Movie>();
        }

    }

    public void write(ArrayList<Movie> movie){

        Container container=new Container(movie);
        byte[] serialized=serializeObject(container);

        ContentValues content=new ContentValues();
        content.put("object",serialized);

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.insert(TABLE_NAME,null,content);

    }


    public byte[] serializeObject(Object o) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(o);
            out.close();

            // Get the bytes of the serialized object
            byte[] buf = bos.toByteArray();

            return buf;
        } catch(IOException ioe) {
            Log.e("serializeObject", "error", ioe);

            return null;
        }
    }

    public Object deserializeObject(byte[] b) {
        try {
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(b));
            Object object = in.readObject();
            in.close();

            return object;
        } catch(ClassNotFoundException cnfe) {
            Log.e("deserializeObject", "class not found error", cnfe);

            return null;
        } catch(IOException ioe) {
            Log.e("deserializeObject", "io error", ioe);

            return null;
        }
    }
}
