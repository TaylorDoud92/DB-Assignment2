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
	private JTextField userName;
	private JTextField password;
	private String user;
	private String pass;
	private JLabel errorMessage;
	
	private static  Connection conn;
	private Statement stmt;
	private ResultSet rs;


	
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
		frmLogin.setBounds(100, 100, 211, 141);
		frmLogin.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{1, 0, 0};
		gridBagLayout.rowHeights = new int[]{1, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		frmLogin.getContentPane().setLayout(gridBagLayout);
		
		errorMessage = new JLabel("");
		GridBagConstraints gbc_errorMessage = new GridBagConstraints();
		gbc_errorMessage.insets = new Insets(0, 0, 5, 5);
		gbc_errorMessage.anchor = GridBagConstraints.NORTHWEST;
		gbc_errorMessage.gridx = 0;
		gbc_errorMessage.gridy = 0;
		frmLogin.getContentPane().add(errorMessage, gbc_errorMessage);
		
		JButton btnLogin = new JButton("Login");
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
		GridBagConstraints gbc_lblUsername = new GridBagConstraints();
		gbc_lblUsername.insets = new Insets(0, 0, 5, 5);
		gbc_lblUsername.fill = GridBagConstraints.BOTH;
		gbc_lblUsername.gridx = 0;
		gbc_lblUsername.gridy = 1;
		frmLogin.getContentPane().add(lblUsername, gbc_lblUsername);
		
		userName = new JTextField();
		GridBagConstraints gbc_userName = new GridBagConstraints();
		gbc_userName.insets = new Insets(0, 0, 5, 0);
		gbc_userName.fill = GridBagConstraints.BOTH;
		gbc_userName.gridx = 1;
		gbc_userName.gridy = 1;
		frmLogin.getContentPane().add(userName, gbc_userName);
		userName.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password:");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.fill = GridBagConstraints.BOTH;
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 2;
		frmLogin.getContentPane().add(lblPassword, gbc_lblPassword);
		
		password = new JTextField();
		GridBagConstraints gbc_password = new GridBagConstraints();
		gbc_password.insets = new Insets(0, 0, 5, 0);
		gbc_password.fill = GridBagConstraints.BOTH;
		gbc_password.gridx = 1;
		gbc_password.gridy = 2;
		frmLogin.getContentPane().add(password, gbc_password);
		password.setColumns(10);
		GridBagConstraints gbc_btnLogin = new GridBagConstraints();
		gbc_btnLogin.insets = new Insets(0, 0, 0, 5);
		gbc_btnLogin.fill = GridBagConstraints.BOTH;
		gbc_btnLogin.gridx = 0;
		gbc_btnLogin.gridy = 3;
		frmLogin.getContentPane().add(btnLogin, gbc_btnLogin);
	}

	protected void getUser(String username, String password) {
		try {
		   String url = "jdbc:oracle:thin:@" + HOSTNAME + ":" + PORT;
			conn = DriverManager.getConnection(url, username, password);
//			errorMessage.setText("It works ");]
			GUI main = new GUI(conn);
			main.frame.setVisible(true);
			main.frame.setResizable(false);
			main.frame.setTitle("Integration Assignment");
			frmLogin.dispose();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block

			System.out.println("YO!" + e.toString());
			errorMessage.setText("Invalid credentials. Please log in with a valid user.");
		}
	}

}
