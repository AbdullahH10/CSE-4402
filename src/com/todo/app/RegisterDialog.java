package com.todo.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.swing.SwingConstants;
import javax.swing.JTextField;

public class RegisterDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUsername;
	private JTextField textFieldPassword;
	
	private DataModule dataModule;
	
	String username;
	String password;
	
	Connection c = null;
    Statement stmt = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RegisterDialog dialog = new RegisterDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RegisterDialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblGivaAUsername = new JLabel("Giva a username and a unique pssword.");
			lblGivaAUsername.setHorizontalAlignment(SwingConstants.CENTER);
			lblGivaAUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblGivaAUsername.setBounds(12, 13, 408, 43);
			contentPanel.add(lblGivaAUsername);
		}
		{
			JLabel lblUsername = new JLabel("Username:");
			lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblUsername.setBounds(12, 69, 107, 25);
			contentPanel.add(lblUsername);
		}
		{
			textFieldUsername = new JTextField();
			textFieldUsername.setBounds(131, 73, 289, 22);
			contentPanel.add(textFieldUsername);
			textFieldUsername.setColumns(10);
		}
		{
			JLabel lblPassword = new JLabel("Password:");
			lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
			lblPassword.setBounds(12, 107, 107, 25);
			contentPanel.add(lblPassword);
		}
		{
			textFieldPassword = new JTextField();
			textFieldPassword.setBounds(131, 108, 289, 22);
			contentPanel.add(textFieldPassword);
			textFieldPassword.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Register");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.addActionListener(actionEvent -> 
				{
					  this.dispose();
					  username=textFieldUsername.getText().toString();
					  password=textFieldPassword.getText().toString();
					  
					  try {
					         Class.forName("org.sqlite.JDBC");
					         c = DriverManager.getConnection("jdbc:sqlite:todo.db");
					         System.out.println("Opened database successfully");

					         stmt = c.createStatement();
					         String sql = "INSERT INTO Users " +
					                        "(Name,Password)" +
					                        " VALUES (\"" +
					                        this.username +"\","+
					                        this.password +
					                        ");";
					         stmt.executeUpdate(sql);
					         stmt.close();
					         c.close();
					      }
					  
					  catch ( Exception e ) 
					  {
					         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
					         System.exit(0);
					  }
					  
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

}
