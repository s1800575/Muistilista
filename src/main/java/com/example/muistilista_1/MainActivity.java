package com.example.muistilista_1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase mDatabase;
    private GroceryAdapter mAdapter;
    private EditText lisattavaAsia;
    private TextView lisattavaMaara;
    private int maara = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TietokantaOperointi dbHelper = new TietokantaOperointi(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);


        lisattavaAsia = findViewById(R.id.lisattavaAsia);
        lisattavaMaara = findViewById(R.id.lisattavaMaara);

        Button lisaaTuote = findViewById(R.id.lisaaTuote);
        Button nappiVahenna = findViewById(R.id.nappiVahenna);
        Button nappiLisaa = findViewById(R.id.nappiLisaa);


        nappiLisaa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kasvata();
            }
        });

        nappiVahenna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vahenna();
            }
        });

        lisaaTuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lisaa();
            }
        });
    }

    private void kasvata() {
        maara++;
        lisattavaMaara.setText(String.valueOf(maara));
    }

    private void vahenna() {
        if (maara > 0) {
            maara--;
            lisattavaMaara.setText(String.valueOf(maara));
        }
    }

    private void lisaa() {

        if (lisattavaAsia.getText().toString().trim().length() == 0 || maara == 0) {
            return;
        }

        String nimi = lisattavaAsia.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(TuotteetTauluun.TuotteenLisays.COLUMN_NIMI, nimi);
        cv.put(TuotteetTauluun.TuotteenLisays.COLUMN_MAARA, maara);

        mDatabase.insert(TuotteetTauluun.TuotteenLisays.TAULUN_NIMI, null, cv);
        mAdapter.swapCursor(getAllItems());

        lisattavaAsia.getText().clear();
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                TuotteetTauluun.TuotteenLisays.TAULUN_NIMI,
                null,
                null,
                null,
                null,
                null,
                TuotteetTauluun.TuotteenLisays.COLUMN_AIKA + " DESC"
        );
    }

}
