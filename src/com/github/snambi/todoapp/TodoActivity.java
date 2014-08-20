package com.github.snambi.todoapp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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

public class TodoActivity extends Activity {
	
	public static final int REQUEST_CODE = 100;
	public static final int RESPONSE_CODE = 200;

	private ArrayList<String> items;
	private ArrayAdapter<String> itemsAdapter;
	private ListView listviewItems;
	private EditText etNewItem;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        
        items = new ArrayList<String>();
        listviewItems = (ListView) findViewById(R.id.lvItems);
        etNewItem = (EditText) findViewById(R.id.etAddNewItem);
        
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listviewItems.setAdapter(itemsAdapter);
        
        //items.add("Item 1");
        //items.add("Item 2");
        
        setupListViewListener();
    }
    
    public void addTodoItem( View v){
    	String text = etNewItem.getText().toString();
    	items.add(text);
    	itemsAdapter.notifyDataSetChanged();
    	etNewItem.setText("");
    	
    	saveItems();
    }
    
    protected void onActivityResult( int requestCode, int responseCode, Intent data){
    	if( requestCode == REQUEST_CODE && responseCode == RESPONSE_CODE ){
    		if( data != null ){
    			String newitem = data.getStringExtra("ITEM");
    			int position = data.getIntExtra("ROW", 0);
    			
    			// update the value of "item" at index 'position'
    			if( !items.get(position).equals(newitem) ){
    				items.set(position, newitem);
    				itemsAdapter.notifyDataSetChanged();
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
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				
				saveItems();
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
				
				String selectedItem = items.get(position);
				
				Intent intent = new Intent(TodoActivity.this, EditItemActivity.class);
				intent.putExtra("ITEM", selectedItem);
				intent.putExtra("ROW",  position);
				
				// start the activity
				//startActivity(intent);
				startActivityForResult(intent, 100);
			}
		});
    }
    
    private void readItems(){
    	File filesDir = getFilesDir();
    	File todoFile = new File( filesDir, "todo.txt");
    	
    	try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items = new ArrayList<String>();
			e.printStackTrace();
		}
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
