package xyz.carlesllobet.pullmarket.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

import xyz.carlesllobet.pullmarket.Domain.Product;

public class DatabaseHandler extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "db";
 
    // Login table nombre
    private static final String TABLE_PRODUCTS = "products";
 
    // Login Table Columns nombres
    private static final String KEY_NOMBRE = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_ID = "id";
    private static final String KEY_FOTO = "pic";
    private static final String KEY_SECTOR = "sector";
    private static final String KEY_PREU = "preu";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PROD_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_PRODUCTS + "("
                + KEY_ID + " INTEGER UNIQUE PRIMARY KEY NOT NULL,"
                + KEY_NOMBRE + " STRING,"
                + KEY_DESCRIPTION + " TEXT NOT NULL,"
                + KEY_FOTO + " STRING,"
                + KEY_SECTOR + " INTEGER,"
                + KEY_PREU + " INTEGER"+ ")";
        db.execSQL(CREATE_PROD_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
 
        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public boolean addProduct(Integer id, String nombre, String description, Integer sector, Integer preu, Uri pic) {
    	SQLiteDatabase db = this.getWritableDatabase();

        //Si existeix, retorna fals, i no es pot afegir
        if (CheckProductExist(id)) return false;

        if ((id.equals("")) || (nombre.equals("")) || (preu.toString().equals(""))) return false;

        String foto = pic.toString();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id); // Name
        values.put(KEY_NOMBRE, nombre); // userName
        values.put(KEY_DESCRIPTION, description); // Password
        values.put(KEY_FOTO, foto); // Foto
        values.put(KEY_SECTOR, sector); // Notificacion
        values.put(KEY_PREU, preu); // Puntuacion


        // Inserting Row
        db.insert(TABLE_PRODUCTS, null, values);
        db.close(); // Closing database connection
        return true;
    }

    /**
     * Getting user data from database
     * */
    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> products = new ArrayList<Product>();

        String selectQuery = "SELECT * FROM " + TABLE_PRODUCTS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        Integer i = 0;
        while(cursor.getCount() > 0){
            Product aux = new Product(cursor.getInt(0),cursor.getString(1),cursor.getString(2),Uri.parse(cursor.getString(3)),
                    cursor.getInt(4),cursor.getInt(5));
            products.add(i, aux);
            ++i;
        }
        cursor.close();
        db.close();
        // return user
        return products;
    }

    public Boolean CheckProductExist(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            return true;
        }
        return false;
    }

    public String getProductName (Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            db.close();
            return (c.getString(1));
        }
        db.close();
        return "";
    }

    public Uri getFoto (Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery="SELECT * FROM " + TABLE_PRODUCTS + " WHERE " + KEY_ID + " = " + id;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            db.close();
            return (Uri.parse(c.getString(3)));
        }
        db.close();
        return null;
    }
     
    /**
     * Re crate database
     * Delete all tables and create them again
     * */
    public void resetTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_PRODUCTS, null, null);
        db.close();
    }
}
