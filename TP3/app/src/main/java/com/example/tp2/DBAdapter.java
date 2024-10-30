package com.example.tp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    static final String TABLE_CONTACTS = "contacts";
    static final String KEY_NUMERO = "idContact";
    static final String KEY_NOM = "nom";
    static final String KEY_PRENOM = "prenom";
    static final String KEY_TEL = "tel";
    static final String KEY_ADRESSE = "adresse";
    static final String KEY_CP = "cp";
    static final String KEY_EMAIL = "email";
    static final String KEY_METIER = "metier";
    static final String KEY_SITUATION = "situation";
    static final String CREATE_TABLE_CONTACTS =
            "CREATE TABLE " + TABLE_CONTACTS + " (" +
                    KEY_NUMERO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NOM + " TEXT NOT NULL, " +
                    KEY_PRENOM + " TEXT NOT NULL, " +
                    KEY_TEL + " TEXT NOT NULL, " +
                    KEY_ADRESSE + " TEXT NOT NULL, " +
                    KEY_CP + " NUMBER(5,0) NOT NULL, " +
                    KEY_EMAIL + " TEXT NOT NULL, " +
                    KEY_METIER + " TEXT NOT NULL, " +
                    KEY_SITUATION + " TEXT NOT NULL);";


    static final String TABLE_CHAMPS = "champs";
    static final String KEY_IDCHAMPS = "id";
    static final String KEY_LIBELLE = "libelle";
    static final String KEY_DONNEE = "donnée";
    static final String CREATE_TABLE_CHAMPS =
            "CREATE TABLE " + TABLE_CHAMPS + " (" +
                    KEY_IDCHAMPS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_LIBELLE + " TEXT NOT NULL, " +
                    KEY_DONNEE + " TEXT NOT NULL );";


    static final String TAG = "DBAdapter";
    static final String DATABASE_NAME = "CarnetContactDB";
    static final int DATABASE_VERSION = 1;
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_CONTACTS);
                db.execSQL(CREATE_TABLE_CHAMPS);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHAMPS);
            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    /*méthodes pour la table Contact*/

    public long insertContact(Integer num, String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMERO, num);
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_TEL, tel);
        values.put(KEY_ADRESSE, adresse);
        values.put(KEY_CP, cp);
        values.put(KEY_EMAIL, email);
        values.put(KEY_METIER, metier);
        values.put(KEY_SITUATION, situation);
        return db.insert(DBAdapter.TABLE_CONTACTS, null, values);
    }

    public Cursor getContact(String num) {
        return db.query(DBAdapter.TABLE_CONTACTS, null, DBAdapter.KEY_NUMERO + "=?", new String[]{num}, null, null, null);
    }

    public int updateContact(Integer numAct, Integer numNv, String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMERO, numNv);
        values.put(KEY_NOM, nom);
        values.put(KEY_PRENOM, prenom);
        values.put(KEY_TEL, tel);
        values.put(KEY_ADRESSE, adresse);
        values.put(KEY_CP, cp);
        values.put(KEY_EMAIL, email);
        values.put(KEY_METIER, metier);
        values.put(KEY_SITUATION, situation);
        return db.update(TABLE_CONTACTS, values, KEY_NUMERO + "=?", new String[]{String.valueOf(numAct)});
    }

    public int deleteContact(int num) {
        return db.delete(TABLE_CONTACTS, KEY_NUMERO + "=?", new String[]{String.valueOf(num)});
    }

    /*méthodes pour la table Champs*/

    public long insertChamp(Integer id, String libelle, String donnee) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDCHAMPS, id);
        values.put(KEY_LIBELLE, libelle);
        values.put(KEY_DONNEE, donnee);
        return db.insert(DBAdapter.TABLE_CHAMPS, null, values);
    }

    public Cursor getChamp(String id) {
        return db.query(DBAdapter.TABLE_CHAMPS, null, DBAdapter.KEY_IDCHAMPS + "=?", new String[]{id}, null, null, null);
    }

    public int updateChamp(Integer id, String libelle, String donnee) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDCHAMPS, id);
        values.put(KEY_LIBELLE, libelle);
        values.put(KEY_DONNEE, donnee);
        return db.update(TABLE_CHAMPS, values, KEY_IDCHAMPS + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteChamp(int id) {
        return db.delete(TABLE_CHAMPS, KEY_IDCHAMPS + "=?", new String[]{String.valueOf(id)});
    }
}
