package com.github.snambi.todoapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends Activity {
	
	private EditText itemEditText;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		itemEditText = (EditText) findViewById(R.id.etEditItem);
		
		// get the values passed from parent activity
		Intent intent = getIntent();
		String item = intent.getStringExtra("ITEM");
		position = intent.getIntExtra("ROW",0);
		
		if( item != null && item.length() != 0 ){
			itemEditText.setText(item);
		}
	}
	
	public void onSubmit(View view){
		String newitem = itemEditText.getEditableText().toString();
		
		Intent intent = new Intent();
		intent.putExtra("ITEM", newitem);
		intent.putExtra("ROW", position);
		
		setResult( TodoActivity.RESPONSE_CODE, intent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
