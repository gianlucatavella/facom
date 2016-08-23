/* ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
   ø  by Gianluca Tavella - gianluca.tavella@libero.it                       ø
   ø  Feb 2002                                                               ø
   ø  Facom - Fatture Commerciali                                            ø
   ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Classe Materiale: finestra con interfaccia grafica per la visualizzaione
 *				dei costi del materiale
 *
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
/*
 * import dei package necessari
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import island.swing.*;

class Materiale extends JFrame{
	private JTable tabella;
	private JComboBox comboIva;
	private JComboBox comboFornitori;
	private JComboBox comboIncremento;
	private JPanel pannello;
	private Object[][] dbOggetti;
	private Color coloreBack = new Color(153, 153, 255);
	private Color coloreBackCombo = new Color(104, 178, 234);
	private boolean second = false;
	private JScrollPane scrollPane;
	private NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);

	public Materiale(){
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
				setVisible(false);
				//rimettere setvisible e togliere systemexit
				//System.exit(0);
                
            }
        });
		setTitle("Facom - Materiale");
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
		pannello = new JPanel();
		pannello.setLayout(null);
		pannello.setBackground(coloreBack);
		setContentPane(pannello);
	 	Font smallFancy = new Font("Serif", Font.BOLD, 16);
	 	Font smallSans = new Font("Sans Serif", Font.BOLD, 12);
		Font courier = new Font("Monospaced", Font.BOLD, 12);
		
		JLabel lblFor = new JLabel("Fornitore:");
		lblFor.setFont(smallFancy);
	 	lblFor.setBounds(10,10,100,30);
		pannello.add(lblFor);
		
	 	comboFornitori = new JComboBox(getFornitori());
	 	comboFornitori.setBounds(120, 13, 250, 20);
	 	comboFornitori.setEditable(false);
		comboFornitori.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				createTable();
		 	}	
		});		
	 	comboFornitori.setBackground(coloreBackCombo);
	 	pannello.add(comboFornitori);

		JLabel lbliva = new JLabel("I.V.A. %:");
		lbliva.setFont(smallFancy);
	 	lbliva.setBounds(10,40,100,30);
		pannello.add(lbliva);
		
	 	comboIva = new JComboBox(dbFatture.getIva());
	 	comboIva.setBounds(120, 43, 50, 20);
	 	comboIva.setEditable(true);
		comboIva.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				createTable();
		 	}	
		});		
	 	comboIva.setBackground(coloreBackCombo);
	 	pannello.add(comboIva);
		
		JLabel lblIncremento = new JLabel("Incremento %:");
		lblIncremento.setFont(smallFancy);
	 	lblIncremento.setBounds(10,70,120,30);
		pannello.add(lblIncremento);

	 	Object[] objIncr = {"120","100","80","50","30","10","0"};
	 	comboIncremento = new JComboBox(objIncr);
	 	comboIncremento.setBounds(120, 73, 50, 20);
	 	comboIncremento.setEditable(true);
		comboIncremento.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				createTable();			
		 	}	
		});		
	 	comboIncremento.setBackground(coloreBackCombo);
	 	pannello.add(comboIncremento);
	 	createTable();
      
	}
	private Vector getFornitori(){
		try{
			/*
			 * registrazione del driver jdbc (bridge jdbc-odbc)
			 */
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  //registra driver
			/*
			 * creazione della connessione al database "geco.mdb"
			 */
			Connection connessione = DriverManager.getConnection("jdbc:odbc:geco");
			/*
			 * creazione del comando (query) e definizione delle proprietà del
			 * risultante ResultSet
			 */
			Statement comando = connessione.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
															ResultSet.CONCUR_READ_ONLY);
			/*
			 * creazione del ResultSet contenente i risultati della query
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Nome FROM Fornitori;");
			Vector vettore = new Vector();
			while (risultatoQuery.next()){
				String s = risultatoQuery.getString(1);
				if (s == null){
					s = "";
				}	
				vettore.add(s);
					
			}
			return vettore;				

			/*
			 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
			 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			Vector vettore = new Vector();
			e.printStackTrace();
			return vettore;
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			Vector vettore = new Vector();
			e.printStackTrace();
			return vettore;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			Vector vettore = new Vector();
			e.printStackTrace();
			return vettore;
		}
	}
	private mater[] getMateriale(String forni){
		try{
			/*
			 * registrazione del driver jdbc (bridge jdbc-odbc)
			 */
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");  //registra driver
			/*
			 * creazione della connessione al database "geco.mdb"
			 */
			Connection connessione = DriverManager.getConnection("jdbc:odbc:geco");
			/*
			 * creazione del comando (query) e definizione delle proprietà del
			 * risultante ResultSet
			 */
			Statement comando = connessione.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
															ResultSet.CONCUR_READ_ONLY);
			/*
			 * creazione del ResultSet contenente i risultati della query
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Tipo, Prezzo, Anno FROM Materiale, Fornitori " +
				 "WHERE Materiale.Cod_Fornitore = Fornitori.Cod_Fornitore " +
				 "AND Fornitori.Nome = '" + forni +"' ORDER BY Tipo;");
			int nrig = 0;
			while (risultatoQuery.next()){
				nrig++;	
			}
			if (nrig == 0){
				mater[] obj = new mater[1];
				obj[0] = new mater("NOMATERIALE",0,0);
				return obj;
			}
			risultatoQuery.beforeFirst();
			mater[] materiale = new mater[nrig];
			int i = 0;
			while(risultatoQuery.next()){
				materiale[i] = new mater(	risultatoQuery.getString(1),
											risultatoQuery.getFloat(2),
											risultatoQuery.getInt(3));

				i++;
			}
			return materiale;	
				

			/*
			 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
			 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			mater[] obj = new mater[1];
			obj[0] = new mater("NOMATERIALE",0,0);
			return obj;
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			mater[] obj = new mater[1];
			obj[0] = new mater("NOMATERIALE",0,0);
			return obj;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			mater[] obj = new mater[1];
			obj[0] = new mater("NOMATERIALE",0,0);
			return obj;
		}
	}
	private void createTable(){
			if (second){
				pannello.remove(scrollPane);
			}	
			mater[] mat = getMateriale(comboFornitori.getSelectedItem().toString());
			int iva = Integer.parseInt(comboIva.getSelectedItem().toString());
			int incremento = Integer.parseInt(comboIncremento.getSelectedItem().toString());
			float incr;
			float incriva;
			float prez;
			
			Object[][] datiTabella = new Object[mat.length][5];
			for (int i = 0; i < mat.length; i++){
				datiTabella[i][0] = mat[i].getDescrizione();
				prez = mat[i].getPrezzo();
				incr = prez + prez*incremento/100;
				incriva = incr + incr*iva/100; 

				datiTabella[i][1] = nf.format(prez);
				datiTabella[i][2] = nf.format(incr);
				datiTabella[i][3] = nf.format(incriva);
				datiTabella[i][4] = new Integer(mat[i].getAnno());
			}	
			String[] descriCampi = {"Descrizione",
									"Prezzo €",
									"Incremento",
									"Incr. + iva",
									"Aggiornamento"};
									
			tabella = new JTable(datiTabella, descriCampi);
			tabella.setGridColor(Color.blue);
			TableColumn column = null;
		    column = tabella.getColumnModel().getColumn(0);
	        column.setPreferredWidth(400); //sport column is bigger
		
			scrollPane = new JScrollPane(tabella);
		 	scrollPane.setBounds(10,110,670,300);
			pannello.add(scrollPane);
			second = true;
									
			
	}		
		

	public static void main(String[] args){
			Materiale materiale = new Materiale();
			materiale.setSize(700,500);
			materiale.setLocation(200,160);
			materiale.setIconImage(new ImageIcon("images/facom.gif").getImage());
			materiale.setVisible(true);	
	}	
}		