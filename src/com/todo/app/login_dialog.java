package com.todo.app;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;

public class login_dialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldUsername;
	private JTextField passwordFieldPassword;
	
	private boolean loginCancelled=true;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			login_dialog dialog = new login_dialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public login_dialog() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblUsername.setBounds(12, 19, 108, 16);
		contentPanel.add(lblUsername);
		
		textFieldUsername = new JTextField();
		textFieldUsername.setBounds(132, 13, 288, 34);
		contentPanel.add(textFieldUsername);
		textFieldUsername.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPassword.setBounds(12, 80, 108, 16);
		contentPanel.add(lblPassword);
		
		passwordFieldPassword = new JTextField();
		passwordFieldPassword.setBounds(132, 74, 288, 34);
		contentPanel.add(passwordFieldPassword);
		passwordFieldPassword.setColumns(10);
		
		JButton btnRegister = new JButton("Register First!");
		btnRegister.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnRegister.setBounds(12, 139, 408, 46);
		contentPanel.add(btnRegister);
		btnRegister.addActionListener(actionEvent ->
		{
			this.dispose();
			RegisterDialog registerDialog = new RegisterDialog();
			registerDialog.setVisible(true);
			
		}
				);
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				
				okButton.addActionListener(actionEvent->
				{
					this.dispose();
					loginCancelled=false;
				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
				cancelButton.addActionListener(actionEvent->
				{
					this.dispose();
				});
			}
		}
		
		setModal(true);
	}
	
	public boolean getLoginCancelled()
	{
		return this.loginCancelled;
	}
	
	public String getUsername()
	{
		return this.textFieldUsername.getText().toString();
	}
	
	public String getPassword()
	{
		return this.passwordFieldPassword.getText().toString();
	}
}
