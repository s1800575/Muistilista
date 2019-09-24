package com.example.muistilista_1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.muistilista_1.TuotteetTauluun.*;

import androidx.annotation.Nullable;

public class TietokantaOperointi extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "tuotelista2.db";
    public static int DATABASE_VERSION = 1;

    public TietokantaOperointi(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION = 1);
    }

    @Override
    public void onCreate(SQLiteDatabase tietokanta) {

        final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                TuotteetTauluun.TuotteenLisays.TAULUN_NIMI + " (" +
                TuotteetTauluun.TuotteenLisays._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TuotteetTauluun.TuotteenLisays.COLUMN_NIMI + " TEXT NOT NULL, " +
                TuotteetTauluun.TuotteenLisays.COLUMN_MAARA + " INTEGER NOT NULL, " +
                TuotteetTauluun.TuotteenLisays.COLUMN_AIKA + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        tietokanta.execSQL(SQL_CREATE_GROCERYLIST_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase tietokanta, int vanhaVersio, int uusiVersio) {
        tietokanta.execSQL("DROP TABLE IF EXISTS " + TuotteenLisays.TAULUN_NIMI);
        onCreate(tietokanta);

    }
}
