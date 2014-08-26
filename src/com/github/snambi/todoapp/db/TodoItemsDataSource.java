package com.github.snambi.todoapp.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TodoItemsDataSource {
	
	private SQLiteDatabase database;
	private ToDoSqlLiteHelper dbHelper;
	private String[] allColumns = { ToDoSqlLiteHelper.SQL_COL_ITEM_ID, 
									ToDoSqlLiteHelper.SQL_COL_ITEM_DESC,
									ToDoSqlLiteHelper.SQL_COL_ITEM_DUEDATE };
	
	public TodoItemsDataSource( Context context) {
		dbHelper = new ToDoSqlLiteHelper(context);
	}

	public void open(){
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public static TodoItem convertCursorToDoItem(Cursor cursor){
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		
		TodoItem item = new TodoItem();
		if( cursor != null && !cursor.isClosed() ){
			
			item.setId(cursor.getLong(0));
			item.setDescription(cursor.getString(1));
			Date date=null;
			
			try {
				if( cursor.getString(2) != null ){
					date = dateFormat.parse(cursor.getString(2));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			item.setDueDate(date);
		}
		
		return item;
	}
	
	public TodoItem createTodoItem(TodoItem item){
		ContentValues values = new ContentValues();
		
		values.put(ToDoSqlLiteHelper.SQL_COL_ITEM_DESC, item.getDescription());
		values.put(ToDoSqlLiteHelper.SQL_COL_ITEM_DUEDATE, item.getDueDateString());
		
		long insertId = database.insert( ToDoSqlLiteHelper.SQL_TABLE_TODOITEMS, null, values);
		Cursor cursor = database.query( ToDoSqlLiteHelper.SQL_TABLE_TODOITEMS, 
															allColumns,
															ToDoSqlLiteHelper.SQL_COL_ITEM_ID+"="+insertId,
															null,null,null,null);
		
		cursor.moveToFirst();
		TodoItem i = convertCursorToDoItem(cursor);
		item.setId( i.getId() );
		cursor.close();

		return item;
	}
	
	public void deleteTodoItem( TodoItem item){
		if( item != null ){
			long id = item.getId();
			database.delete(ToDoSqlLiteHelper.SQL_TABLE_TODOITEMS, 
							ToDoSqlLiteHelper.SQL_COL_ITEM_ID+"="+id,
							null);
			Log.d(TodoItemsDataSource.class.getName(), "TodoItem deleted with id :" + id);
		}
	}
	
	public List<TodoItem> getAllItems(){
		List<TodoItem> items = new ArrayList<TodoItem>();
		
		Cursor cursor = database.query( ToDoSqlLiteHelper.SQL_TABLE_TODOITEMS, 
										allColumns, null,null, null, null, null);
		
		cursor.moveToFirst();
		while( !cursor.isAfterLast() ){
			TodoItem item = convertCursorToDoItem(cursor);
			items.add( item);
			cursor.moveToNext();
		}
		
		cursor.close();
		
		return items;
	}
	
	public void updateToDoItem( TodoItem item){
		ContentValues values = new ContentValues();
		values.put(ToDoSqlLiteHelper.SQL_COL_ITEM_DESC, item.getDescription());
		values.put(ToDoSqlLiteHelper.SQL_COL_ITEM_DUEDATE, item.getDueDateString());
		
		database.update(ToDoSqlLiteHelper.SQL_TABLE_TODOITEMS, values, 
							ToDoSqlLiteHelper.SQL_COL_ITEM_ID + "=" + item.getId(), 
							null);
	}
	
	/*
	 * if the item has a non-zero id, then update the item.
	 * otherwise, insert the item into db.
	 */
	public void saveItems( List<TodoItem> items){
		for( TodoItem item : items ){
			if( item.getId() > 0 ){
				updateToDoItem(item);
			}else{
				createTodoItem(item);
			}
		}
	}
	
	public void saveItem( TodoItem item){
		if( item.getId() > 0){
			updateToDoItem(item);
		}else{
			createTodoItem(item);
		}
	}
}
