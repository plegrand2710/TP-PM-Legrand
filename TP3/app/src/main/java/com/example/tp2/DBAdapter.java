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

    static final String TAG = "TP3";
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


    public long insertContact(String nom, String prenom, String tel, String adresse, String cp, String email, String metier, String situation, String miniature) {
        ContentValues values = new ContentValues();
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
        Log.d(TAG, "insertContact: j'ai récupéré les valeurs dans la base");
        return db.insert(DBAdapter.TABLE_CONTACTS, null, values);
    }

    public Contact getContact(String num) {
        Cursor cursor= db.query(DBAdapter.TABLE_CONTACTS, null, DBAdapter.KEY_NUMERO + "=?", new String[]{num}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contact contact = new Contact();
            int numc = cursor.getColumnIndex("idContact");
            contact.set_numC(cursor.getInt(numc));
            int nom = cursor.getColumnIndex("nom");
            contact.set_nom(cursor.getString(nom));
            int prenom = cursor.getColumnIndex("prenom");
            contact.set_prenom(cursor.getString(prenom));
            int tel = cursor.getColumnIndex("tel");
            contact.set_tel(cursor.getString(tel));
            int adresse = cursor.getColumnIndex("adresse");
            contact.set_adresse(cursor.getString(adresse));
            int email = cursor.getColumnIndex("email");
            contact.set_email(cursor.getString(email));
            cursor.close();
            return contact;
        }
        return null;
    }

    public Cursor getAllContacts() {
        return db.query(TABLE_CONTACTS, null, null, null, null, null, null);
    }

    public Integer getDernierId() {
        Log.d(TAG, "getDernierId: je suis ici");
        Cursor cursor = db.query(TABLE_CONTACTS, new String[]{"MAX(" + DBAdapter.KEY_NUMERO + ")"}, null, null, null, null, null);
        Log.d(TAG, "getDernierId: id = " + cursor.getInt(0));
        return cursor.getInt(0);
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
        long id1 = insertContact("Legrand", "pauline", "986575", "residence heimanu", "12345", "paijfz,dvkdsl@bfdbd", "etudiante", "couple", "3");
        long id2 = insertContact("grandle", "camille", "9687567", "piece du joux", "98765", "nojuhybh@nvdv", "lycee", "seul", "1");
        long id3 = insertContact("d'acc", "anthone", "986575", "residence heimanu", "12345", "paijfz,dvkdsl@bfdbd", "etudiante", "couple", "4");
        long id = insertContact("okay", "corinne", "9687567", "piece du joux", "98765", "nojuhybh@nvdv", "lycee", "seul", "5");
        long ic = insertChamp(0, "hebergement", "appart");
        id = insertContact("pourquoi", "camille", "9687567", "piece du joux", "98765", "nojuhybh@nvdv", "lycee", "seul", "1");
        id = insertContact("choub", "anthone", "986575", "residence heimanu", "12345", "paijfz,dvkdsl@bfdbd", "etudiante", "couple", "4");
        id = insertContact("create", "corinne", "9687567", "piece du joux", "98765", "nojuhybh@nvdv", "lycee", "seul", "5");
        long ic1 = insertChamp(1, "email2", "gfgnfgf@ndlkhnf.com");
        long ic2 = insertChamp(2, "nom2", "comme");
        id = insertCc(0, (int) id1, (int) ic);
        id = insertCc(1, (int) id1, (int) ic1);
        id = insertCc(2, (int) id3, (int) ic2);

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

    public int getNbLigneTable(String tableName) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
            if (cursor != null && cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return count;
    }
    public int getNumLigne(String tableName, String whereClause) {
        int rowNumber = -1;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT ROWID FROM " + tableName + " WHERE " + whereClause, null);

            if (cursor != null && cursor.moveToFirst()) {
                rowNumber = cursor.getInt(0);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return rowNumber;
    }

    public void clearAllTables() {
        open();
        try {
            db.execSQL("DELETE FROM " + TABLE_CONTACTS);
            db.execSQL("DELETE FROM " + TABLE_CHAMPS);
            db.execSQL("DELETE FROM " + TABLE_CC);
            Log.d(TAG, "clearAllTables: Toutes les tables ont été vidées avec succès.");
        } catch (SQLException e) {
            Log.e(TAG, "clearAllTables: Erreur lors de la suppression du contenu des tables", e);
        } finally {
            close();
        }
    }

    public int getChampId(String libelle, String donnee) {
        Cursor cursor = db.query(TABLE_CHAMPS, new String[]{KEY_IDCHAMPS},
                KEY_LIBELLE + "=? AND " + KEY_DONNEE + "=?",
                new String[]{libelle, donnee}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int champId = cursor.getInt(0);
            cursor.close();
            return champId;
        }

        if (cursor != null) {
            cursor.close();
        }
        return -1;
    }

    public boolean existeRelationCC(int numContact, int idChamp) {
        Cursor cursor = db.query(TABLE_CC, null,
                KEY_NUMCONTACT + "=? AND " + KEY_IDCHAMP + "=?",
                new String[]{String.valueOf(numContact), String.valueOf(idChamp)},
                null, null, null);

        boolean exists = (cursor != null && cursor.moveToFirst());
        if (cursor != null) {
            cursor.close();
        }
        return exists;
    }

}
