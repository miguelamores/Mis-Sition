package com.example.miguelamores.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by miguelamores on 7/16/14.
 */
public class SQLHelper extends SQLiteOpenHelper {
    public SQLHelper(Context context) {
        super(context, "Sit2.sqlite", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table sitio(_id integer primary key autoincrement," +
                "nombre text, foto text, direccion text, email text,"+
                "telefono text, valoracion text, comentarios text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
