package com.github.snambi.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.github.snambi.todoapp.db.TodoItem;
import com.github.snambi.todoapp.db.TodoItemsDataSource;

/**
 * Default activity that lists all the activities in the main page.
 * 
 * @author snambi
 */
public class TodoActivity extends Activity {
	
	public static final int REQUEST_CODE = 100;
	public static final int RESPONSE_CODE = 200;

	private List<TodoItem> items;
	private ArrayAdapter<TodoItem> itemsAdapter;
	private TodoItemsDataSource itemsDataSource;
	
	private ListView listviewItems;
	private EditText etNewItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        // open database
        itemsDataSource = new TodoItemsDataSource(this);
        itemsDataSource.open();
        
        items = new ArrayList<TodoItem>();
        listviewItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etAddNewItem);
        
        //readItems();
        readItemsFromDb();
        
        itemsAdapter = new ArrayAdapter<TodoItem>(this, android.R.layout.simple_list_item_1, items);
        listviewItems.setAdapter(itemsAdapter);
        
        setupListViewListener();
    }
    
    public void addTodoItem( View v){
    	String text = etNewItem.getText().toString();
    	TodoItem item = new TodoItem();
    	item.setDescription(text);
    	
    	items.add( item );
    	itemsAdapter.notifyDataSetChanged();
    	
    	etNewItem.setText("");
    	
    	saveItemDb( item);
    }
    
    protected void onActivityResult( int requestCode, int responseCode, Intent data){
    	if( requestCode == REQUEST_CODE && responseCode == RESPONSE_CODE ){
    		if( data != null ){
    			String newitem = data.getStringExtra("ITEM");
    			int position = data.getIntExtra("ROW", 0);
    			
    			// update the value of "item" at index 'position'
    			if( !items.get(position).equals(newitem) ){
    				TodoItem item = items.get(position);
    				item.setDescription(newitem);
    				itemsAdapter.notifyDataSetChanged();
    				
    				// update the item in DB
    				saveItemDb(item);
    			}
    		}
    	}
    }
    
    private void setupListViewListener(){
    	
    	// set up call back to delete an item
    	listviewItems.setOnItemLongClickListener( new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, 
										View view,
										int position, 
										long rowId) {
				TodoItem item = items.remove(position);
				//delete item from DB
				deleteItem(item);
				
				itemsAdapter.notifyDataSetChanged();
				
				//saveItems();
				return true;
			}
		});
    	
    	// set up call back to edit an item
    	listviewItems.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, 
					View view, 
					int position,
					long rowId) {
				
				TodoItem selectedItem = items.get(position);
				
				Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
				intent.putExtra("ITEM", selectedItem.getDescription());
				intent.putExtra("ROW",  position);
				
				// start the activity
				//startActivity(intent);
				startActivityForResult(intent, 100);
			}
		});
    }
    
    private void readItemsFromDb(){
    	items = itemsDataSource.getAllItems();
    }
    
    private void readItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File( filesDir, "todo.txt");
    	
    	items = new ArrayList<TodoItem>();
    	try {
    		
			List<String> stritems = new ArrayList<String>(FileUtils.readLines(todoFile));
			for( String itm : stritems ){
				TodoItem i = new TodoItem();
				i.setDescription(itm);
				items.add(i);
			}
		} catch (IOException e) {
			
			e.printStackTrace();
		}
    }
    
    private void deleteItem( TodoItem item){
    	itemsDataSource.deleteTodoItem(item);
    }
    
    // saves one item : that is updates or inserts one item.
    private void saveItemDb( TodoItem item ){
    	itemsDataSource.saveItem(item);
    }
    
    // saves all items
    private void saveItemsDb(){
    	itemsDataSource.saveItems(items);
    }
        
    private void saveItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File( filesDir, "todo.txt");
    	
    	try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
