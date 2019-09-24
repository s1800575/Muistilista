package com.example.muistilista_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        closeKeyboard();


        TietokantaOperointi dbHelper = new TietokantaOperointi(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(mAdapter);

        // () - mm. suunnat pyyhkäisylle

        new ItemTouchHelper (new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                poistaTuote((long) viewHolder.itemView.getTag());


            }
        // tämä kiinnittää ItemTouchHelperin tähän kyseiseen recycleViewhin

        }).attachToRecyclerView(recyclerView);


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

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void lisaa() {

        // tehdään tarkastus lisättävälle tuotteelle

        if (lisattavaAsia.getText().toString().contains("*") || lisattavaAsia.getText().toString().contains("%")) {
            Toast toast = Toast.makeText(this, "Lisättävä tuote sisältää kiellettyjä merkkejä ja ohjelma keskeytetään", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
            toast.show();
            finish();

        }

            //if (lisattavaAsia.getText().toString().trim().length() == 0 || maara == 0) {

            if (lisattavaAsia.getText().toString().trim().length() == 0) {
                Toast toast = Toast.makeText(this, "Anna lisättävä tuote", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                return;

            }

            if (maara == 0) {
                Toast toast = Toast.makeText(this, "Anna lisattavä määrä", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
                toast.show();
                return;

            }

        //    return;
        // }

        String nimi = lisattavaAsia.getText().toString();
        ContentValues cv = new ContentValues();
        cv.put(TuotteetTauluun.TuotteenLisays.COLUMN_NIMI, nimi);
        cv.put(TuotteetTauluun.TuotteenLisays.COLUMN_MAARA, maara);

        mDatabase.insert(TuotteetTauluun.TuotteenLisays.TAULUN_NIMI, null, cv);
        mAdapter.swapCursor(getAllItems());

        closeKeyboard();
        lisattavaAsia.getText().clear();
        maara = 0;
        lisattavaMaara.setText(String.valueOf(maara));

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

    private void poistaTuote(long id) {
        mDatabase.delete(TuotteetTauluun.TuotteenLisays.TAULUN_NIMI,
                TuotteetTauluun.TuotteenLisays._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());


        new AlertDialog.Builder(this)
                .setTitle("Tuote poistettiin")
                .setMessage("Tuote poistettiin tietokannasta "+TietokantaOperointi.DATABASE_NAME+" versio "+TietokantaOperointi.DATABASE_VERSION)
                .setPositiveButton(android.R.string.ok, null)
                .show();

    }


}
