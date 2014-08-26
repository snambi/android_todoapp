package com.github.snambi.todoapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ToDoSqlLiteHelper extends SQLiteOpenHelper{

	static final String DATABASE_NAME = "todo_items.db";
	static final int DATABASE_VERSION  = 2;
	
	static final String SQL_TABLE_TODOITEMS = "TODOITEMS";
	static final String SQL_COL_ITEM_ID = "ITEM_ID";
	static final String SQL_COL_ITEM_DESC = "ITEM_DESC";
	static final String SQL_COL_ITEM_DUEDATE = "ITEM_DUE_DATE";
	static final String SQL_CREATE_TBL = "create table " + SQL_TABLE_TODOITEMS + " ( "+ SQL_COL_ITEM_ID + " integer primary key autoincrement, "
														+ SQL_COL_ITEM_DESC + "  text not null,"
														+ SQL_COL_ITEM_DUEDATE + " text );";
	
	public ToDoSqlLiteHelper( Context context	){
		super(context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(SQL_CREATE_TBL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(ToDoSqlLiteHelper.class.getName(), 
				"Upgrading database from " + oldVersion + " to " + newVersion + ". All data will be erased.");
		database.execSQL("DROP TABLE IF EXISTS " + SQL_TABLE_TODOITEMS);
		onCreate(database);
	}

}
