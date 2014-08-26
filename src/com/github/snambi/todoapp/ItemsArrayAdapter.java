package com.github.snambi.todoapp;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.snambi.todoapp.db.TodoItem;

public class ItemsArrayAdapter<T> extends ArrayAdapter<TodoItem>{

	public ItemsArrayAdapter(Context context, 
								List<TodoItem> items) {
		
		super(context, R.layout.todo_item, items);
	}

	public View getView( int position, View convertView, ViewGroup parent){
		TodoItem item = getItem(position);
		
		if( convertView == null ){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
		}
		
		TextView tvDesc = (TextView) convertView.findViewById( R.id.tvDesc);
		TextView tvDueDate = (TextView) convertView.findViewById(R.id.tvDueDate);
		
		tvDesc.setText( item.getDescription());
		tvDueDate.setText( item.getDueDateString() );
		
		return convertView;
	}

}
