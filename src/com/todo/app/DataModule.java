package com.todo.app;

import java.net.URI;
import java.util.Date;

import javax.swing.*;

import com.remobjects.dataabstract.FillRequestTask;
import com.remobjects.dataabstract.RemoteDataAdapter;
import com.remobjects.dataabstract.data.CalculatedDataColumn;
import com.remobjects.dataabstract.data.DataRow;
import com.remobjects.dataabstract.data.DataRowAction;
import com.remobjects.dataabstract.data.DataRowChangedEvent;
import com.remobjects.dataabstract.data.DataTable;
import com.remobjects.dataabstract.data.LookupDataColumn;
import com.remobjects.dataabstract.intf.TableRequestInfo;
import com.remobjects.dataabstract.swing.DataTableModel;

public class DataModule {
	
	private RemoteDataAdapter dataAdapter;

	private DataTable tasksTable;
	private DataTable prioritiesTable;
	private DataTable userTable;

	private DataTableModel tasksTableModel;
	private DataTableModel prioritiesTableModel;
	private DataTableModel userTableModel;
	
	public void initComponents()
	{
		///@SupressWarnings'deprecation';
		this.dataAdapter = RemoteDataAdapter.create(URI.create("http://127.0.0.1:7099/bin"));
	}
	
	private void initTables()
	{
		this.tasksTable = new DataTable("Tasks");
		this.tasksTableModel = new DataTableModel(this.tasksTable);
		this.tasksTable.addTableDataChangedListener(this.tasksTableModel);
		
		this.prioritiesTable = new DataTable("Priorities");
		this.prioritiesTableModel = new DataTableModel(this.prioritiesTable);
		this.prioritiesTable.addTableDataChangedListener(this.prioritiesTableModel);
		
		this.userTable = new DataTable("Users");
		this.userTableModel = new DataTableModel(this.userTable);
		this.userTable.addTableDataChangedListener(this.userTableModel);
	}
	
	public DataModule()
	{
		initComponents();
		initTables();
	}
	
	public DataTableModel getTasksDataTableModel()
	{
		return tasksTableModel;
	}
	
	public boolean login(String username, String password)
	{
		String loginString = String.format("User=%s;Password=%s;Domain=%s;Schema=%s", username,password,"todo","Tasks");
		
		return this.dataAdapter.login(loginString);
	}
	
	public void loadData() {
	    DataTable[] tables = {this.tasksTable, this.prioritiesTable};
	    ///TableRequestInfo[] tablesName= {Tasks,Priorities};
	    
	    this.tasksTable.clear();
	    this.tasksTable.getColumns().clear();
	    
	    this.dataAdapter.fill(tables);
	    setupTable();
	}
	
	protected void setupTable() {   
	    //Create a lookup column based on Priority
	    LookupDataColumn lookup = new LookupDataColumn("PriorityText",
	            tasksTable.getColumns().getColumn("Priority"),
	            prioritiesTable.getColumns().getColumn("Id"),
	            prioritiesTable.getColumns().getColumn("Name"));
	    
	    lookup.setCaption("Priority Lvl");
	    
	    CalculatedDataColumn calculatedDoneColumn = new CalculatedDataColumn("Status", String.class, dataRow -> {
	        long doneAsLong =  ((Long)dataRow.getField("Done")).longValue();
	        return doneAsLong == 1 ? "Done" : "Not Yet Done";
	    });
	    
	    tasksTable.getColumns().add(lookup);
	    tasksTable.getColumns().add(calculatedDoneColumn);
	}
	
	public void applyChanges() 
	{
		  this.dataAdapter.applyChangesAsync(tasksTable, (aTask, aState) -> {
		    if (aTask.isCancelled() || aTask.isFailed()) {  
		      System.out.printf("An error occurred: %s\n", aTask.getFailureCause().getMessage());
		    } else {
		      System.out.println("update success");
		    }
		  }).execute();
	}
	
	public DataRow createNewRow() 
	{
		  DataRow newRow = this.tasksTable.addNewRow();
		  
		  newRow.setField("DueDate", new Date());
		  newRow.setField("Task", " ");
		  newRow.setField("Details", " ");
		  newRow.setField("Done",(long)0);
		  newRow.setField("Priority",(long)2);

		  return newRow;
	}
	
	public void discardRow(DataRow aRow) 
	{
		  this.tasksTable.cancelChangesForRow(aRow);
	}
	
	public void deleteRow(int selectedRowIndex) 
	{
	    this.tasksTableModel.getRow(selectedRowIndex).delete();
	    
	    this.tasksTableModel.tableRowChanged(new DataRowChangedEvent(tasksTableModel.getTable(), null, DataRowAction.Nothing));
	}
}
