package com.github.snambi.todoapp;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TodoActivity extends Activity {

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
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listviewItems.setAdapter(itemsAdapter);
        
        items.add("Item 1");
        items.add("Item 2");
        
        setupListViewListener();
    }
    
    public void addTodoItem( View v){
    	String text = etNewItem.getText().toString();
    	items.add(text);
    	itemsAdapter.notifyDataSetChanged();
    	etNewItem.setText("");
    }
    
    private void setupListViewListener(){
    	listviewItems.setOnItemLongClickListener( new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, 
										View view,
										int position, 
										long rowId) {
				items.remove(position);
				itemsAdapter.notifyDataSetChanged();
				return false;
			}
		});
    }
}
