package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

//import net.miginfocom.swing.MigLayout;


public class login {

	private JFrame frmLogin;
	
	//TextFields
	private JTextField userName;
	private JTextField password;
	
	//Labels
	private JLabel errorMessage;
	
	//Attributes
	private static String user;
	private static String pass;
	
	private static  Connection conn;
	private Statement stmt;
	private ResultSet rs;


	//connection
	final static String HOSTNAME = "localhost";
	final static String PORT = "1521";


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login window = new login();
					window.frmLogin.setVisible(true);
					String[] args = {user, pass};
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public login() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmLogin = new JFrame();
		frmLogin.setTitle("Login");
		frmLogin.setBounds(100, 100, 312, 142);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLogin.getContentPane().setLayout(null);
		
		errorMessage = new JLabel("");
		errorMessage.setBounds(0, 0, 0, 0);
		frmLogin.getContentPane().add(errorMessage);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(101, 70, 85, 23);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
					user = userName.getText().trim();
					pass = password.getText().trim();
					if(user == null || user.equals("")){
						errorMessage.setText("Please input a user.");
					}
					else if(pass == null || pass.equals("")){
						errorMessage.setText("Please input a password.");
					}
					else{
						getUser(user, pass);
					}
			}
		});
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setBounds(48, 11, 71, 20);
		frmLogin.getContentPane().add(lblUsername);
		
		userName = new JTextField();
		userName.setBounds(129, 11, 110, 20);
		frmLogin.getContentPane().add(userName);
		userName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(48, 36, 71, 20);
		frmLogin.getContentPane().add(lblPassword);
		
		password = new JTextField();
		password.setBounds(129, 36, 110, 20);
		frmLogin.getContentPane().add(password);
		password.setColumns(10);
		frmLogin.getContentPane().add(btnLogin);
	}

	protected void getUser(String username, String password) {
		try {
		   String url = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT;
			conn = DriverManager.getConnection(url, username, password);
//			errorMessage.setText("It works ");]
			GUI main = new GUI(conn, username, password);
			main.frame.setVisible(true);
			main.frame.setResizable(false);
			main.frame.setTitle("Integration Assignment");
			frmLogin.dispose();
			
		} catch (SQLException e) {
			System.out.println("YO!" + e.toString());
			errorMessage.setText("Invalid credentials. Please log in with a valid user.");
		}
	}

}
