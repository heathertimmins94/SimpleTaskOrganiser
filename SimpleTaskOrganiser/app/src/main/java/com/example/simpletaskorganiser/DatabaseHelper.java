package com.example.simpletaskorganiser;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * DatabaseHelper class
 * Created by Glynn Bolton on 03/03/2019.
 * Copyright (c) 2019 Glynn Bolton. All rights reserved.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "TaskOrganiser.db"; //Use Pascal case in SQL database name?
    public static final String TABLE_NAME = "Tasks"; //Use Pascal case and plural in SQL table name?
    public static final String COL_TABLE_ID = "_id"; //This SQL name is mandatory
    public static final String COL_TABLE_DESC = "Description"; //Use Pascal case and singular in SQL column name?

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        //SQLiteDatabase db = this.getWritableDatabase();
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "
                + TABLE_NAME + "(" + COL_TABLE_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TABLE_DESC + " TEXT)");
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     *
     * <p>
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * </p><p>
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     * </p>
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    /**
     * Inserts specified record into task table.
     *
     * @param description   description of the task
     * @return true if successful
     */
    public boolean insertTask(String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TABLE_DESC, description);
        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    /**
     * Returns all data from task table.
     *
     * @return all data from task table
     */
    public Cursor getAllTasks() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return result;
    }

    /**
     * Updates specified record of task table.
     *
     * @param id            identifier of record to update
     * @param description   description of task
     * @return true if successful
     */
    public boolean updateTask(String id, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TABLE_ID, id);
        contentValues.put(COL_TABLE_DESC, description);
        db.update(TABLE_NAME, contentValues, "_id = ?", new String[]{id});
        return true;
    }

    /**
     * Removes record from task table.
     *
     * @param id identifier of record to remove
     * @return number of rows removed
     */
    public Integer deleteTask(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_TABLE_ID + " = ?", new String[]{id});
    }

    /**
     * Removes record from task table.
     *
     * @param id identifier of record to remove
     * @return number of rows removed
     */
    public Integer deleteTask(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_TABLE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    /**
     * Returns record at specified identifier.
     *
     * @param id identifier of record
     * @return record
     */
    public Cursor getTask(long id) {
        Cursor c;
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COL_TABLE_ID, COL_TABLE_DESC};
        String where = COL_TABLE_ID + "=" + id;
        c = db.query(
                true,
                TABLE_NAME,
                columns,
                where,
                null,
                null,
                null,
                null,
                null);

        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    /**
     *
     * @return
     */
    public String[] getAllColumns() {
        return new String[]  {COL_TABLE_ID, COL_TABLE_DESC};
    }


    /**
     *
     * @return
     */
    private long getSize(){
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return count;
    }
}
