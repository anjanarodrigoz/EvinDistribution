package com.example.evindistribution;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import com.example.evindistribution.Models.Search;
import com.example.evindistribution.Models.SoldItemDetails;

import java.util.ArrayList;
import java.util.List;

public class DbHandler extends SQLiteOpenHelper {

    private static final int VERSION = 3;
    private static final String DB_NAME = "EVIN";
    private static final String TABLE_NAME = "SOLD_ITEM";
    private static final String TABLE_NAME_TWO = "STORE";


    private static final String ID = "id";
    private static final String SEARCH = "search";
    private static final String NAME = "name";
    private static final String ITEM_PRICE = "itemPrice";
    private static final String QTY = "qty";
    private static final String SALE_PRICE = "salePrice";
    private static final String PROFIT = "profit";

    private static DbHandler mInstance = null;


    private DbHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static synchronized DbHandler getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new DbHandler(context.getApplicationContext());
        }
        return mInstance;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_QUARY = "CREATE TABLE " + TABLE_NAME + " (" +
                ID + " TEXT PRIMARY KEY," +
                NAME + " TEXT," +
                ITEM_PRICE + " DOUBLE," +
                QTY + " INTEGER," +
                SALE_PRICE + " DOUBLE," +
                PROFIT + " DOUBLE" + ");";

        String CREATE_QUARY_TWO = "CREATE TABLE " + TABLE_NAME_TWO + " (" +
                ID + " TEXT PRIMARY KEY," +
                SEARCH + " TEXT" + ");";

        db.execSQL(CREATE_QUARY_TWO);
        db.execSQL(CREATE_QUARY);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_TABLE_QUARY = "DROP TABLE IF EXISTS " + TABLE_NAME;
        String DROP_TABLE_QUARY_TWO = "DROP TABLE IF EXISTS " + TABLE_NAME_TWO;
        db.execSQL(DROP_TABLE_QUARY);
        db.execSQL(DROP_TABLE_QUARY_TWO);
        onCreate(db);

    }

    public void addItem(SoldItemDetails soldItemDetails) {

        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(ID, soldItemDetails.getId());
        contentValues.put(NAME, soldItemDetails.getName());
        contentValues.put(QTY, soldItemDetails.getQty());
        contentValues.put(ITEM_PRICE, soldItemDetails.getItemPrice());
        contentValues.put(SALE_PRICE, soldItemDetails.getSellPrice());
        contentValues.put(PROFIT, soldItemDetails.getProfit());

        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        sqLiteDatabase.close();
    }

    public void createStore(Search item) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ID, item.getId());
        values.put(SEARCH, item.getSearch());

        db.insert(TABLE_NAME_TWO, null, values);
        db.close();
    }


    public List<String> getSearchID(String search) {

        List<String> idList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query;
        if (search == null) {
            query = "SELECT * FROM " + TABLE_NAME_TWO;
        } else {
            query = "SELECT * FROM " + TABLE_NAME_TWO + " WHERE " + SEARCH + " LIKE '%" + search + "%'";
        }

        Cursor cursor = db.rawQuery(query, null);

        String id;
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getString(0);
                idList.add(id);
            } while (cursor.moveToNext());
        }
    return idList;
    }


    public List<SoldItemDetails> getItems() {

        List<SoldItemDetails> itemList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String quary = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(quary, null);

        if (cursor.moveToFirst()) {
            do {
                SoldItemDetails item = new SoldItemDetails();
                item.setId(cursor.getString(0));
                item.setName(cursor.getString(1));
                item.setQty(cursor.getInt(3));
                item.setItemPrice(cursor.getDouble(2));
                item.setSellPrice(cursor.getDouble(4));
                item.setProfit(cursor.getDouble(5));

                itemList.add(item);

            } while (cursor.moveToNext());

        }
        return itemList;

    }

    public void deleteItem(String id) {

        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, ID + "=?", new String[]{id});
        db.close();
    }

    public void updateItem(SoldItemDetails item) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(QTY, item.getQty());
        contentValues.put(SALE_PRICE, item.getSellPrice());
        contentValues.put(PROFIT, item.getProfit());

        db.update(TABLE_NAME, contentValues, ID + "=?", new String[]{item.getId()});
        db.close();

    }

    public List<String> getIds() {

        List<String> ids = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String quary = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(quary, null);

        if (cursor.moveToFirst()) {
            do {

                ids.add(cursor.getString(0));

            } while (cursor.moveToNext());

        }
        return ids;

    }

    public void deleteAll() {

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
        db.close();
    }
}
