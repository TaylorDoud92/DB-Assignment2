package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.CallableStatement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import oracle.jdbc.OracleTypes;

//import persistence.clientale.String;

import javax.swing.JButton;
import javax.swing.JDialog;	

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.sql.ResultSet;


public class GUI {

	JFrame frame;
	
	//GUI TextFields
	private JTextField delimitedFileTextField;
	
	//GUI Labels
	private JLabel lblStep1;
	private JLabel lblStep2;
	private JLabel lblStep3;
	private JLabel lblStep4;
	private JLabel lblDelimitedFile;
	
	//GUI Buttons
	private JButton btnCheck;
	private JButton btnProcess;
	private JButton btnPerform;
	private JButton btnExport;
	private JButton btnClose;

	
	private Connection conn;
	final static String HOSTNAME = "localhost";
	final static String PORT = "1521";
	private JLabel lblMessage;
	
	private String username;
	private String password;
	

	/**
	 * Create the application.
	 */
	public GUI(Connection conn,String username, String password) {
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
		frame.setBounds(100, 100, 634, 274);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblStep1 = new JLabel("Step 1: Check Payroll Load Okay");
		lblStep1.setBounds(34, 17, 200, 14);
		frame.getContentPane().add(lblStep1);
		
		lblStep2 = new JLabel("Step 2: Process Delimited Text File");
		lblStep2.setBounds(34, 51, 213, 14);
		frame.getContentPane().add(lblStep2);
		
		lblDelimitedFile = new JLabel("Delimited File:");
		lblDelimitedFile.setBounds(34, 84, 128, 14);
		frame.getContentPane().add(lblDelimitedFile);
		
		lblStep3 = new JLabel("Step 3: Perform Month End");
		lblStep3.setBounds(34, 116, 178, 14);
		frame.getContentPane().add(lblStep3);
		
		lblStep4 = new JLabel("Step 4: Export Data");
		lblStep4.setBounds(34, 150, 178, 14);
		frame.getContentPane().add(lblStep4);
		
		delimitedFileTextField = new JTextField();
		delimitedFileTextField.setBounds(288, 81, 289, 20);
		frame.getContentPane().add(delimitedFileTextField);
		delimitedFileTextField.setColumns(10);
		
		btnCheck = new JButton("Check");
		btnCheck.addActionListener(new MyActionListener());
		btnCheck.setBounds(288, 13, 164, 23);
		frame.getContentPane().add(btnCheck);
		
		btnProcess = new JButton("Process");
		btnProcess.addActionListener(new MyActionListener());
		btnProcess.setBounds(288, 47, 164, 23);
		frame.getContentPane().add(btnProcess);
		
		btnPerform = new JButton("Perform");
		btnPerform.addActionListener(new MyActionListener());
		btnPerform.setBounds(288, 112, 164, 23);
		frame.getContentPane().add(btnPerform);
		
		btnExport = new JButton("Export");
		btnExport.addActionListener(new MyActionListener());
		btnExport.setBounds(288, 146, 164, 23);
		frame.getContentPane().add(btnExport);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new MyActionListener());
		btnClose.setBounds(515, 190, 89, 23);
		frame.getContentPane().add(btnClose);
		
		lblMessage = new JLabel("");
		lblMessage.setBounds(101, 194, 351, 14);
		frame.getContentPane().add(lblMessage);
	}
	
	private class MyActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == btnCheck) {
				if(checkFlag()){
					setProcessFlagFalse();
					lblMessage.setText("Payroll is good to go!");
					btnProcess.setEnabled(true);
					btnPerform.setEnabled(true);
					btnExport.setEnabled(true);
					btnClose.setEnabled(true);
				} else {
					lblMessage.setText("Payroll is in use and cannot be processed at this time.");
					btnProcess.setEnabled(false);
					btnPerform.setEnabled(false);
					btnExport.setEnabled(false);
					btnClose.setEnabled(false);
				}
			}
			
			if(e.getSource() == btnProcess) {
				processDelimitedFile();
				setProcessFlagTrue();
				
			}
			
			if(e.getSource() == btnPerform) {
				performMonthEnd();
			}
			
			if(e.getSource() == btnExport) {

				ExportFile file = new ExportFile(conn,username,password);
				file.frame.setVisible(true);
				file.frame.setResizable(false);
				file.frame.setTitle("Integration Assignment");
			}
			
			if(e.getSource() == btnClose) {
				System.exit(0);
			}
		}
	}
	public void performMonthEnd() {
		if(checkMonthEndFlag()){
			
			lblMessage.setText("Month end is good to go!");
			changeMonthEndFlagToFalse();
			zeroAccounts();
			changeMonthEndFlagToTrue();
		
		} else {
			lblMessage.setText("Month end is NOT good to go!");
		}
		
	}

	private void processDelimitedFile() {
		String fileLocation;
		String cmdLine;
		Process proc;
		int exitValue = 0;
		
		
		fileLocation = delimitedFileTextField.getText();
		
		
		int nameIndex = fileLocation.lastIndexOf("/");
		
		if(fileLocation == null){
			
			nameIndex = fileLocation.lastIndexOf("\\");
		}			
		
		String fileName = fileLocation.substring(nameIndex+1, fileLocation.indexOf('.', nameIndex));
		fileLocation.substring(0,nameIndex);
		
	    try {
			PrintWriter pw = new PrintWriter(fileName + ".ctl");
			pw.println("LOAD DATA");
			pw.println("INFILE" +"'"+fileLocation+"'");
			pw.println("REPLACE");
			pw.println("INTO TABLE PAYROLL_LOAD");
			pw.println("FIELDS TERMINATED BY ';'");
			pw.println("(Payroll_date");
			pw.println("Employee_id");
			pw.println("Amount");
			pw.println("Status)");
			pw.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    
		
		cmdLine = "sqlldr "+ username + "/" + password+", control="+ fileName +".ctl";
		
		
	try {
		Runtime rt = Runtime.getRuntime();
	
			proc = rt.exec(cmdLine);
			exitValue = proc.waitFor();
		} catch (InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(exitValue==0){
			
		}
		
	}
	
//	private void exportData(String alias, String directory, String fileName){
//		Statement stmt;
//		try {
//			stmt = conn.createStatement();
//			String query = "CREATE OR REPLACE DIRECTORY " + alias +  " AS '" + directory + "'";
//			stmt.executeUpdate(query);
//			String sql = "{call proc_populate_export_file(?,?)}";
//			CallableStatement statement = conn.prepareCall(sql);
//			statement.setString(1, alias);
//			statement.setString(2, fileName);
//			statement.execute();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
//	
	private boolean checkFlag(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{? = call func_check_payroll()}");
			cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
			cstmt.executeUpdate();
			char result = cstmt.getString(1).charAt(0);
			if(result == 'Y')
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Prepared Call failed");
			e.printStackTrace();
		}
		return false;		
	}
	
	private void setProcessFlagFalse(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{call proc_set_flag_false()}"); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	private void setProcessFlagTrue(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{call proc_set_flag_true()}"); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkMonthEndFlag(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{? = call func_check_month_end()}");
			cstmt.registerOutParameter(1, OracleTypes.VARCHAR);
			char result = cstmt.getString(1).charAt(0);
			if(result == 'Y')
				return true;
			else
				return false;
		} catch (SQLException e) {
			System.out.println("Prepared call failed");
			e.printStackTrace();
		}
		return false;
	}
	
	private void changeMonthEndFlagToFalse(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{call proc_set_monthendflag_false()}"); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void changeMonthEndFlagToTrue(){
		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{call proc_set_monthendflag_false()}"); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void zeroAccounts(){

		CallableStatement cstmt;
		try {
			cstmt = conn.prepareCall("{call proc_balance_rev()}"); 
			cstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}
}
