package com.todo.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.remobjects.dataabstract.data.DataRow;

import javax.swing.JLabel;
import java.awt.Font;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

public class EditTaskDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldTask;
	private JTextField textFieldDueDate;
	private JTextArea textAreaDescription;
	private JComboBox comboBoxPriority;
	private JCheckBox chckbxCompleted;
	
	private DataRow selectedRowData;
	private boolean wasEditCancelled = true;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditTaskDialog dialog = new EditTaskDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditTaskDialog() {
		setBounds(100, 100, 884, 451);
		getContentPane().setLayout(null);
		contentPanel.setBounds(12, 0, 842, 354);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		JLabel lblTask = new JLabel("Task");
		lblTask.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTask.setBounds(12, 13, 56, 16);
		contentPanel.add(lblTask);
		
		textFieldTask = new JTextField();
		textFieldTask.setBounds(109, 13, 721, 22);
		contentPanel.add(textFieldTask);
		textFieldTask.setColumns(10);
		
		JLabel lblDueDate = new JLabel("Due Date");
		lblDueDate.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDueDate.setBounds(12, 52, 85, 16);
		contentPanel.add(lblDueDate);
		
		textFieldDueDate = new JTextField();
		textFieldDueDate.setBounds(109, 48, 721, 22);
		contentPanel.add(textFieldDueDate);
		textFieldDueDate.setColumns(10);
		
		JLabel lblPriority = new JLabel("Priority");
		lblPriority.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPriority.setBounds(12, 81, 85, 22);
		contentPanel.add(lblPriority);
		
		comboBoxPriority = new JComboBox();
		comboBoxPriority.setFont(new Font("Tahoma", Font.PLAIN, 20));
		comboBoxPriority.setBounds(109, 83, 346, 22);
		contentPanel.add(comboBoxPriority);
		comboBoxPriority.addItem("Low");
		comboBoxPriority.addItem("Normal");
		comboBoxPriority.addItem("High");
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDescription.setBounds(12, 116, 100, 22);
		contentPanel.add(lblDescription);
		
		textAreaDescription = new JTextArea();
		textAreaDescription.setBounds(12, 151, 818, 163);
		contentPanel.add(textAreaDescription);
		
		chckbxCompleted = new JCheckBox("Completed");
		chckbxCompleted.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckbxCompleted.setBounds(12, 323, 164, 25);
		contentPanel.add(chckbxCompleted);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(12, 356, 842, 35);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.addActionListener(actionEvent -> 
				{
					  this.dispose();
					  wasEditCancelled = false;
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				cancelButton.addActionListener(actionEvent -> 
				{
					  this.dispose();
				});
			}
		}
		
		this.setModal(true);
	}
	
	public boolean wasDialogCancelled() 
	{
		  return wasEditCancelled;
	}
	
	public void setData(DataRow selectedRow) 
	{
		  selectedRowData = selectedRow;
		  
		  String taskName = (String) selectedRowData.getField("Task");
		  String taskDescription = (String) selectedRowData.getField("Details");
		  Date dueDate = (Date) selectedRowData.getField("DueDate");
		  Long priorityLevel = (Long) selectedRowData.getField("Priority");
		  long doneStatusAsLong = (Long) selectedRowData.getField("Done");
		  
		  Integer plvl = (int) (long) priorityLevel;
		      
		  textFieldTask.setText(taskName);
		  textAreaDescription.setText(taskDescription);
		  SimpleDateFormat simpleDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		  textFieldDueDate.setText(simpleDateFormatter.format(dueDate));
		  comboBoxPriority.setSelectedIndex(plvl-1);
		  
		  boolean doneStatusAsBoolean = doneStatusAsLong == 0? false: true;
		  chckbxCompleted.setSelected(doneStatusAsBoolean); 
	}
	
	public DataRow getData() {
		  String taskName = textFieldTask.getText();
		  String taskDescription = textAreaDescription.getText();
		  java.util.Date dueDate = (Date) selectedRowData.getField("DueDate");
		  
		  try {
		    dueDate = new SimpleDateFormat("yyyy-MM-dd").parse(textFieldDueDate.getText());
		  } catch (ParseException e) {
		    e.printStackTrace();
		  }
		  
		  Long priorityLevel = (long) (comboBoxPriority.getSelectedIndex()+1);
		  Boolean doneStatus = chckbxCompleted.isSelected();
		  Long doneStatusAsLong = (long) (doneStatus == true? 1 : 0);

		  selectedRowData.setField("Task", taskName);
		  selectedRowData.setField("Details", taskDescription);
		  selectedRowData.setField("DueDate", dueDate);
		  selectedRowData.setField("Priority", priorityLevel);
		  selectedRowData.setField("Done", doneStatusAsLong);
		      
		  return selectedRowData;
		}
}
