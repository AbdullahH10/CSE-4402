package com.todo.app;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.remobjects.dataabstract.data.DataRow;

public class todo {

	private JFrame frame;
	private JTextField textFieldFilter;
	private JTable table;
	
	private boolean isLoggedIn;
	
	private DataModule dataModule;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					todo window = new todo();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public todo() {
		initialize();
	}
	
	private void tweakTable() {
		  // get the columns we are interested in
		  TableColumn dueDateColumn = this.table.getColumn("DueDate");
		  TableColumn statusColumn = this.table.getColumn("Status");
		  TableColumn taskColumn = this.table.getColumn("Task");
		  TableColumn priorityLevelColumn = this.table.getColumn("Priority Lvl");
		  
		  
		// delete all the columns from the table 
		  TableColumnModel tableColumnModel = this.table.getColumnModel();
		  
		  while (tableColumnModel.getColumnCount() > 0) 
		  {
		        tableColumnModel.removeColumn(tableColumnModel.getColumn(0));
		   }
		  
		  
		// add back the columns we are interested in
		  tableColumnModel.addColumn(taskColumn);
		  tableColumnModel.addColumn(dueDateColumn);
		  tableColumnModel.addColumn(priorityLevelColumn);
		  tableColumnModel.addColumn(statusColumn);
		  
		}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 600, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		//JButton btnLoadData = new JButton("Load Data");
		JButton btnLoadData = new JButton("Sign In");
		btnLoadData.setBounds(12, 13, 97, 25);
		frame.getContentPane().add(btnLoadData);
		
		
		btnLoadData.addActionListener(actionEvent->
		{
			if(!this.isLoggedIn)
			{
				login_dialog loginDialog = new login_dialog();
				loginDialog.setVisible(true);
			
			if(!loginDialog.getLoginCancelled())
			{
				
				if(this.dataModule.login(loginDialog.getUsername(),loginDialog.getPassword()))
				{
					this.isLoggedIn=true;
					JOptionPane.showMessageDialog(null,"success");
				}
				
				else
				{
					JOptionPane.showMessageDialog(null,"Error logging in, please try again");
					return;
				}
			}
			
			else
			{
				//login cancelled
				JOptionPane.showMessageDialog(null,"New user created");
				return;
			}
			
			}
			this.dataModule.loadData();
			tweakTable();
	
		});
		
		
		JButton btnAddTask = new JButton("Add Task");
		btnAddTask.setBounds(118, 13, 97, 25);
		frame.getContentPane().add(btnAddTask);
		
		
		btnAddTask.addActionListener(actionEvent -> 
		{
			  DataRow newRow = this.dataModule.createNewRow(); 
			  
			  EditTaskDialog editDialog = new EditTaskDialog();
			  editDialog.setLocationRelativeTo(null);
			  editDialog.setTitle("Add New Task");
			  editDialog.setData(newRow);
			  editDialog.setVisible(true);
			  
			  
			  if (!editDialog.wasDialogCancelled()) 
			  {
				  newRow = editDialog.getData();
			  } 
			  
			  else 
			  {
				  this.dataModule.discardRow(newRow);
			  }
		});
		
		
		JButton btnEditTask = new JButton("Edit Task");
		btnEditTask.setBounds(227, 13, 97, 25);
		frame.getContentPane().add(btnEditTask);
		
		
		btnEditTask.addActionListener(actionEvent ->
		{
		  int indexOfSelectedRow = this.table.getSelectedRow();
		  
		  if (indexOfSelectedRow != -1) 
		  {
			  int realIndex = this.table.convertRowIndexToModel(indexOfSelectedRow);
			  DataRow selectedRow = this.dataModule.getTasksDataTableModel().getRow(realIndex);
			  
			  EditTaskDialog editDialog = new EditTaskDialog();
			  editDialog.setLocationRelativeTo(null);
			  editDialog.setTitle("Edit Task");
			  editDialog.setData(selectedRow);
			  editDialog.setVisible(true);
			  
			  if (!editDialog.wasDialogCancelled()) 
			  {
			      selectedRow = editDialog.getData();       
			  }
			  
		  }
		  
		});
		
		
		JButton btnDeleteTask = new JButton("Delete Task");
		btnDeleteTask.setBounds(336, 13, 109, 25);
		frame.getContentPane().add(btnDeleteTask);
		
		
		btnDeleteTask.addActionListener(actionEvent -> 
		{
		    int indexOfSelectedRow = this.table.getSelectedRow();
		    
		    if (indexOfSelectedRow != -1) 
		    {
		    	if (JOptionPane.showConfirmDialog(frame, "Are you sure you wan to delete this task?", 
		                "Delete Task", JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) 
		    	{
		    		int realIndex = this.table.convertRowIndexToModel(indexOfSelectedRow);
		            this.dataModule.deleteRow(realIndex);
		    	}
		    }
		});
		
		
		JButton btnApplyUpdates = new JButton("Apply Updates");
		btnApplyUpdates.setBounds(457, 13, 125, 25);
		frame.getContentPane().add(btnApplyUpdates);
		
		
		btnApplyUpdates.addActionListener(actionEvent -> 
		{
			  this.dataModule.applyChanges();
		});
		
		
		textFieldFilter = new JTextField();
		textFieldFilter.setBounds(12, 51, 433, 22);
		frame.getContentPane().add(textFieldFilter);
		textFieldFilter.setColumns(10);
		
		//JButton btnApplyFilter = new JButton("Apply Filter");
		JButton btnApplyFilter = new JButton("Search Note");
		btnApplyFilter.setBounds(457, 51, 125, 25);
		frame.getContentPane().add(btnApplyFilter);
		
		
		btnApplyFilter.addActionListener(actionEvent -> 
		{
		    applyFilterToTable(this.textFieldFilter.getText());
		});
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 86, 570, 166);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scrollPane.setViewportView(table);
		
		this.dataModule = new DataModule();
	    table.setModel(this.dataModule.getTasksDataTableModel());
	}
	
	private void applyFilterToTable(String textToFilterBy) {
	    RowFilter<TableModel, Object> rf = null;
	    TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(this.table.getModel());
	    
	    if (textToFilterBy.length() != 0) {
	        table.setRowSorter(sorter);

	        try {
	            rf = RowFilter.regexFilter(textToFilterBy);
	        } catch (java.util.regex.PatternSyntaxException e) {
	            System.out.printf("PatternSyntaxException: %s\n", e);
	            return;
	        }
	        
	        sorter.setRowFilter(rf);
	    } else {
	        table.setRowSorter(null);
	    }
	}
}
