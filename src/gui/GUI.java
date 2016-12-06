package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JDialog;


public class GUI {

	private JFrame frame;
	
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
	private JLabel lblStepLogin;
	private JButton btnLogin;
	
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
					window.frame.setResizable(false);
					window.frame.setTitle("Integration Assignment");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 560, 314);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblStep1 = new JLabel("Step 1: Check Payroll Load Okay");
		lblStep1.setBounds(38, 69, 164, 14);
		frame.getContentPane().add(lblStep1);
		
		lblStep2 = new JLabel("Step 2: Process Delimited Text File");
		lblStep2.setBounds(38, 103, 178, 14);
		frame.getContentPane().add(lblStep2);
		
		lblDelimitedFile = new JLabel("Delimited File:");
		lblDelimitedFile.setBounds(38, 136, 128, 14);
		frame.getContentPane().add(lblDelimitedFile);
		
		lblStep3 = new JLabel("Step 3: Perform Month End");
		lblStep3.setBounds(38, 168, 178, 14);
		frame.getContentPane().add(lblStep3);
		
		lblStep4 = new JLabel("Step 4: Export Data");
		lblStep4.setBounds(38, 202, 178, 14);
		frame.getContentPane().add(lblStep4);
		
		delimitedFileTextField = new JTextField();
		delimitedFileTextField.setBounds(218, 133, 289, 20);
		frame.getContentPane().add(delimitedFileTextField);
		delimitedFileTextField.setColumns(10);
		
		btnCheck = new JButton("Check");
		btnCheck.addActionListener(new MyActionListener());
		btnCheck.setBounds(218, 65, 164, 23);
		frame.getContentPane().add(btnCheck);
		
		btnProcess = new JButton("Process");
		btnProcess.addActionListener(new MyActionListener());
		btnProcess.setBounds(218, 99, 164, 23);
		frame.getContentPane().add(btnProcess);
		
		btnPerform = new JButton("Perform");
		btnPerform.addActionListener(new MyActionListener());
		btnPerform.setBounds(218, 164, 164, 23);
		frame.getContentPane().add(btnPerform);
		
		btnExport = new JButton("Export");
		btnExport.addActionListener(new MyActionListener());
		btnExport.setBounds(218, 198, 164, 23);
		frame.getContentPane().add(btnExport);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new MyActionListener());
		btnClose.setBounds(445, 242, 89, 23);
		frame.getContentPane().add(btnClose);
		
		lblStepLogin = new JLabel("Step 1: Login");
		lblStepLogin.setBounds(38, 35, 164, 14);
		frame.getContentPane().add(lblStepLogin);
		
		btnLogin = new JButton("Login");
		btnLogin.addActionListener(new MyActionListener());
		btnLogin.setBounds(218, 31, 164, 23);
		frame.getContentPane().add(btnLogin);
	}
	
	private class MyActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
//			if(e.getSource() == btnLogin){
//				
//			}
			
			if(e.getSource() == btnCheck) {
				
			}
			
			if(e.getSource() == btnProcess) {
				
			}
			
			if(e.getSource() == btnPerform) {

			}
			
			if(e.getSource() == btnExport) {
				
			}
			
			if(e.getSource() == btnClose) {
				System.exit(0);
			}
		}
	}
}
