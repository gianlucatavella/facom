
/* ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
   ø  by Gianluca Tavella - gianluca.tavella@libero.it                       ø
   ø  Feb 2002                                                               ø
   ø                                                                         ø
   ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Classe Apri: finestra con una lista da cui selezionare la fattura da aprire
 *
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */ 
/*
 * import dei package necessari
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;
import island.swing.*;

class Apri extends JFrame{
	//////////////////////////////////////
	//// Variabili e oggetti di classe ///
	//////////////////////////////////////
	private JList lista;
	private Facom window;
	private DefaultListModel modellolista;
	private int[][] anniNumeri;

	public Apri(Facom win){
		window = win;
		/*
		 * gestione dell'evento chiusura finestra
		 */
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	window.setEnabled(true);
				window.setVisible(true);
				setVisible(false);
                
            }
        });
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
				("SELECT Anno, Numero, NomeCliente FROM Fattura ORDER BY Anno DESC, Numero DESC;");
			int n = 0;
			while (risultatoQuery.next()){
				n++;	
			}
			String[] fatture = new String[n];
			anniNumeri = new int[n][2];
			risultatoQuery.beforeFirst();
			String numzero;
			int numb;
			lista = new JList();
			modellolista = new DefaultListModel();

			for (int i = 0; i < fatture.length; i++){
				risultatoQuery.next();
				numb = risultatoQuery.getInt(2);
				if (numb < 10){
					numzero = "0" + numb;
				}else{
					numzero = "" + numb;
				}						
				fatture[i] = risultatoQuery.getString(1) +
							" - n° " + 
							numzero +
							" - " +
							risultatoQuery.getString(3);


				modellolista.addElement(fatture[i]);			
			}			
			risultatoQuery.beforeFirst();
			for (int i = 0; i < fatture.length; i++){
				risultatoQuery.next();			

				anniNumeri[i][0] = risultatoQuery.getInt(1);
				anniNumeri[i][1] = risultatoQuery.getInt(2);
			}
	        /*
	         * creazione del contenitore dei componenti
	         */
		 	Container contentPane = getContentPane();
		 	contentPane.setLayout(new BorderLayout());
			/*
			 * creazione della lista, del pannello per lo scrolling e dei pulsanti
			 * aggiunta degli elementi creati al contenitore
			 */
			lista.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			lista.setModel(modellolista);
			JScrollPane panel = new JScrollPane(lista);
			contentPane.add(panel, BorderLayout.CENTER);
			JButton pulsante = new JButton("Apri", new ImageIcon("images/apri.gif"));
			contentPane.add(pulsante, BorderLayout.EAST);
			JButton chiudi = new JButton("Chiudi");
			contentPane.add(chiudi, BorderLayout.SOUTH);
			JLabel lblDesc = new JLabel("Selezionare dall'elenco la fattura da aprire");
			contentPane.add(lblDesc, BorderLayout.NORTH);
			/*
			 * gestione degli eventi da associare ai pulsanti
			 */	
			pulsante.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					/*
					 * verifica è stato selezionato un elemento dalla lista
					 */
					if (lista.getSelectedIndex() != -1){
						window.ApriFattura(anniNumeri[lista.getSelectedIndex()][0],
										   anniNumeri[lista.getSelectedIndex()][1]);
						setVisible(false);
		            	window.setEnabled(true);
						window.setVisible(true);
		            	
				   
					}	
			 	}	
	 		});		
			chiudi.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					/*
					 * chiusura della finestra
					 */
					setVisible(false);	
	            	window.setEnabled(true);
					window.setVisible(true);
			 	}	
	 		});		
			
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
		}        

	}

}		 	