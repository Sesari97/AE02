package es.Florida;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSplitPane;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;


public class AE02 {

	private JFrame frame;
	private JTextField txtUserInput;
	private JTextField txtPasswordInput;
	Connection con;
	private JTextField txtQuery;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AE02 window = new AE02();
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
	public AE02() {
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
		
		JButton btnConnect = new JButton("Conectar");
		btnConnect.setBounds(168, 112, 89, 23);
		frame.getContentPane().add(btnConnect);
		
		txtUserInput = new JTextField();
		txtUserInput.setBounds(25, 81, 178, 20);
		frame.getContentPane().add(txtUserInput);
		txtUserInput.setColumns(10);
		txtUserInput.setVisible(false);
		
		txtPasswordInput = new JTextField();
		txtPasswordInput.setColumns(10);
		txtPasswordInput.setBounds(25, 137, 178, 20);
		frame.getContentPane().add(txtPasswordInput);
		txtPasswordInput.setVisible(false);
		
		JLabel lblUser = new JLabel("Usuario");
		lblUser.setBounds(25, 56, 94, 14);
		frame.getContentPane().add(lblUser);
		lblUser.setVisible(false);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(25, 116, 94, 14);
		frame.getContentPane().add(lblPassword);
		lblPassword.setVisible(false);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(69, 168, 89, 23);
		frame.getContentPane().add(btnLogin);
		btnLogin.setVisible(false);
		
		JButton btnExplainDB = new JButton("Estructura BBDD");
		btnExplainDB.setBounds(30, 22, 173, 23);
		frame.getContentPane().add(btnExplainDB);
		btnExplainDB.setVisible(false);
		
		JButton btnExplainTitles = new JButton("Estrucutra tabla titles");
		btnExplainTitles.setBounds(30, 52, 173, 23);
		frame.getContentPane().add(btnExplainTitles);
		btnExplainTitles.setVisible(false);
		
		JButton btnContentTitles = new JButton("Mostrar contenido titles");
		btnContentTitles.setBounds(30, 80, 173, 23);
		frame.getContentPane().add(btnContentTitles);
		btnContentTitles.setVisible(false);
		
		txtQuery = new JTextField();
		txtQuery.setBounds(236, 23, 188, 134);
		frame.getContentPane().add(txtQuery);
		txtQuery.setColumns(10);
		txtQuery.setVisible(false);
		
		JButton btnLaunchQuery = new JButton("Lanzar SQL");
		btnLaunchQuery.setBounds(287, 168, 89, 23);
		frame.getContentPane().add(btnLaunchQuery);
		btnLaunchQuery.setVisible(false);
		
		JButton btncloseConnection = new JButton("Cerrar conexión");
		btncloseConnection.setBounds(145, 227, 155, 23);
		frame.getContentPane().add(btncloseConnection);
		btncloseConnection.setVisible(false);
		
		
		
		btnConnect.addActionListener(new ActionListener() {
			
			/**
			 * Metodo para conectarse a la base de datos y cargar el xml
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					
					String user = "";
					String pass = "";
					String url = "";
					
					File ficheroXML = new File ("src/Información.xml");
					System.out.println(ficheroXML.getAbsolutePath());
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
					//an instance of builder to parse the specified xml file  
					DocumentBuilder db = dbf.newDocumentBuilder();  
					Document doc = db.parse(ficheroXML);  
					doc.getDocumentElement().normalize();
					
					NodeList nodeList = doc.getElementsByTagName("usuario");
					
					for (int i = 0; i < nodeList.getLength(); i++) {
						
						Node node = nodeList.item(i);
						
						if (node.getNodeType() == Node.ELEMENT_NODE) {
							Element element = (Element) node;
							System.out.println("DB Info: " + element.getElementsByTagName("url").item(0).getTextContent() + " ----- " + element.getElementsByTagName("user").item(0).getTextContent() + " ------- ");
							url = element.getElementsByTagName("url").item(0).getTextContent();
							user = element.getElementsByTagName("user").item(0).getTextContent();
							pass = element.getElementsByTagName("pass").item(0).getTextContent();
						}
						
					}
					
					Class.forName("com.mysql.cj.jdbc.Driver");
					con = DriverManager.getConnection(url, user, pass);
					
					if (con.isValid(5)) {
						 JOptionPane.showInternalMessageDialog(null,
			                    "Usuario del XML recogido correctamente.\nConexión realizada con éxito", "Atencion", JOptionPane.INFORMATION_MESSAGE);
						 lblUser.setVisible(true);
						 lblPassword.setVisible(true);
						 btnLogin.setVisible(true);
						 btnConnect.setVisible(false);
						 txtUserInput.setVisible(true);
						 txtPasswordInput.setVisible(true);
					}
					else {
						JOptionPane.showInternalMessageDialog(null,
			                    "Error al intentar conectarse", "Atencion", JOptionPane.INFORMATION_MESSAGE);
					}
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				
			}
		});
		
		
		
		btnLogin.addActionListener(new ActionListener() {
			
			/**
			 * Metodo para conectarse con el usuario y la contrasenya
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String userLogin = txtUserInput.getText();
				String userPassword = txtPasswordInput.getText();
				
				if (userLogin == "" || userPassword == "") {
					 JOptionPane.showInternalMessageDialog(null,
		                    "El campo usuario y contraseña deben estar rellenos.", "Atencion", JOptionPane.INFORMATION_MESSAGE);
				}
				else {
					try {
						MessageDigest md5 = MessageDigest.getInstance("MD5");
						md5.update(StandardCharsets.UTF_8.encode(userPassword));
						String encodedPassword = String.format("%032x", new BigInteger(1, md5.digest()));
						
						Statement st = con.createStatement(
							    ResultSet.TYPE_SCROLL_INSENSITIVE, 
							    ResultSet.CONCUR_READ_ONLY);
						ResultSet rs = st.executeQuery("SELECT * FROM users WHERE user = '" + userLogin + "' AND pass = '" + encodedPassword + "'");
						rs.last();
					    int size = rs.getRow();
					    rs.beforeFirst();
						if (size > 0) {
							txtUserInput.setVisible(false);
							txtPasswordInput.setVisible(false);
							btnLogin.setVisible(false);
							lblUser.setVisible(false);
							lblPassword.setVisible(false);
							btnExplainDB.setVisible(true);
							btnExplainTitles.setVisible(true);
							btnContentTitles.setVisible(true);
							btnLaunchQuery.setVisible(true);
							txtQuery.setVisible(true);
							btncloseConnection.setVisible(true);
						}
						rs.close();
						st.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				

			}
		});
		
	
		btnExplainDB.addActionListener(new ActionListener() {
			
			/**
			 * Metodo para mostrar la informacion de la base de datos
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					DatabaseMetaData metaData = con.getMetaData();
					ResultSet rs = metaData.getTables("books", null, null, null);
					StringBuilder sb = new StringBuilder();
					while(rs.next()) {
						sb.append("Nombre: " + rs.getString("Table_NAME") + "\n\n");
					}
					
					JOptionPane.showInternalMessageDialog(null,
		                    sb, "ESTRUCTURA BD", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		btnExplainTitles.addActionListener(new ActionListener() {
			
			/**
			 * Metodo para mostrar la estructura de una tabla
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				Statement st;
				try {
					st = con.createStatement();
					ResultSet rs = st.executeQuery("EXPLAIN titles;");
					StringBuilder tableInfo = new StringBuilder();
					while(rs.next()) {
						tableInfo.append("Field: " + rs.getString(1) + " || " + "Type: " + rs.getString(2) + " || " + "Null: " + rs.getString(3) + " || " +
							"Key: " + rs.getString(4) + " || " + "Default: " + rs.getString(5) + " || " + "Extra: " + rs.getString(6) + "\n\n");
					}
					
					JOptionPane.showInternalMessageDialog(null,
		                    tableInfo, "ESTRUCTURA TABLA TITLES", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		

		btnContentTitles.addActionListener(new ActionListener() {
			
			
			/**
			 * Metodo para mostrar el contenido de una tabla
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				Statement st;
				try {
					st = con.createStatement();
					ResultSet rs = st.executeQuery("SELECT * FROM TITLES;");
					StringBuilder sb = new StringBuilder();
					while(rs.next()) {
						sb.append("Titulo: " + rs.getString(2) + " // " + "Autor: " + rs.getString(3) + " // " + "Año: " + rs.getString(4) + 
								" // " + "Editorial: " + rs.getString(5) + " // " + "Paginas: " + rs.getString(6) + "\n\n");
					}
					
					JOptionPane.showInternalMessageDialog(null,
		                    sb, "LIBROS", JOptionPane.INFORMATION_MESSAGE);
					
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		
		btnLaunchQuery.addActionListener(new ActionListener() {
			
			/**
			 * Metodo para lanzar una query a la base de datos
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				String query = txtQuery.getText();
				
				if ((query.contains("SELECT") || query.contains("select")) || (query.contains("INSERT") || query.contains("insert")) || 
						(query.contains("UPDATE") || query.contains("update")) || (query.contains("DELETE") || query.contains("delete"))) {
					
					if (query.contains("SELECT") || query.contains("select")) {
						Statement st;
						try {
							st = con.createStatement();
							ResultSet rs = st.executeQuery(query);
							StringBuilder sb = new StringBuilder();
							ResultSetMetaData rsm = rs.getMetaData();
							int columns = rsm.getColumnCount();
							while(rs.next()) {
								for (int i = 1; i <= columns; i++) {
							        if (i > 1) System.out.print(",  ");
							        String columnValue = rs.getString(i);
							        sb.append(rsm.getColumnName(i) + ": " + columnValue + " // ");
							    }
								sb.append("\n\n");
							}
							
							JOptionPane.showInternalMessageDialog(null,
				                    sb, "LIBROS", JOptionPane.INFORMATION_MESSAGE);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						
						int input = JOptionPane.showConfirmDialog(null, "¿Desear lanzar la query?", "Atención", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
						if (input == 0) {							
							PreparedStatement ps;
							try {
								ps = con.prepareStatement(query);
								int resultadoActualizar = ps.executeUpdate();
								if (resultadoActualizar == 1) {
									JOptionPane.showInternalMessageDialog(null,
											"Query ejecutada correcamente", "Query Status", JOptionPane.INFORMATION_MESSAGE);
								} else {
									JOptionPane.showInternalMessageDialog(null,
											"Ha ocurrido un error al ejecutar la query", "Query Status", JOptionPane.INFORMATION_MESSAGE);
								}
								ps.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						
					}
				} else {
					JOptionPane.showInternalMessageDialog(null,
		                    "La query no es correcta", "Atencion", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		
		
		btncloseConnection.addActionListener(new ActionListener() {
			
			/**
			 * Metodo cerrar la conexion de la base de datos
			 * @param e
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					con.close();
					int input = JOptionPane.showConfirmDialog(null, "¿Seguro que quieres cerrar la conexión?", "Atención", JOptionPane.YES_NO_OPTION);
					if (input == 0) {						
						btnExplainDB.setVisible(false);
						btnExplainTitles.setVisible(false);
						btnContentTitles.setVisible(false);
						btnLaunchQuery.setVisible(false);
						txtQuery.setVisible(false);
						btncloseConnection.setVisible(false);
						btnConnect.setVisible(true);
						JOptionPane.showConfirmDialog(null, "Conexión cerrada", "Atención", JOptionPane.CLOSED_OPTION);
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
	}
}
