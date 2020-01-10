package com.bfbyxx.wccydriver.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.Arrays;
import java.util.List;

public class MyCallBack implements LouSQLite.ICallBack {

    public static final String TABLE_NAME_PHRASE = "phrase";
    public static final String TABLE_NAME_FAVORITE = "favorite";

    private static final String DATABASE_NAME = "wccyDriver.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";
    private static final String SEP_COMMA = ",";

    private static final String TABLE_SCHEMA_PHRASE =
            "CREATE TABLE " + TABLE_NAME_PHRASE + " (" +
                    PhraseEntry._ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                    PhraseEntry.COLEUM_NAME_ID + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_TITLE + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_TIME + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_CONTENT + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_FAVORITE + TYPE_INTEGER +
                    ")";
    private static final String TABLE_SCHEMA_FAVORITE =
            "CREATE TABLE " + TABLE_NAME_FAVORITE + " (" +
                    PhraseEntry._ID + TYPE_INTEGER + " PRIMARY KEY AUTOINCREMENT, " +
                    PhraseEntry.COLEUM_NAME_ID + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_TITLE + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_TIME + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_CONTENT + TYPE_TEXT + SEP_COMMA +
                    PhraseEntry.COLEUM_NAME_FAVORITE + TYPE_INTEGER +
                    ")";

    public MyCallBack() {
    }

    @Override
    public List<String> createTablesSQL() {
        return Arrays.asList(
                TABLE_SCHEMA_PHRASE,
                TABLE_SCHEMA_FAVORITE
        );
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public int getVersion() {
        return DATABASE_VERSION;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        switch (oldVersion) {
            case 0:
                db.execSQL(TABLE_SCHEMA_FAVORITE); // 升级操作；
            case 1:
                break;
            default:
                break;
        }
    }


    @Override
    public <T> void assignValuesByEntity(String tableName, T t, ContentValues values) {

        switch (tableName) {
            case TABLE_NAME_PHRASE:
                if (t instanceof Phrase) {
                    Phrase phrase = (Phrase) t;
                    values.put(PhraseEntry.COLEUM_NAME_ID, phrase.getId());
                    values.put(PhraseEntry.COLEUM_NAME_TITLE, phrase.getmTitle());
                    values.put(PhraseEntry.COLEUM_NAME_TIME, phrase.getmTime());
                    values.put(PhraseEntry.COLEUM_NAME_CONTENT, phrase.getContent());
                    values.put(PhraseEntry.COLEUM_NAME_FAVORITE, phrase.getFavorite());
                }
                break;
            case TABLE_NAME_FAVORITE:
                if (t instanceof Phrase) {
                    Phrase phrase = (Phrase) t;
                    values.put(PhraseEntry.COLEUM_NAME_ID, phrase.getId());
                    values.put(PhraseEntry.COLEUM_NAME_TITLE, phrase.getmTitle());
                    values.put(PhraseEntry.COLEUM_NAME_TIME, phrase.getmTime());
                    values.put(PhraseEntry.COLEUM_NAME_CONTENT, phrase.getContent());
                    values.put(PhraseEntry.COLEUM_NAME_FAVORITE, phrase.getFavorite());
                }
                break;
        }
    }

    @Override
    public Object newEntityByCursor(String tableName, Cursor cursor) {
        switch (tableName) {
            case TABLE_NAME_PHRASE:
                return new Phrase(
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_ID)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_TIME)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_CONTENT)),
                        cursor.getInt(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_FAVORITE))
                );
            case TABLE_NAME_FAVORITE:
                return new Phrase(
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_ID)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_TIME)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_CONTENT)),
                        cursor.getInt(cursor.getColumnIndex(PhraseEntry.COLEUM_NAME_FAVORITE))
                );
        }

        return null;
    }
}
