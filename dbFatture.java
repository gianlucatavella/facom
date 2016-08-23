import java.sql.*;
import java.text.*;
import island.swing.*;

class dbFatture{
	public static boolean clienteEsistente(String nomec){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Nome FROM Cliente;");

			while (risultatoQuery.next()){
				if (risultatoQuery.getString(1).equalsIgnoreCase(nomec)){
					return true;
				}
			}
			return false;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			return false;
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			return false;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}
	}
	public static boolean creaCliente(String NOME, String int1, String int2, String int3,
			String int4, String PartitaIva, String CodF1, String CodF2){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			comando.executeUpdate
				("INSERT INTO Cliente VALUES ('" + NOME + "', '" + int1 + "', '" +
					int2 + "', '" + int3 + "', '" + int4 + "', '" + PartitaIva +
					"', '" + CodF1 + "', '" + CodF2 + "');");
			return true;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}	
	}
	public static boolean aggiornaCliente(String NOME, String int1, String int2, String int3,
			String int4, String PartitaIva, String CodF1, String CodF2){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			comando.executeUpdate("DELETE * FROM Cliente WHERE Nome = '" + NOME + "';");
			 
			comando.executeUpdate
				("INSERT INTO Cliente VALUES ('" + NOME + "', '" + int1 + "', '" +
					int2 + "', '" + int3 + "', '" + int4 + "', '" + PartitaIva +
					"', '" + CodF1 + "', '" + CodF2 + "');");
			return true;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}	
	}
	public static boolean eliminaCliente(String NOME){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			comando.executeUpdate("DELETE * FROM Cliente WHERE Nome = '" + NOME + "';");
			 
			return true;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}	
	}
	public static String[] getListaClienti(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Nome FROM Cliente ORDER BY Nome;");

			
			int i = 0;
			while (risultatoQuery.next()) {
				i++;
			}
			/*
			 * se il ResultSet è vuoto viene fatta ritornare una matrice con una sola
			 * stringa vuota e si interrompe l'esecuzione del metodo, altrimenti 
			 * l'esecuzione del metodo prosegue
			 */
			if (i == 0){
				String[] listaclienti = new String[1];
				listaclienti[0] = "";
				return listaclienti;
			}
			/*
			 * posizionamento alla prima riga del ResultSet
			 */	
			risultatoQuery.first();
			/*
			 * creazione dell'array di stringhe in base alla dimensione del ResultSet
			 */
			String[] listaC = new String[i];
			/*
			 * popolamento dell'array
			 */
			for (int j = 0; j < listaC.length; j++){
				listaC[j] = risultatoQuery.getString(1);
				risultatoQuery.next();
			}				
			return listaC;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			String[] listaclienti = new String[1];
			listaclienti[0] = "";
			return listaclienti;		
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			String[] listaclienti = new String[1];
			listaclienti[0] = "";
			return listaclienti;		
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			String[] listaclienti = new String[1];
			listaclienti[0] = "";
			return listaclienti;		
		}
	}
	public static String[] getDataNumeroMassimo(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Data, Numero FROM Fattura WHERE Data >= (SELECT Max(Data) FROM Fattura);");
		 	risultatoQuery.next();
		 	SimpleDateFormat formatter;
		 	formatter = new SimpleDateFormat("dd/MM/yyyy");
	 		String[] s = {formatter.format(risultatoQuery.getDate(1)),
	 					 risultatoQuery.getString(2)};
	 		return s;			 
		}catch (Exception e){
			String[] s = {"",""};
			return s;
		}
	}	 		
	public static String[] getDescrizioni(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Descrizione FROM Impostazioni WHERE Descrizione IS NOT NULL ORDER BY Descrizione;");

			
			int i = 0;
			while (risultatoQuery.next()) {
				i++;
			}
			/*
			 * se il ResultSet è vuoto viene fatta ritornare una matrice con una sola
			 * stringa vuota e si interrompe l'esecuzione del metodo, altrimenti 
			 * l'esecuzione del metodo prosegue
			 */
			if (i == 0){
				String[] listadescrizioni = new String[1];
				listadescrizioni[0] = "";
				return listadescrizioni;
			}
			/*
			 * posizionamento alla prima riga del ResultSet
			 */	
			risultatoQuery.first();
			/*
			 * creazione dell'array di stringhe in base alla dimensione del ResultSet
			 */
			String[] listaC = new String[i];
			/*
			 * popolamento dell'array
			 */
			for (int j = 0; j < listaC.length; j++){
				listaC[j] = risultatoQuery.getString(1);
				risultatoQuery.next();
			}				
			return listaC;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			String[] listadescrizioni = new String[1];
			listadescrizioni[0] = "";
			return listadescrizioni;		
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			String[] listadescrizioni = new String[1];
			listadescrizioni[0] = "";
			return listadescrizioni;		
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			String[] listadescrizioni = new String[1];
			listadescrizioni[0] = "";
			return listadescrizioni;		
		}
	}
	public static String[] getMisure(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Misura FROM Impostazioni WHERE Misura IS NOT NULL;");

			
			int i = 0;
			while (risultatoQuery.next()) {
				i++;
			}
			/*
			 * se il ResultSet è vuoto viene fatta ritornare una matrice con una sola
			 * stringa vuota e si interrompe l'esecuzione del metodo, altrimenti 
			 * l'esecuzione del metodo prosegue
			 */
			if (i == 0){
				String[] listamisure = new String[1];
				listamisure[0] = "";
				return listamisure;
			}
			/*
			 * posizionamento alla prima riga del ResultSet
			 */	
			risultatoQuery.first();
			/*
			 * creazione dell'array di stringhe in base alla dimensione del ResultSet
			 */
			String[] listaC = new String[i];
			/*
			 * popolamento dell'array
			 */
			for (int j = 0; j < listaC.length; j++){
				listaC[j] = risultatoQuery.getString(1);
				risultatoQuery.next();
			}				
			return listaC;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			String[] listamisure = new String[1];
			listamisure[0] = "";
			return listamisure;		
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			String[] listamisure = new String[1];
			listamisure[0] = "";
			return listamisure;		
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			String[] listamisure = new String[1];
			listamisure[0] = "";
			return listamisure;		
		}
	}
	public static String[] getPagamenti(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Pagamento FROM Impostazioni WHERE Pagamento IS NOT NULL;");

			
			int i = 0;
			while (risultatoQuery.next()) {
				i++;
			}
			/*
			 * se il ResultSet è vuoto viene fatta ritornare una matrice con una sola
			 * stringa vuota e si interrompe l'esecuzione del metodo, altrimenti 
			 * l'esecuzione del metodo prosegue
			 */
			if (i == 0){
				String[] listapagamenti = new String[1];
				listapagamenti[0] = "";
				return listapagamenti;
			}
			/*
			 * posizionamento alla prima riga del ResultSet
			 */	
			risultatoQuery.first();
			/*
			 * creazione dell'array di stringhe in base alla dimensione del ResultSet
			 */
			String[] listaC = new String[i];
			/*
			 * popolamento dell'array
			 */
			for (int j = 0; j < listaC.length; j++){
				listaC[j] = risultatoQuery.getString(1);
				risultatoQuery.next();
			}				
			return listaC;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			String[] listapagamenti = new String[1];
			listapagamenti[0] = "";
			return listapagamenti;		
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			String[] listapagamenti = new String[1];
			listapagamenti[0] = "";
			return listapagamenti;		
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			String[] listapagamenti = new String[1];
			listapagamenti[0] = "";
			return listapagamenti;		
		}
	}
	public static String[] getIva(){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT Iva FROM Impostazioni WHERE Iva IS NOT NULL ORDER BY Iva DESC;");

			
			int i = 0;
			while (risultatoQuery.next()) {
				i++;
			}
			/*
			 * se il ResultSet è vuoto viene fatta ritornare una matrice con una sola
			 * stringa vuota e si interrompe l'esecuzione del metodo, altrimenti 
			 * l'esecuzione del metodo prosegue
			 */
			if (i == 0){
				String[] listaIVA = new String[1];
				listaIVA[0] = "";
				return listaIVA;
			}
			/*
			 * posizionamento alla prima riga del ResultSet
			 */	
			risultatoQuery.first();
			/*
			 * creazione dell'array di stringhe in base alla dimensione del ResultSet
			 */
			String[] listaC = new String[i];
			/*
			 * popolamento dell'array
			 */
			for (int j = 0; j < listaC.length; j++){
				listaC[j] = risultatoQuery.getString(1);
				risultatoQuery.next();
			}				
			return listaC;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			String[] listaIVA = new String[1];
			listaIVA[0] = "";
			return listaIVA;		
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			String[] listaIVA = new String[1];
			listaIVA[0] = "";
			return listaIVA;		
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			String[] listaIVA = new String[1];
			listaIVA[0] = "";
			return listaIVA;		
		}
	}
	public static boolean fatturaEsistente(int numero, int anno){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT * FROM Fattura WHERE Numero = " + numero + " AND Anno = " + anno + ";");

			if (risultatoQuery.next()){
				return true;
			}
			return false;		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			return false;
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			return false;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}
	}
	public static String getFatturato(String anno){
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			ResultSet risultatoQuery = comando.executeQuery
				("SELECT sum(importo) AS fatturato FROM righe, fattura WHERE fattura.numero=righe.numero AND fattura.anno=righe.anno AND fattura.anno=" + anno + ";");

			if (risultatoQuery.next()){
				return risultatoQuery.getString(1);
			}
			return "";		
		/*
		 * gestione esplicita delle eccezioni, visualizzazione di un messaggio
		 * e conseguente ritorno, di fronte ad un'eventuale eccezione, del
		 * valore true
		 */	
		}catch (SQLException e){
			new MessageWarning("Connessione non riuscita " + e.getMessage(), "Facom");
			return "";
		}catch (ClassNotFoundException e){
			new MessageWarning("Caricamento driver fallito " + e.getMessage(), "Facom");
			return "";
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return "";
		}
	}
	public static boolean putVoce(String voce){
		
		try{
			String[] descEsi = new String[getDescrizioni().length];
			descEsi = getDescrizioni();
			for (int i = 0; i < descEsi.length; i++){
				if	(descEsi[i].equalsIgnoreCase(voce)){
					return false;
				}	
			}	
			
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
			 * creazione del ResultSet contenente i risultati della query che seleziona
			 * le stazioni presenti nel database ordinate per nome
			 */
			comando.executeUpdate
				("INSERT INTO Impostazioni VALUES ('" + voce + "',null,null,null);");
			return true;
		}catch (Exception e){
			new MessageWarning("Errore: " + e.getMessage(), "Facom");
			return false;
		}	
	}


}			

	