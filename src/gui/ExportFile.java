package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import javax.swing.JButton;

public class ExportFile {

	private JFrame frame;
	private JTextField fieldPath;
	private JTextField fieldName;
	private JTextField fieldAlias;
	
	private JButton btnSubmit;
	
	private  Connection conn;
	private Statement stmt;
	private ResultSet rs;
	
	private String username;
	private String password;
	private JTextField messageField;

	/**
	 * Create the application.
	 */
	public ExportFile(Connection conn,String username, String password) {
		this.conn = conn;
		this.username = username;
		this.password = password;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblDirectoryPath = new JLabel("Directory Path:");
		lblDirectoryPath.setBounds(60, 61, 88, 14);
		frame.getContentPane().add(lblDirectoryPath);
		
		JLabel lblNameOfFile = new JLabel("Name of File:");
		lblNameOfFile.setBounds(60, 99, 88, 14);
		frame.getContentPane().add(lblNameOfFile);
		
		JLabel lblAlias = new JLabel("Alias:");
		lblAlias.setBounds(60, 135, 88, 14);
		frame.getContentPane().add(lblAlias);
		
		fieldPath = new JTextField();
		fieldPath.setBounds(158, 58, 198, 20);
		frame.getContentPane().add(fieldPath);
		fieldPath.setColumns(10);
		
		fieldName = new JTextField();
		fieldName.setColumns(10);
		fieldName.setBounds(158, 96, 198, 20);
		frame.getContentPane().add(fieldName);
		
		fieldAlias = new JTextField();
		fieldAlias.setColumns(10);
		fieldAlias.setBounds(158, 132, 198, 20);
		frame.getContentPane().add(fieldAlias);
		
		btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(175, 198, 89, 23);
		frame.getContentPane().add(btnSubmit);
		btnSubmit.addActionListener(new MyActionListener());
		
		messageField = new JTextField();
		messageField.setBounds(175, 163, 86, 20);
		frame.getContentPane().add(messageField);
		messageField.setColumns(10);
	}
	
	
	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		
			if(e.getSource() == btnSubmit) {
				
				if(!fieldPath.getText().isEmpty() && !fieldName.getText().isEmpty() && !fieldAlias.getText().isEmpty()) {
					
					exportData(fieldAlias.getText(),fieldPath.getText(), fieldAlias.getText());
					JOptionPane.showMessageDialog(null,"Export Complete!");
					
				} else {
					messageField.setText("Export Failed for some reason. Please try again later.");
				}
			}
			
		}
		
		
	}
	
	private void exportData(String alias, String directory, String fileName){
		try {
			stmt = conn.createStatement();
			String query = "CREATE OR REPLACE DIRECTORY " + alias +  " AS '" + directory + "'";
			stmt.executeUpdate(query);
			String sql = "{call proc_populate_export_file(?,?)}";
			CallableStatement statement = conn.prepareCall(sql);
			statement.setString(1, alias);
			statement.setString(2, fileName);
			statement.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
