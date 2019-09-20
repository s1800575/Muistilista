package com.example.muistilista_1;

import android.provider.BaseColumns;

public class TuotteetTauluun {

    private TuotteetTauluun() {}

    public static final class TuotteenLisays implements BaseColumns {
        public static final String TAULUN_NIMI = "tuoteLista";
        public static final String COLUMN_NIMI = "nimi";
        public static final String COLUMN_MAARA = "maara";
        public static final String COLUMN_AIKA = "timestamp";


    }
}
