package com.github.snambi.todoapp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity to edit an existing item.
 * 
 * @author snambi
 */
public class EditItemActivity extends Activity {
	
	private EditText itemEditText;
	private TextView dueDateView;
	private int position;
	private Date date;

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
		
		// show current date
		date = new Date();
		dueDateView = (TextView) findViewById(R.id.tvSetDueDate);
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		String dateStr = dateFormat.format(date);
		
		dueDateView.setText(dateStr);
		// now attach a listener for the due date
		
		dueDateView.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDatePickerDialog( v );
			}
		});
		
//		dueDateView.setOnTouchListener( new OnTouchListener() {
//			
//			public boolean onTouch(View v, MotionEvent event) {
//				showDatePickerDialog( v );
//				return true;
//			}
//		});
	}
		
	public void onSubmit(View view){
		String newitem = itemEditText.getEditableText().toString();
		
		Intent intent = new Intent();
		intent.putExtra("ITEM", newitem);
		intent.putExtra("ROW", position);
		intent.putExtra("DUE_DATE", dueDateView.getText().toString() );
		
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
	
	public void showDatePickerDialog( View view){
		DialogFragment fragment = new DatePickerFragment(dueDateView, date);
		fragment.show(getFragmentManager(), "datepicker");
	}
	
	@SuppressLint("SimpleDateFormat") 
	public static class DatePickerFragment extends DialogFragment 
					implements DatePickerDialog.OnDateSetListener {
		
		private Date date;
		private TextView tvview;
		private boolean fired = false;
		
		public DatePickerFragment( TextView view, Date date){
			this.date = date;
			this.tvview = view;
		}
		
		public Dialog onCreateDialog(Bundle savedInstance ){
	        // Use the current date as the default date in the picker
	        final Calendar c = Calendar.getInstance();
	        int year = c.get(Calendar.YEAR);
	        int month = c.get(Calendar.MONTH);
	        int day = c.get(Calendar.DAY_OF_MONTH);

	        // Create a new instance of DatePickerDialog and return it
	        return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void onDateSet(DatePicker view, 
								int year, 
								int monthOfYear,
								int dayOfMonth) {
			
			if(fired == true ){
				return;
			}
			
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, dayOfMonth);
			c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			
			date = c.getTime();
			
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			String dateStr = dateFormat.format(date);
			
			tvview.setText(dateStr);
			
			fired = true;
		}
	}
}
