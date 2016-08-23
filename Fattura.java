import java.util.*;
import island.util.*;
import java.sql.*;
import java.io.*;
import java.text.*;
import island.swing.*;

class Fattura{
	private Vector vettoreRighe;
	private Cliente cliente;
	private String data;
	private int anno;
	private String pagamento;
	private int numero;
	private float imponibileVal;
	private float ivaPercVal;
	private float ivaVal;
	private float totaleVal;
	private String imponibile;
	private String ivaPerc;
	private String iva;
	private String totale;
	private boolean init = false;
	private NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
	private NumberFormat nfEuro = NumberFormat.getInstance(Locale.ITALIAN);
	private SimpleDateFormat dataFormatter = new SimpleDateFormat("dd/MM/yyyy");
	
	public Fattura(){
		vettoreRighe = new Vector();
		
	}
	public Fattura(int parAnno, int parNumero) throws FatturaException{
		
		try{
			vettoreRighe = new Vector();
			nfEuro.setMaximumFractionDigits(2);
			nfEuro.setMinimumFractionDigits(2);
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
				("SELECT * FROM Fattura WHERE Numero = " + parNumero + " AND Anno = " + parAnno + ";");
			if (!risultatoQuery.next()){
				throw new FatturaException("Fattura non presente nel database:");
			}

			risultatoQuery = comando.executeQuery
				("SELECT Um, Quantita, Descrizione, Prezzo, Importo, Riga FROM Righe WHERE Numero = " + parNumero + " AND Anno = " + parAnno + " ORDER BY Riga;");				
				
			while(risultatoQuery.next()){
				
				String un = risultatoQuery.getString("Um");
				if (un == null){
					un = "";
				}	
				float fqu = risultatoQuery.getFloat("Quantita");
				String qu;
				if (fqu == 0){
					qu = "";
				}else{
					qu = nf.format(fqu);
				}
				String de = risultatoQuery.getString("Descrizione");
				float fpr = risultatoQuery.getFloat("Prezzo");
				String pr;
				if (fpr == 0){
					pr = "";
				}else{
					pr = nf.format(fpr);
				}
				float fim = risultatoQuery.getFloat("Importo");
				String im;
				if (fim == 0){
					im = "";
				}else{
					im = nfEuro.format(fim);
				}
				
				
				addRiga(un, qu, de, pr, im);
			}			
			risultatoQuery = comando.executeQuery
				("SELECT Numero, Anno, Data, NomeCliente, IVA, Pagamento FROM Fattura WHERE Numero = " + parNumero + " AND Anno = " + parAnno + ";");
			risultatoQuery.next();
			String cl = risultatoQuery.getString("NomeCliente");
			String da = dataFormatter.format(risultatoQuery.getDate("Data"));;
			String nu = risultatoQuery.getString("Numero");
			String pa = risultatoQuery.getString("Pagamento");
			if (pa == null){
				pa = "";
			}	
			String iv = risultatoQuery.getString("Iva");
			float ivVal = risultatoQuery.getFloat("Iva");
			
			
			crea(new Cliente(cl),
				da,
				nu,
				pa,
				nfEuro.format(calcolaImponibile()),
				iv,
				nfEuro.format(calcolaImponibile()*(ivVal/100)),
				nfEuro.format(calcolaImponibile()*(1 + (ivVal/100))),
				false);
				
							
		
		}catch (SQLException e){

			throw new FatturaException("Connessione non riuscita " + e.getMessage());
		}catch (ClassNotFoundException e){
			throw new FatturaException("Caricamento driver fallito " + e.getMessage());
		}catch (Exception e){
			throw new FatturaException("Errore durante la selezione dal database:\n " + e.getMessage());
		}	
		
	}	
	
	public void addRiga(String unita, String quantita, String descrizione, 
						String prezzo, String importo)
			throws FatturaException{
		try{
			Riga r = new Riga(unita, quantita, descrizione, prezzo, importo);
			vettoreRighe.add(r);
		}catch (RigaException e){
			throw new FatturaException(e.getMessage());
		}
	}
	public void removeRiga(int indice){
		vettoreRighe.remove(indice);
	}
	public Riga getRiga(int indice){
		return (Riga) vettoreRighe.get(indice);
	}
	public Riga[] getRighe(){
		Enumeration e = vettoreRighe.elements();
		Riga[] r = new Riga[vettoreRighe.size()];
		for (int i = 0; i < r.length; i++){
			r[i] = (Riga) e.nextElement();
		}	
		return r;
	}	
	public String getUltimaRigaInserita(){
		Riga r = (Riga) vettoreRighe.lastElement();
		return r.toString();
		
	}
	public void replaceRiga(String unita, String quantita, String descrizione, 
							String prezzo, String importo, int indice)
			throws FatturaException{
		try{
			Riga r = new Riga(unita, quantita, descrizione, prezzo, importo);
			removeRiga(indice);
			vettoreRighe.add(indice, r);
		}catch (RigaException e){
			throw new FatturaException(e.getMessage());
		}
	}	
	public float calcolaImponibile(){
		float imponibile = 0;
		Enumeration e = vettoreRighe.elements();
		Riga r;
		while (e.hasMoreElements()){
			r = (Riga) e.nextElement();
			imponibile += r.getValoreImporto();
		}	
		return imponibile;
	}
	public void crea(Cliente clienteP, 
								String dataP,
								String numeroP,
								String pagamentoP,
								String imponibileP,
								String ivaPercP,
								String ivaP,
								String totaleP, boolean nuova) throws FatturaException{
		if (dataP.equalsIgnoreCase("")){
			throw new FatturaException("Inserire una data per la fattura");
		}	
		if (numeroP.equalsIgnoreCase("")){
			throw new FatturaException("Inserire un numero per la fattura");
		}
		if (!DateMng.isValid(dataP)){
			throw new FatturaException("Verificare che la data inserita sia corretta\ne nel formato richiesto");
		}	
		if (imponibileP.equalsIgnoreCase("")){
			throw new FatturaException("La fattura deve avere un imponibile.");
		}
		if (ivaP.equalsIgnoreCase("")){
			throw new FatturaException("La fattura deve avere un imposta I.V.A.");
		}
		if (ivaPercP.equalsIgnoreCase("")){
			throw new FatturaException("La fattura deve avere una percentuale I.V.A.");
		}
		if (totaleP.equalsIgnoreCase("")){
			throw new FatturaException("La fattura deve avere un totale.");
		}
		
		try{
			numero = Integer.parseInt(numeroP);
		}catch (Exception e){
			throw new FatturaException("Verificare il numero della fattura.");
		}
			
				

		try{
			cliente = clienteP;
			imponibile = imponibileP;
			ivaPerc = ivaPercP;
			iva = ivaP;
			totale = totaleP;
			data = dataP;
			pagamento = pagamentoP;
			GregorianCalendar cal = DateMng.getGregorianCalendar(dataP);
			anno = cal.get(Calendar.YEAR);
			imponibileVal = nf.parse(imponibileP).floatValue();
			ivaPercVal = nf.parse(ivaPercP).floatValue();
			ivaVal = nf.parse(ivaP).floatValue();
			totaleVal = nf.parse(totaleP).floatValue();
			 
		}catch (Exception e){
			throw new FatturaException("Errore: " + e.getMessage());
		}
		if (nuova && dbFatture.fatturaEsistente(numero, anno)){
			throw new FatturaException("Nel database è già presente una fattura con lo stesso\nnumero dello stesso anno");
		}	
		init = true;	
		
	}
	public void creaFile() throws FatturaException{
		if (init){
			try{
				String n;
				int numRiga = 0;
				int numeroPagine = getNumeroPagine();
				Riga[] righe = getRighe();
				if (numero < 10){
					n = "0" + numero;
				}else{
					n = "" + numero;	
				}	

				for (int pagina = 1; pagina < (numeroPagine + 1); pagina++){

					String OutputFileName = System.getProperty("user.dir") +
										File.separatorChar +
										"fatture" +
										File.separatorChar +
										"fatt" + anno + "n" + n + "pag" + pagina +".html";
					FileOutputStream OutFile = new FileOutputStream(OutputFileName);
					DataOutputStream OutData = new DataOutputStream(OutFile);

					
					OutData.writeBytes("<html>\n");
					OutData.writeBytes("<head>\n");
					OutData.writeBytes("<title>Fattura n° " + numero + " del " + data + "</title>\n");
					OutData.writeBytes("<meta name=\"description\" content=\"Fattura Commerciale - created by Facom 1.0.8 - Gianluca Tavella 2002\">\n");
					OutData.writeBytes("</head>\n");
					OutData.writeBytes("<body bgcolor=\"#FFFFFF\" text=\"#000000\">\n");
					OutData.writeBytes("<table width=\"736\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" align=\"left\" height=\"1001\">\n");
					OutData.writeBytes("  <tr> \n");
					OutData.writeBytes("    <td valign=\"top\" height=\"973\"> \n");
					OutData.writeBytes("      <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"732\" height=\"873\">\n");
					OutData.writeBytes("        <tr> \n");
					OutData.writeBytes("          <td height=\"355\" valign=\"top\"> \n");
					OutData.writeBytes("            <table width=\"730\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
					OutData.writeBytes("              <tr> \n");
					OutData.writeBytes("                <td width=\"300\" valign=\"top\" align=\"center\" height=\"349\"> \n");
					OutData.writeBytes("                  <p><img src=\"images/dittatavella.gif\" width=\"300\" height=\"180\"></p>\n");
					OutData.writeBytes("                  <table width=\"309\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"117\">\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td width=\"139\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\">Partita \n");
					OutData.writeBytes("                          I.V.A. Cliente:</font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td width=\"10\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td width=\"161\"> \n");
					OutData.writeBytes("                        <div align=\"left\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\"><b>" + cliente.getPI() + "</b></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td width=\"139\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td width=\"10\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td width=\"161\"> \n");
					OutData.writeBytes("                        <div align=\"left\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\"><font face=\"Arial, Helvetica, sans-serif\"></font></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td align=\"right\" width=\"139\" height=\"23\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\">Codice \n");
					OutData.writeBytes("                          Fiscale Cliente:</font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"10\" height=\"23\"> \n");
					OutData.writeBytes("                        <div align=\"right\"><font face=\"Arial, Helvetica, sans-serif\"></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"161\" height=\"23\"> \n");
					OutData.writeBytes("                        <p align=\"left\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\"><b>\n");
					OutData.writeBytes("                          " + cliente.getCF1() + "</b></font></p>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td align=\"right\" width=\"139\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"10\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td align=\"left\" width=\"161\" valign=\"top\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\"><b>\n");
					OutData.writeBytes("                        " + cliente.getCF2() + "</b></font></td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td align=\"right\" width=\"139\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"10\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"161\" valign=\"top\">&nbsp;</td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td align=\"right\" width=\"139\" valign=\"top\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\">Pagamento:</font></td>\n");
					OutData.writeBytes("                      <td align=\"right\" width=\"10\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td align=\"left\" width=\"161\" valign=\"top\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\"><b>\n");
					OutData.writeBytes("                        " + pagamento + "</b></font></td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                  </table>\n");
					OutData.writeBytes("                  <p>&nbsp;</p>\n");
					OutData.writeBytes("                </td>\n");
					OutData.writeBytes("                <td width=\"425\" valign=\"top\" align=\"right\" height=\"349\"> \n");
					OutData.writeBytes("                  <table width=\"250\" border=\"1\" cellspacing=\"0\" cellpadding=\"0\" bordercolor=\"#000000\">\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td height=\"28\" bgcolor=\"#FFFFCC\"> \n");
					OutData.writeBytes("                        <div align=\"center\"><font face=\"Arial, Helvetica, sans-serif\">FATTURA \n");
					OutData.writeBytes("                          n&deg; <b>" + numero + "</b> del </font><font face=\"Arial, Helvetica, sans-serif\"><b>" + data + "</b></font></div>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                  </table>\n");
					OutData.writeBytes("                  <table width=\"130\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" height=\"38\">\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td align=\"right\"><font face=\"Arial, Helvetica, sans-serif\" size=\"2\">Pag. \n");
					OutData.writeBytes("                        <b>" + pagina +"</b> di <b>" + numeroPagine + "</b></font></td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                  </table>\n");
					OutData.writeBytes("                  <p align=\"center\"><br>\n");
					OutData.writeBytes("                  </p>\n");
					OutData.writeBytes("                  <p align=\"center\">&nbsp;</p>\n");
					OutData.writeBytes("                  <p align=\"center\">&nbsp; </p>\n");
					OutData.writeBytes("                  <table width=\"376\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\">\n");
					OutData.writeBytes("                    <tr> \n");
					OutData.writeBytes("                      <td valign=\"top\" align=\"left\" width=\"43\">&nbsp;</td>\n");
					OutData.writeBytes("                      <td valign=\"top\" align=\"left\" width=\"333\"> \n");
					OutData.writeBytes("                        <p><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b></b></font></p>\n");
					OutData.writeBytes("                        <p><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"><b> \n");
					OutData.writeBytes("                          " + cliente.getInt1().replaceAll(" ","&nbsp;") + "<br>\n");
					OutData.writeBytes("                          " + cliente.getInt2().replaceAll(" ","&nbsp;")+ "<br>\n");
					OutData.writeBytes("                          " + cliente.getInt3().replaceAll(" ","&nbsp;") + "<br>\n");
					OutData.writeBytes("                          " + cliente.getInt4().replaceAll(" ","&nbsp;")+ "</b></font></p>\n");
					OutData.writeBytes("                      </td>\n");
					OutData.writeBytes("                    </tr>\n");
					OutData.writeBytes("                  </table>\n");
					OutData.writeBytes("                  <p align=\"center\">&nbsp;</p>\n");
					OutData.writeBytes("                  </td>\n");
					OutData.writeBytes("              </tr>\n");
					OutData.writeBytes("            </table>\n");
					OutData.writeBytes("          </td>\n");
					OutData.writeBytes("        </tr>\n");
					String[][] matrixRow = new String[30][5];
					for (int j = 0; j < 30; j++){
						for (int k = 0; k < 5; k++){
							matrixRow[j][k] = "<br>\n";
						}	
					}
					boolean esci = false;
					int jM = 0;
					for (int j = numRiga; j < righe.length;){
						

						String[] desc = righe[j].getDescrizioneDivisa();
						if (jM+desc.length > 30){
							esci = true;
							numRiga = j;
							break;
						}	

						if (!righe[j].getUnita().equalsIgnoreCase("") && !righe[j].getQuantita().equalsIgnoreCase("")){
							matrixRow[jM][0] = righe[j].getUnita() + "<br>\n";
						}
						if (!righe[j].getQuantita().equalsIgnoreCase("")){
							matrixRow[jM][1] = righe[j].getQuantita() + "<br>\n";
						}
						if (!righe[j].getPrezzo().equalsIgnoreCase("")){
							matrixRow[jM][3] = righe[j].getPrezzo() + "<br>\n";
						}
						if (!righe[j].getImporto().equalsIgnoreCase("")){
							matrixRow[jM][4] = righe[j].getImporto() + "<br>\n";
						}

						for (int k = 0; k < desc.length; k++){
							matrixRow[jM][2] = desc[k] + "<br>\n";
							jM++;
						}
						jM++;
						j++;
					}
													
						
					
					OutData.writeBytes("        <tr> \n");
					OutData.writeBytes("          <td height=\"673\" align=\"left\" valign=\"top\"> \n");
					OutData.writeBytes("            <table width=\"730\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\" height=\"538\" bordercolor=\"#999999\">\n");
					OutData.writeBytes("              <tr align=\"center\" valign=\"middle\"> \n");
					OutData.writeBytes("                <td width=\"41\" height=\"36\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">UM</font></b></td>\n");
					OutData.writeBytes("                <td width=\"67\" height=\"36\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">Q.TA'</font></b></td>\n");
					OutData.writeBytes("                <td width=\"409\" height=\"36\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">DESCRIZIONE</font></b></td>\n");
					OutData.writeBytes("                <td width=\"89\" height=\"36\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">PREZZO</font></b></td>\n");
					OutData.writeBytes("                <td width=\"92\" height=\"36\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">IMPORTO</font></b></td>\n");
					OutData.writeBytes("              </tr>\n");
					OutData.writeBytes("              <tr align=\"right\" valign=\"top\"> \n");
					OutData.writeBytes("                <td width=\"41\" height=\"503\"><font face=\"Courier New, Courier, mono\" size=\"2\">\n");
					for (int x = 0; x < 30; x++){
						OutData.writeBytes(matrixRow[x][0]);
					}	
					OutData.writeBytes("                  </font></td>\n");
					OutData.writeBytes("                <td width=\"67\" height=\"503\"> \n");
					OutData.writeBytes("                  <font face=\"Courier New, Courier, mono\" size=\"2\">\n");
					for (int x = 0; x < 30; x++){
						OutData.writeBytes(matrixRow[x][1]);
					}	

					OutData.writeBytes("                    </font>\n");
					OutData.writeBytes("                  </td>\n");
					OutData.writeBytes("                <td width=\"409\" height=\"503\" align=\"left\"><font face=\"Courier New, Courier, mono\" size=\"2\">\n");
					for (int x = 0; x < 30; x++){
						OutData.writeBytes(matrixRow[x][2]);
					}	
					OutData.writeBytes("                  </font></td>\n");
					OutData.writeBytes("                <td width=\"89\" height=\"503\"><font face=\"Courier New, Courier, mono\" size=\"2\">\n");
					for (int x = 0; x < 30; x++){
						OutData.writeBytes(matrixRow[x][3]);
					}	
					OutData.writeBytes("                  </font></td>\n");
					OutData.writeBytes("                <td width=\"92\" height=\"503\"> \n");
					OutData.writeBytes("                  <font face=\"Courier New, Courier, mono\" size=\"2\">\n");
					for (int x = 0; x < 30; x++){
						OutData.writeBytes(matrixRow[x][4]);
					}	
					OutData.writeBytes("                    </font>\n");
					OutData.writeBytes("                </td>\n");
					OutData.writeBytes("              </tr>\n");
					OutData.writeBytes("            </table>\n");
					
					if (!esci){
						
						OutData.writeBytes("            <table width=\"733\" border=\"0\" cellspacing=\"0\" cellpadding=\"2\" height=\"123\">\n");
						OutData.writeBytes("              <tr> \n");
						OutData.writeBytes("                <td width=\"482\" height=\"120\" valign=\"bottom\" align=\"right\"> \n");
						OutData.writeBytes("                  <p><b><font color=\"#000000\" size=\"2\" face=\"Times New Roman, Times, serif\">S. \n");
						OutData.writeBytes("                    E. &amp; O.&nbsp;&nbsp; </font></b></p>\n");
						OutData.writeBytes("                  </td>\n");
						OutData.writeBytes("                <td width=\"243\" height=\"120\" valign=\"top\" align=\"right\"> \n");
						OutData.writeBytes("                  <p>&nbsp;</p>\n");
						OutData.writeBytes("                  <table width=\"236\" border=\"1\" cellspacing=\"0\" cellpadding=\"2\" height=\"128\" bordercolor=\"#999999\">\n");
						OutData.writeBytes("                    <tr> \n");
						OutData.writeBytes("                      <td height=\"41\" width=\"130\" align=\"center\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">Imponibile</font></b></td>\n");
						OutData.writeBytes("                      <td height=\"41\" width=\"92\" align=\"right\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + imponibile + "</font></b></td>\n");
						OutData.writeBytes("                    </tr>\n");
						OutData.writeBytes("                    <tr> \n");
						OutData.writeBytes("                      <td height=\"41\" width=\"130\" align=\"center\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">I.V.A. \n");
						OutData.writeBytes("                        <font size=\"4\">" + ivaPerc + "</font>%</font></b></td>\n");
						OutData.writeBytes("                      <td height=\"41\" width=\"92\" align=\"right\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">" + iva + "</font></b></td>\n");
						OutData.writeBytes("                    </tr>\n");
						OutData.writeBytes("                    <tr bgcolor=\"#CCCCCC\"> \n");
						OutData.writeBytes("                      <td height=\"41\" width=\"130\" align=\"center\" bgcolor=\"#E5E5E5\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\">Totale \n");
						OutData.writeBytes("                        &euro;</font></b></td>\n");
						OutData.writeBytes("                      <td height=\"41\" width=\"92\" align=\"right\"><b><font face=\"Arial, Helvetica, sans-serif\" size=\"3\"> \n");
						OutData.writeBytes("                        " + totale + "</font></b></td>\n");
						OutData.writeBytes("                    </tr>\n");
						OutData.writeBytes("                  </table>\n");
						OutData.writeBytes("                </td>\n");
						OutData.writeBytes("              </tr>\n");
						OutData.writeBytes("            </table>\n");
						OutData.writeBytes("          </td>\n");
						OutData.writeBytes("        </tr>\n");
						OutData.writeBytes("      </table>\n");
						OutData.writeBytes("    </td>\n");
						OutData.writeBytes("  </tr>\n");
						OutData.writeBytes("</table>\n");
					}	
					
					OutData.writeBytes("</body>\n");
					OutData.writeBytes("</html>\n");

					OutData.close();
					OutFile.close();
					Runtime.getRuntime().exec("C:\\Programmi\\Internet Explorer\\IEXPLORE.EXE " + OutputFileName);
				
				}
										
				
			}catch (Exception e){
				
				throw new FatturaException("Errore: " + e.getMessage());
			}															
		}		
	}
	public void insertIntoDB(boolean nuova) throws FatturaException{
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
			if (!nuova){
				comando.executeUpdate("DELETE FROM Fattura WHERE Numero = " +
										numero + " AND Anno = " + anno + ";");
			}							

			comando.executeUpdate("INSERT INTO Fattura VALUES(" + numero + ", " +
									anno + ", '" + data + "', '" + cliente.getNome() +
									"', " + ivaPerc + ", " + getNumeroPagine() + 
									", '" + pagamento + "');");
			Riga[] righe = getRighe();
			for (int i = 0; i < righe.length; i++){
				comando.executeUpdate("INSERT INTO Righe VALUES(" + numero + ", " +
										anno + ", " + (i+1) + ", '" + 
										righe[i].getUnita() + "', " +
										righe[i].getValoreQuantita() + ", '" +
										righe[i].getDescrizione() + "', " +
										righe[i].getValorePrezzo() + ", " +
										righe[i].getValoreImporto() + ");");
			}
				
			 
		}catch (Exception e){
			throw new FatturaException("Errore durante l'inserimento nel database:\n " + e.getMessage());
		}	
		
	
	
	}
	public void delete() throws FatturaException{
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
			if (comando.executeUpdate("DELETE FROM Fattura WHERE Numero = " +
									numero + " AND Anno = " + anno + ";") == 1){

				int numeroPagine = getNumeroPagine();
				String n;
				if (numero < 10){
					n = "0" + numero;
				}else{
					n = "" + numero;	
				}	
	
				for (int pagina = 1; pagina < (numeroPagine + 1); pagina++){
										
					String OutputFileName = System.getProperty("user.dir") +
										File.separatorChar +
										"fatture" +
										File.separatorChar +
										"fatt" + anno + "n" + n + "pag" + pagina +".html";
					File file = new File(OutputFileName);
					file.delete();
					
				}							
			}else{
				throw new FatturaException("Impossibile eliminare la fattura.");
			}	
				
			 
		}catch (Exception e){
			throw new FatturaException("Errore durante l'eliminazione:\n " + e.getMessage());
		}	
	}			
		
	public int getNumeroPagine(){
		Riga[] r = getRighe();
		int n = 0;
		int pag = 1;
		for (int i = 0; i < r.length; i++){
			n += r[i].getNumeroRigheDescrizione() + 1;
			if (n > 30*pag){
				pag++;
			}	
		}
		return pag;
	}
	public String getData(){
		return data;
	}
	public int getAnno(){
		return anno;
	}
	public String getNumero(){
		return String.valueOf(numero);
	}
	public String getNomeCliente(){
		return cliente.getNome();
	}
	public Object getNomeClienteObject(){
		return (Object) cliente.getNome();
	}
	public String getPagamento(){
		return pagamento;
	}
	public Object getPagamentoObject(){
		return (Object) pagamento;
	}
	public String getImponibile(){
		return imponibile;
	}
	public String getIva(){
		return iva;
	}
	public String getTotale(){
		return totale;
	}
	public String getIvaPerc(){
		return ivaPerc;
	}
	public Object getIvaPercObject(){
		return (Object) ivaPerc;
	}
	public boolean isCreate(){
		return init;
	}	
	
								
}						
			
