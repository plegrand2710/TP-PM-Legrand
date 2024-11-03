package com.example.tp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

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
    static final String KEY_MINIATURE = "miniature";
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
                    KEY_SITUATION + " TEXT NOT NULL, " +
                    KEY_MINIATURE + " INTEGER NOT NULL);";


    static final String TABLE_CHAMPS = "champs";
    static final String KEY_IDCHAMPS = "id";
    static final String KEY_LIBELLE = "libelle";
    static final String KEY_DONNEE = "donnée";
    static final String CREATE_TABLE_CHAMPS =
            "CREATE TABLE " + TABLE_CHAMPS + " (" +
                    KEY_IDCHAMPS + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_LIBELLE + " TEXT NOT NULL, " +
                    KEY_DONNEE + " TEXT NOT NULL );";

    static final String TABLE_CC = "contact_champ";
    static final String KEY_IDTUPLE = "idTuple";
    static final String KEY_NUMCONTACT = "num";
    static final String KEY_IDCHAMP = "idChamp";
    static final String CREATE_TABLE_CC =
            "CREATE TABLE " + TABLE_CC + " (" +
                    KEY_IDTUPLE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NUMCONTACT + " INTEGER NOT NULL, " +
                    KEY_IDCHAMP + " INTEGER NOT NULL );";

    static final String TAG = "annuaire";
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
                db.execSQL(CREATE_TABLE_CC);
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
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CC);
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


    public List<String> getTableColumns(String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ");", null);

            if (cursor != null && cursor.moveToFirst()) {
                int nameIndex = cursor.getColumnIndex("name");
                do {
                    String columnName = cursor.getString(nameIndex);
                    columns.add(columnName);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columns;
    }


    public long insertContact(Integer num, String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, String miniature) {
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
        values.put(KEY_MINIATURE, miniature);
        return db.insert(DBAdapter.TABLE_CONTACTS, null, values);
    }

    public long insertContact(Contact contact) {
        ContentValues values = contact.toContentValues();
        return db.insert(TABLE_CONTACTS, null, values);
    }

    public Cursor getContact(String num) {
        return db.query(DBAdapter.TABLE_CONTACTS, null, DBAdapter.KEY_NUMERO + "=?", new String[]{num}, null, null, null);
    }

    public Cursor getAllContacts() {
        return db.query(TABLE_CONTACTS, null, null, null, null, null, null);
    }

    public int updateContact(Integer numAct, Integer numNv, String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, String miniature) {
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
        values.put(KEY_MINIATURE, miniature);
        return db.update(TABLE_CONTACTS, values, KEY_NUMERO + "=?", new String[]{String.valueOf(numAct)});
    }

    public int deleteContact(int num) {
        return db.delete(TABLE_CONTACTS, KEY_NUMERO + "=?", new String[]{String.valueOf(num)});
    }

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

    public long insertCc(Integer id, Integer numContact, Integer idChamp) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDTUPLE, id);
        values.put(KEY_NUMCONTACT, numContact);
        values.put(KEY_IDCHAMP, idChamp);
        return db.insert(DBAdapter.TABLE_CC, null, values);
    }

    public Cursor getCc(String id) {
        return db.query(DBAdapter.TABLE_CC, null, DBAdapter.KEY_IDTUPLE + "=?", new String[]{id}, null, null, null);
    }

    public int updateCc(Integer id, String numContact, String idChamp) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDTUPLE, id);
        values.put(KEY_NUMCONTACT, numContact);
        values.put(KEY_IDCHAMP, idChamp);
        return db.update(TABLE_CC, values, KEY_IDTUPLE + "=?", new String[]{String.valueOf(id)});
    }

    public int deleteCc(int id) {
        return db.delete(TABLE_CC, KEY_IDTUPLE + "=?", new String[]{String.valueOf(id)});
    }

    public void loadBD() {
        long id = insertContact(0, "Legrand", "pauline", "986575", "residence heimanu", "12345", "paijfz,dvkdsl@bfdbd", "etudiante", "couple", "3");
        id = insertContact(1, "grandle", "camille", "9687567", "piece du joux", "98765", "nojuhybh@nvdv", "lycee", "seul", "1");
        id = insertChamp(0, "hebergement", "appart");
        id = insertChamp(1, "email2", "gfgnfgf@ndlkhnf.com");
        id = insertChamp(2, "nom2", "comme");
        id = insertCc(0, 1, 1);
        id = insertCc(1, 1, 2);
        id = insertCc(2, 0, 0);

    }

    public Dictionary getChampsAddContact(int numContact) {
        Dictionary champsContact = new Hashtable();


    Cursor c = db.query(TABLE_CC,
                new String[]{KEY_IDCHAMP},
                KEY_NUMCONTACT + "=?",
                new String[]{String.valueOf(numContact)},
                null, null, null);

        Log.d("Additionel", "récupère les ids pour le contact concerné");

        if (c != null && c.moveToFirst()) {
            do {
                int idChamp = c.getInt(0);

                Log.d("Additionel", "requete sur le tuple : " + idChamp);
                Cursor champCursor = db.query(TABLE_CHAMPS,
                        new String[]{KEY_LIBELLE, KEY_DONNEE},
                        KEY_IDCHAMPS + "=?",
                        new String[]{String.valueOf(idChamp)},
                        null, null, null);


                Log.d("Additionel", "récupération des données");
                if (champCursor != null && champCursor.moveToFirst()) {
                    String libelle = champCursor.getString(0);
                    String donnee = champCursor.getString(1);
                    champsContact.put(libelle, donnee);

                    Log.d("Additionel", "Libellé: " + libelle + " | Donnée: " + donnee);

                    champCursor.close();
                }
            } while (c.moveToNext());
            c.close();
        }

        Log.d("Additionel", "getChampsAddContact: champsContact = " + champsContact);
        return champsContact;
    }

}
