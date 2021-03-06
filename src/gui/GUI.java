package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.CallableStatement;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//import persistence.clientale.String;

import javax.swing.JButton;
import javax.swing.JDialog;	

import java.sql.Connection;
import java.sql.DriverManager;
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
	
	private JDialog Login;

	
	private Connection conn;
	final static String HOSTNAME = "localhost";
	final static String PORT = "1521";
	

	/**
	 * Create the application.
	 */
	public GUI(Connection conn) {
		this.conn = conn;
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
	}
	
	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource() == btnCheck) {
				System.out.println("This is " + checkFlag());
			}
			
			if(e.getSource() == btnProcess) {
				processDelimitedFile();
				
			}
			
			if(e.getSource() == btnPerform) {
				performMonthEnd();
			}
			
			if(e.getSource() == btnExport) {
				exportData();
			}
			
			if(e.getSource() == btnClose) {
				System.exit(0);
			}
		}
	}

	public Boolean checkFlag() {
		try {
			String sql = "{ ? = call IsPayrollRunning() }";
			CallableStatement statement = conn.prepareCall(sql);
			statement.registerOutParameter(1, java.sql.Types.CHAR);  
			statement.executeUpdate();
			String inUse = statement.getString(1);
			if (inUse.equals("N")) // Means payroll is not in use
				return false;
		} catch (Exception e) {

		}
		return true;		
	}

	public void exportData() {
		// TODO Auto-generated method stub
		
	}

	public void performMonthEnd() {
		// TODO Auto-generated method stub
		
	}

	public void processDelimitedFile() {
		// TODO Auto-generated method stub
		
	}
}
