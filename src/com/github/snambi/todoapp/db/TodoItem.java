package com.github.snambi.todoapp.db;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TodoItem {

	private long id;
	private String description;
	private Date dueDate;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public String getDueDateString(){
		Date date = getDueDate();
		String result ="";
		if( date != null ){
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			result= dateFormat.format(date);
		}
			
		return result;
	}
	public String toString(){
		return description;
	}
}
