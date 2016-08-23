import island.util.ita.*;
import java.sql.*;
import island.swing.*;

class Cliente{
	private String nomecliente;
	private String intestazione1;
	private String intestazione2;
	private String intestazione3;
	private String intestazione4;
	private String partitaiva;
	private String codicefiscale1;
	private String codicefiscale2;
	
	public Cliente(String NOME, String int1, String int2, String int3, String int4,
					String PartitaIva, String CodF1, String CodF2) 
					throws clienteException{
		nomecliente = NOME;
		intestazione1 = int1;
		intestazione2 = int2;
		intestazione3 = int3;
		intestazione4 = int4;
		partitaiva = PartitaIva;
		codicefiscale1 = CodF1;
		codicefiscale2 = CodF2;
		if (NOME.equalsIgnoreCase("")){
			throw new clienteException(
				"E' necessario inserire un nome per il cliente.");
		}
		if (NOME.length() > 50){
			throw new clienteException(
				"Il nome del cliente deve essere lungo al massimo 50 caratteri.");
		}	
		if (int1.length() > 35){
			throw new clienteException(
				"La prima riga dell'intestazione deve essere lunga al massimo 35 caratteri. \n" + 
					"Eliminare " + (int1.length() - 35) + " caratteri.");
		}	
		if (int2.length() > 35){
			throw new clienteException(
				"La seconda riga dell'intestazione deve essere lunga al massimo 35 caratteri. \n" + 
					"Eliminare " + (int2.length() - 35) + " caratteri.");
		}	
		if (int3.length() > 35){
			throw new clienteException(
				"La terza riga dell'intestazione deve essere lunga al massimo 35 caratteri. \n" + 
					"Eliminare " + (int3.length() - 35) + " caratteri.");
		}	
		if (int4.length() > 35){
			throw new clienteException(
				"La quarta riga dell'intestazione deve essere lunga al massimo 35 caratteri. \n" + 
					"Eliminare " + (int4.length() - 35) + " caratteri.");
		}	
		if (!PartitaIva.equalsIgnoreCase("") && !PI.isValid(PartitaIva)){
			throw new clienteException(
				"La partita IVA inserita non è valida.");
		}	
		if (!CodF1.equalsIgnoreCase("") && !CF.isValid(CodF1)){
			throw new clienteException(
				"Il primo codice fiscale inserito non è valido.");
		}	
		if (!CodF2.equalsIgnoreCase("") && !CF.isValid(CodF2)){
			throw new clienteException(
				"Il secondo codice fiscale inserito non è valido.");
		}	
	}
	public void isNew() throws clienteException{
		if (dbFatture.clienteEsistente(nomecliente)){
			throw new clienteException(
				"Cliente già esistente con lo stesso nome.");
		}
	}	
	public void insertIntoDB() throws clienteException{
		if (!dbFatture.creaCliente(nomecliente, intestazione1, intestazione2, 
								intestazione3, intestazione4, partitaiva,
									codicefiscale1, codicefiscale2)){
			throw new clienteException(
				"Impossibile aggiungere il cliente al database.");
		}		
	}
	public boolean update(){
		return dbFatture.aggiornaCliente(nomecliente,
								intestazione1,
								intestazione2,
								intestazione3,
								intestazione4,
								partitaiva,
								codicefiscale1,
								codicefiscale2);
								
								
	}							
	public Cliente(String NOME) throws clienteException{
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
				("SELECT * FROM Cliente WHERE Nome = '" + NOME + "';");

			if (!risultatoQuery.next()){
				throw new clienteException(
					"Il cliente non è presente nel database.");
			}
			nomecliente = risultatoQuery.getString(1);
			intestazione1 = risultatoQuery.getString(2);
			intestazione2 = risultatoQuery.getString(3);
			intestazione3 = risultatoQuery.getString(4);
			intestazione4 = risultatoQuery.getString(5);
			partitaiva = risultatoQuery.getString(6);
			codicefiscale1 = risultatoQuery.getString(7);
			codicefiscale2 = risultatoQuery.getString(8);
				
						
			
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
	public String getNome(){
		return nomecliente;
	}	
	public String getInt1(){
		return intestazione1;
	}	
	public String getInt2(){
		return intestazione2;
	}	
	public String getInt3(){
		return intestazione3;
	}	
	public String getInt4(){
		return intestazione4;
	}	
	public String getPI(){
		return partitaiva;
	}	
	public String getCF1(){
		return codicefiscale1;
	}	
	public String getCF2(){
		return codicefiscale2;
	}	
		

}
						