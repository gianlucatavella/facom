/* ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
   ø  by Gianluca Tavella - gianluca.tavella@libero.it                       ø
   ø  Nov 2001                                                               ø
   ø  Facom - Fatture Commerciali                                            ø
   ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Classe Facom: applicazione con interfaccia grafica per la gestione delle
 *				fatture
 *
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
/*
 * import dei package necessari
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.text.*;
import island.util.*;
import island.swing.*;

class Facom extends JFrame implements ActionListener{
	private JMenuItem menuChiudi;
	private JMenuItem menuSalva;
	private JMenuItem menuStampa;
	private JMenuItem menuElimina;
	private JMenu jmenuInserisci;
	private JButton buttonElimina;
	private JButton buttonSalva;
	private JPanel contenitore;
	private String titolo = new String("Facom - Fatture Commerciali");
	private JTextField NomeCliente;
	private JTextField int1;
	private JTextField int2;
	private JTextField int3;
	private JTextField int4;
	private JTextField partIva;
	private JTextField codF1;
	private JTextField codF2;
	private JComboBox comboClienti;
	private JTextField dataFattura;
	private JTextField numeroFattura;
	private Color coloreBackCliente = new Color(204,236,255);
	private Color coloreBackComboCliente = new Color(102,204,255);
	private Color coloreBack = new Color(255,204,204);
	private Color coloreBackCombo = new Color(255,153,153);
	private JComboBox comboDescrizioni;
	private JComboBox comboPagamenti;
	private JTextArea textArea;
	private JComboBox comboMisure;
	private JTextField quantita;
	private JTextField prezzo;
	private JTextField importo;
	private NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
	private DefaultListModel modelloListaR;
	private JList listaR;
	private	Fattura nuovaFattura;
	private boolean modificaRiga = false;
	private int indiceModificaRiga = -1;
	private JButton inserisci;
	private JButton modifica;
	private JTextField imponibile;
	private JComboBox comboIva;
	private JTextField iva;
	private JTextField totale;
	private boolean modi = false;
	private boolean isNewFattura = true;

	
	
	
	private Facom(){
		setTitle(titolo);
		addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
	        	if (modi == true){
	        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
			        	System.exit(0);
	        		}	
				}else{
		        	System.exit(0);
	    		}	
            }
        });
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

		/* 
		 * dichiarazione oggetti di metodo 
		 */
		JMenuBar menubar;
		JMenu menu, submenu;
		JMenuItem menuitem;
		JRadioButtonMenuItem rbMenuitem;
 		/*
 		 * creazione della barra dei menu
 		 */
 		menubar = new JMenuBar();
	 	setJMenuBar(menubar);
	 	/*
	 	 * creazione del primo menu (Stazione) e aggiunta alla barra
	 	 */
	 	menu = new JMenu("Fattura");
	 	menu.setMnemonic(KeyEvent.VK_F);
	 	menubar.add(menu);
		/*
		 * creazione della prima voce (Nuova...) del primo menu (Stazione)
		 */
		menuitem = new JMenuItem("Nuova...", new ImageIcon("images/nuova.gif"));
		menuitem.setMnemonic(KeyEvent.VK_N);
		/*
		 * aggiunta di un tasto di scelta rapida (CTRL-N) alla voce di menu
		 */
	    menuitem.setAccelerator(KeyStroke.getKeyStroke(
 		    KeyEvent.VK_N, ActionEvent.CTRL_MASK));
 		menuitem.addActionListener(this);
 		/*
 		 * aggiunta della voce al menu
 		 */
 		menu.add(menuitem);

 		menuitem = new JMenuItem("Apri...", new ImageIcon("images/apri.gif"));
 		menuitem.setMnemonic(KeyEvent.VK_R);
 		menuitem.addActionListener(this);
		menu.add(menuitem);

 		menuChiudi = new JMenuItem("Chiudi");
 		menuChiudi.setMnemonic(KeyEvent.VK_H);
 		menuChiudi.addActionListener(this);
		menu.add(menuChiudi);

		menu.addSeparator();
 		menuSalva = new JMenuItem("Salva", new ImageIcon("images/salva.gif"));
 		menuSalva.setMnemonic(KeyEvent.VK_S);

	    menuSalva.setAccelerator(KeyStroke.getKeyStroke(
 		    KeyEvent.VK_S, ActionEvent.CTRL_MASK));
 		menuSalva.addActionListener(this);
	
		menu.add(menuSalva);
		
		
		menu.addSeparator();
 		menuElimina = new JMenuItem("Elimina", new ImageIcon("images/elimina.gif"));
 		menuElimina.setMnemonic(KeyEvent.VK_E);
 		menuElimina.addActionListener(this);
		menu.add(menuElimina);

		menu.addSeparator();
 		menuitem = new JMenuItem("Esci");
 		menuitem.setMnemonic(KeyEvent.VK_C);
 		menuitem.addActionListener(this);
		menu.add(menuitem);
		
	 	menu = new JMenu("Cliente");
	 	menu.setMnemonic(KeyEvent.VK_L);
	 	menubar.add(menu);

 		menuitem = new JMenuItem("Nuovo cliente...", new ImageIcon("images/nuova.gif"));
 		menuitem.setMnemonic(KeyEvent.VK_N);
 		menuitem.addActionListener(this);
		menu.add(menuitem);

 		menuitem = new JMenuItem("Apri cliente...", new ImageIcon("images/apri.gif"));
 		menuitem.setMnemonic(KeyEvent.VK_A);
 		menuitem.addActionListener(this);
		menu.add(menuitem);
		
	 	menu = new JMenu("Strumenti");
	 	menu.setMnemonic(KeyEvent.VK_S);
	 	menubar.add(menu);

 		menuitem = new JMenuItem("Materiale");
 		menuitem.setMnemonic(KeyEvent.VK_M);
 		menuitem.addActionListener(this);
		menu.add(menuitem);

 		menuitem = new JMenuItem("Ricavo");
 		menuitem.setMnemonic(KeyEvent.VK_R);
 		menuitem.addActionListener(this);
		menu.add(menuitem);

 		menuitem = new JMenuItem("Convertitore");
 		menuitem.setMnemonic(KeyEvent.VK_C);
 		menuitem.addActionListener(this);
		menu.add(menuitem);

		/*
		 * creazione e aggiunta del menu (Help) alla barra dei menu
		 */
		menu = new JMenu("Help");
	 	menu.setMnemonic(KeyEvent.VK_H);
	 	menubar.add(menu);
		/* 
		 * aggiunta delle voci di menu (Guida in linea... e About...) al menu (Help)
		 */
		menuitem = new JMenuItem("Guida in linea...", new ImageIcon("images/help.gif"));
 		menuitem.setMnemonic(KeyEvent.VK_G);
		menuitem.addActionListener(this);
		menu.add(menuitem);
		menu.addSeparator();
 		menuitem = new JMenuItem("About...", new ImageIcon("images/about.gif"));
 		menuitem.setMnemonic(KeyEvent.VK_A);
		menuitem.addActionListener(this);
		menu.add(menuitem);
		
		JToolBar toolbar = new JToolBar();
		JButton button = null;
		button = new JButton(new ImageIcon("images/nuova.gif"));
        button.setToolTipText("Nuova fattura");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
	        	if (modi == true){
	        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
	        			AbilitaMenu(true);
	        			PannelloVuoto();
	        			NuovaFattura();
	        		}
	        	}else{
	    			AbilitaMenu(true);
	    			PannelloVuoto();
	    			NuovaFattura();
	        	}			
            }
        });
        toolbar.add(button);

		button = new JButton(new ImageIcon("images/apri.gif"));
        button.setToolTipText("Apri fattura");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				OpenFatt();
            }
        });
        toolbar.add(button);
		
		buttonSalva = new JButton(new ImageIcon("images/salva.gif"));
        buttonSalva.setToolTipText("Salva fattura");
        buttonSalva.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				SalvaFattura();
            }
        });
        toolbar.add(buttonSalva);

        toolbar.addSeparator();
		
		buttonElimina = new JButton(new ImageIcon("images/elimina.gif"));
        buttonElimina.setToolTipText("Elimina fattura");
        buttonElimina.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
				deleteFattura();
            }
        });
        toolbar.add(buttonElimina);

		toolbar.setFloatable(false);
		
		contenitore = new JPanel();
		
        contenitore.setLayout(new BorderLayout());
		//contenitore.setPreferredSize(null);
		
		contenitore.add(toolbar, BorderLayout.NORTH);

		AbilitaMenu(false);
		setContentPane(contenitore);
        
	}
	private void PannelloVuoto(){
		modi = false;
		try{
			contenitore.remove(1);
			setTitle(titolo);
			setContentPane(contenitore);
		}catch (Exception e){}	
	}	
		
	private void NuovoCliente(){
		modi = true;
		JPanel panelCliente = new JPanel();
		panelCliente.setLayout(null);
		panelCliente.setBackground(coloreBackCliente);

	 	Font smallFancy = new Font("Serif", Font.BOLD, 16);
		Font courier = new Font("Monospaced", Font.BOLD, 12);

		JLabel etiFatt = new JLabel("Gestione clienti");
		setTitle(titolo + " - Nuovo Cliente");
		etiFatt.setBounds(10,0,200,30);
		etiFatt.setFont(smallFancy);
		etiFatt.setForeground(Color.blue);
		panelCliente.add(etiFatt);
		JLabel eti1 = new JLabel("Nome cliente:");
		eti1.setBounds(10,30,100,30);
		eti1.setFont(smallFancy);
		panelCliente.add(eti1);
		JLabel eti2 = new JLabel("Intestazione:");
		eti2.setBounds(10,60,100,30);
		eti2.setFont(smallFancy);
		panelCliente.add(eti2);
		JLabel eti3 = new JLabel("Partita IVA:");
		eti3.setBounds(10,150,100,30);
		eti3.setFont(smallFancy);
		panelCliente.add(eti3);
		JLabel eti4 = new JLabel("Codice Fiscale:");
		eti4.setBounds(10,180,150,30);
		eti4.setFont(smallFancy);
		panelCliente.add(eti4);
		JLabel eti5 = new JLabel("Codice Fiscale 2:");
		eti5.setBounds(10,210,150,30);
		eti5.setFont(smallFancy);
		panelCliente.add(eti5);

		NomeCliente = new JTextField();
		NomeCliente.setBounds(140,35,250,20);
		NomeCliente.setFont(courier);
		panelCliente.add(NomeCliente);

		int1 = new JTextField();
		int1.setBounds(140,65,250,20);
		int1.setFont(courier);
		panelCliente.add(int1);

		int2 = new JTextField();
		int2.setBounds(140,85,250,20);
		int2.setFont(courier);
		panelCliente.add(int2);

		int3 = new JTextField();
		int3.setBounds(140,105,250,20);
		int3.setFont(courier);
		panelCliente.add(int3);
		
		int4 = new JTextField();
		int4.setBounds(140,125,250,20);
		int4.setFont(courier);
		panelCliente.add(int4);

		partIva = new JTextField();
		partIva.setBounds(140,155,150,20);
		partIva.setFont(courier);
		panelCliente.add(partIva);

		codF1 = new JTextField();
		codF1.setBounds(140,185,160,20);
		codF1.setFont(courier);
		panelCliente.add(codF1);

		codF2 = new JTextField();
		codF2.setBounds(140,215,160,20);
		codF2.setFont(courier);
		panelCliente.add(codF2);
		
		JButton inserisci = new JButton("Inserisci");
		inserisci.setBounds(80,255,100,40);
		inserisci.setBackground(coloreBackComboCliente);
		panelCliente.add(inserisci);
		inserisci.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				try{
					Cliente cliente = new Cliente(
							NomeCliente.getText(),
							int1.getText(),
							int2.getText(),
							int3.getText(),
							int4.getText(),
							partIva.getText(),
							codF1.getText(),
							codF2.getText());
					cliente.isNew();
					cliente.insertIntoDB();		
					JOptionPane.showMessageDialog(null, 
							"Nuovo cliente inserito.", "Facom",
							JOptionPane.INFORMATION_MESSAGE);
					PannelloVuoto();
					NuovaFattura();		
						
				}catch (clienteException e){
					JOptionPane.showMessageDialog(null, 
							e.getMessage(), "Facom",
							JOptionPane.WARNING_MESSAGE);
				}			
	
		 	}	
 		});

		JButton annulla = new JButton("Annulla");
		annulla.setBounds(200,255,100,40);
		annulla.setBackground(coloreBackComboCliente);
		panelCliente.add(annulla);
		
		annulla.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				PannelloVuoto();
		 	}	
 		});
		

		contenitore.add(panelCliente, BorderLayout.CENTER);
		setContentPane(contenitore);
	}
	private void RiempiCampiCliente(String nome){
		try{
			Cliente c = new Cliente(nome);
			NomeCliente.setText(c.getNome());
			int1.setText(c.getInt1());
			int2.setText(c.getInt2());
			int3.setText(c.getInt3());
			int4.setText(c.getInt4());
			partIva.setText(c.getPI());
			codF1.setText(c.getCF1());
			codF2.setText(c.getCF2());
					
		}catch (clienteException e){
			JOptionPane.showMessageDialog(null, 
					e.getMessage(), "Facom",
					JOptionPane.WARNING_MESSAGE);
		}			
	
				
	}		
	private void AggiornaCliente(){
		String[] listaclienti = dbFatture.getListaClienti();
		if (listaclienti[0].equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null, 
					"Non è presente nessun cliente", "Facom",
					JOptionPane.INFORMATION_MESSAGE);
		}else{
			modi = true;			
			JPanel panelCliente = new JPanel();
			panelCliente.setLayout(null);
			panelCliente.setBackground(coloreBackCliente);
	
		 	Font smallFancy = new Font("Serif", Font.BOLD, 16);
			Font courier = new Font("Monospaced", Font.BOLD, 12);
	
			JLabel etiFatt = new JLabel("Gestione clienti");
			setTitle(titolo + " - Aggiorna Cliente");
			etiFatt.setBounds(10,0,200,30);
			etiFatt.setFont(smallFancy);
			etiFatt.setForeground(Color.blue);
			panelCliente.add(etiFatt);
		 	comboClienti = new JComboBox(listaclienti);
		 	comboClienti.setBounds(140, 10, 250, 20);
		 	comboClienti.setBackground(coloreBackComboCliente);
		 	panelCliente.add(comboClienti);
			comboClienti.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					RiempiCampiCliente(comboClienti.getSelectedItem().toString());
			 	}	
	 		});		
			JLabel eti1 = new JLabel("Nome cliente:");
			eti1.setBounds(10,30,100,30);
			eti1.setFont(smallFancy);
			panelCliente.add(eti1);
			JLabel eti2 = new JLabel("Intestazione:");
			eti2.setBounds(10,60,100,30);
			eti2.setFont(smallFancy);
			panelCliente.add(eti2);
			JLabel eti3 = new JLabel("Partita IVA:");
			eti3.setBounds(10,150,100,30);
			eti3.setFont(smallFancy);
			panelCliente.add(eti3);
			JLabel eti4 = new JLabel("Codice Fiscale:");
			eti4.setBounds(10,180,150,30);
			eti4.setFont(smallFancy);
			panelCliente.add(eti4);
			JLabel eti5 = new JLabel("Codice Fiscale 2:");
			eti5.setBounds(10,210,150,30);
			eti5.setFont(smallFancy);
			panelCliente.add(eti5);
	
			NomeCliente = new JTextField();
			NomeCliente.setBounds(140,35,250,20);
			NomeCliente.setFont(courier);
			panelCliente.add(NomeCliente);
	
			int1 = new JTextField();
			int1.setBounds(140,65,250,20);
			int1.setFont(courier);
			panelCliente.add(int1);
	
			int2 = new JTextField();
			int2.setBounds(140,85,250,20);
			int2.setFont(courier);
			panelCliente.add(int2);
	
			int3 = new JTextField();
			int3.setBounds(140,105,250,20);
			int3.setFont(courier);
			panelCliente.add(int3);
			
			int4 = new JTextField();
			int4.setBounds(140,125,250,20);
			int4.setFont(courier);
			panelCliente.add(int4);
	
			partIva = new JTextField();
			partIva.setBounds(140,155,150,20);
			partIva.setFont(courier);
			panelCliente.add(partIva);
	
			codF1 = new JTextField();
			codF1.setBounds(140,185,160,20);
			codF1.setFont(courier);
			panelCliente.add(codF1);
	
			codF2 = new JTextField();
			codF2.setBounds(140,215,160,20);
			codF2.setFont(courier);
			panelCliente.add(codF2);
			
			JButton aggiorna = new JButton("Aggiorna");
			aggiorna.setBounds(80,255,100,40);
			aggiorna.setBackground(coloreBackComboCliente);
			panelCliente.add(aggiorna);
			aggiorna.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
						try{
							Cliente c = new Cliente(
									NomeCliente.getText(),
									int1.getText(),
									int2.getText(),
									int3.getText(),
									int4.getText(),
									partIva.getText(),
									codF1.getText(),
									codF2.getText());
									
							if (c.update()){
									JOptionPane.showMessageDialog(null, 
										"Cliente aggiornato.", "Facom",
										JOptionPane.INFORMATION_MESSAGE);
									PannelloVuoto();
									AggiornaCliente();	
							}else{
									JOptionPane.showMessageDialog(null, 
										"Impossibile aggiornare il cliente.", "Facom",
										JOptionPane.INFORMATION_MESSAGE);
							}
						}catch (clienteException e){
							JOptionPane.showMessageDialog(null, 
									e.getMessage(), "Facom",
									JOptionPane.WARNING_MESSAGE);
						}			
											
		
			 	}	
	 		});
	
			JButton elimina = new JButton("Elimina");
			elimina.setBounds(200,255,100,40);
			elimina.setBackground(coloreBackComboCliente);
			panelCliente.add(elimina);
			
			elimina.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					Object[] opzioni = {"Si","No"};
					int n = JOptionPane.showOptionDialog(null, 
						"Eliminare il cliente " + comboClienti.getSelectedItem().toString() + "?",
																"Conferma Eliminazione",
																JOptionPane.YES_NO_OPTION,
																JOptionPane.QUESTION_MESSAGE,
																null,
																opzioni,
																opzioni[1]);
					/*
					 * verifica della scelta selezionata
					 */											
					if (n == JOptionPane.YES_OPTION){
						if (dbFatture.eliminaCliente(comboClienti.getSelectedItem()
								.toString())){
								JOptionPane.showMessageDialog(null, 
									"Cliente eliminato.", "Facom",
									JOptionPane.INFORMATION_MESSAGE);
								PannelloVuoto();
								AggiornaCliente();	
									
						}else{
								JOptionPane.showMessageDialog(null, 
									"Impossibile eliminare il cliente.", "Facom",
									JOptionPane.INFORMATION_MESSAGE);
						}
					}	
								
						
			 	}	
	 		});
	
			JButton annulla = new JButton("Annulla");
			annulla.setBounds(320,255,100,40);
			annulla.setBackground(coloreBackComboCliente);
			panelCliente.add(annulla);
			
			annulla.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					PannelloVuoto();
			 	}	
	 		});
			RiempiCampiCliente(comboClienti.getSelectedItem().toString());
			
	
			contenitore.add(panelCliente, BorderLayout.CENTER);
			setContentPane(contenitore);
		}	
	}
	private void NuovaFattura(){
		String[] listaclienti = dbFatture.getListaClienti();
		if (listaclienti[0].equalsIgnoreCase("")){
			JOptionPane.showMessageDialog(null, 
					"Non è presente nessun cliente. Inserire un nuovo cliente\nprima di una nuova fattura.", "Facom",
					JOptionPane.INFORMATION_MESSAGE);
		}else{
   			AbilitaMenu(true);
			isNewFattura = true;
			modi = true;
			nuovaFattura = new Fattura();
			JPanel panelF = new JPanel();
			panelF.setLayout(null);
			panelF.setBackground(coloreBack);
		 	Font smallFancy = new Font("Serif", Font.BOLD, 16);
		 	Font smallSans = new Font("Sans Serif", Font.BOLD, 12);
			Font courier = new Font("Monospaced", Font.BOLD, 12);
	
			JLabel etiFatt = new JLabel("Nuova Fattura");
			setTitle(titolo + " - Nuova Fattura");
			etiFatt.setBounds(10,0,200,30);
			etiFatt.setFont(smallFancy);
			etiFatt.setForeground(Color.blue);
			panelF.add(etiFatt);

			JLabel etiC = new JLabel("Nome cliente:");
			etiC.setBounds(10,25,200,30);
			etiC.setFont(smallFancy);
			panelF.add(etiC);

		 	comboClienti = new JComboBox(listaclienti);
		 	comboClienti.setBounds(160, 30, 250, 20);
		 	comboClienti.setBackground(coloreBackCombo);
		 	panelF.add(comboClienti);
	
			JButton newCliente = new JButton("Nuovo cliente");
			newCliente.setBounds(430,30,110,20);
			newCliente.setBackground(coloreBackCombo);
			panelF.add(newCliente);
			newCliente.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					AbilitaMenu(false);
					PannelloVuoto();
					NuovoCliente();
			 	}	
	 		});
			
		 	JLabel dataIn = new JLabel("Data fattura (gg/mm/aaaa)");
			dataIn.setFont(smallFancy);
		 	dataIn.setBounds(10,55,200,30);
			panelF.add(dataIn);

			dataFattura = new JTextField();
			dataFattura.setBounds(200,60,100,20);
			dataFattura.setFont(courier);
			panelF.add(dataFattura);

		 	String[] ultima;
		 	ultima = dbFatture.getDataNumeroMassimo();
		 	if (!ultima[0].equalsIgnoreCase("")){
			 	JLabel datamax = new JLabel("(Ultima fattura: " + ultima[0] + ")");
				datamax.setFont(smallSans);
				datamax.setForeground(Color.gray);
			 	datamax.setBounds(420,55,200,30);
				panelF.add(datamax);
			 	JLabel nummax = new JLabel("(Numero ultima fattura: " + ultima[1] + ")");
				nummax.setFont(smallSans);
				nummax.setForeground(Color.gray);
			 	nummax.setBounds(210,85,200,30);
				panelF.add(nummax);
			}

			JButton giornoSett = new JButton("Giorno?");
			giornoSett.setBounds(320,60,83,20);
			giornoSett.setBackground(coloreBackCombo);
			panelF.add(giornoSett);
			giornoSett.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					if (!dataFattura.getText().equalsIgnoreCase("")){
						if (!DateMng.getGiornoSettimana(dataFattura.getText()).equalsIgnoreCase("")){
							MessaggioInformation(DateMng.getGiornoSettimana(dataFattura.getText()));
						}else{
							MessaggioWarning("Verificare che la data sia corretta\ne nel formato adeguato.");
						}		
					}	
			 	}	
	 		});
			

		 	JLabel numFatt = new JLabel("Numero Fattura: ");
			numFatt.setFont(smallFancy);
		 	numFatt.setBounds(10,85,200,30);
			panelF.add(numFatt);

			numeroFattura = new JTextField();
			numeroFattura.setBounds(160,90,40,20);
			numeroFattura.setFont(courier);
			panelF.add(numeroFattura);

			textArea = new JTextArea();
			textArea.setFont(courier);
			textArea.setLineWrap(true);
			textArea.setWrapStyleWord(true);
		 	textArea.setBounds(160,185,350,50);
			panelF.add(textArea);
			
		 	JLabel pagaFatt = new JLabel("Pagamento: ");
			pagaFatt.setFont(smallFancy);
		 	pagaFatt.setBounds(430,85,200,30);
			panelF.add(pagaFatt);

		 	comboPagamenti = new JComboBox(dbFatture.getPagamenti());
		 	comboPagamenti.setBounds(530, 90, 150, 20);
		 	comboPagamenti.setEditable(true);
		 	comboPagamenti.setBackground(coloreBackCombo);
		 	panelF.add(comboPagamenti);


		 	comboDescrizioni = new JComboBox(dbFatture.getDescrizioni());
		 	comboDescrizioni.setBounds(160, 155, 350, 20);
		 	comboDescrizioni.setBackground(coloreBackCombo);
		 	panelF.add(comboDescrizioni);

			comboDescrizioni.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					textArea.setText(comboDescrizioni.getSelectedItem().toString());

			 	}	
	 		});		
			
		 	JLabel descr = new JLabel("Descrizione");
			descr.setFont(smallFancy);
			descr.setForeground(Color.red);
		 	descr.setBounds(280,120,200,30);
			panelF.add(descr);

		 	comboMisure = new JComboBox(dbFatture.getMisure());
		 	comboMisure.setBounds(10, 185, 60, 20);
		 	comboMisure.setEditable(true);
		 	comboMisure.setBackground(coloreBackCombo);
		 	panelF.add(comboMisure);

		 	JLabel mis = new JLabel("Unità");
			mis.setFont(smallFancy);
			mis.setForeground(Color.red);
		 	mis.setBounds(20,120,50,30);
			panelF.add(mis);

		 	JLabel qta = new JLabel("Quantità");
			qta.setFont(smallFancy);
			qta.setForeground(Color.red);
		 	qta.setBounds(80,120,100,30);
			panelF.add(qta);

			quantita = new JTextField();
			quantita.setBounds(80,185,63,20);
			quantita.setFont(courier);
			panelF.add(quantita);
			
		 	JLabel prez = new JLabel("Prezzo €");
			prez.setFont(smallFancy);
			prez.setForeground(Color.red);
		 	prez.setBounds(540,120,100,30);
			panelF.add(prez);

		 	JLabel imp = new JLabel("Importo €");
			imp.setFont(smallFancy);
			imp.setForeground(Color.red);
		 	imp.setBounds(640,120,100,30);
			panelF.add(imp);

			prezzo = new JTextField();
			prezzo.setBounds(530,185,83,20);
			prezzo.setFont(courier);
			panelF.add(prezzo);
			
			JButton insVoce = new JButton("Ins.voce");
			insVoce.setBounds(530,215,83,20);
			insVoce.setBackground(coloreBackCombo);
			panelF.add(insVoce);
			insVoce.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					try{
						if (!textArea.getText().equalsIgnoreCase("")){
							if(dbFatture.putVoce(textArea.getText())){
								MessaggioInformation("Voce inserita");
								comboDescrizioni.addItem(textArea.getText());
							}else{
								MessaggioInformation("La voce è già presente nel database");
							}	
							
						}else{
							MessaggioInformation("L'area di testo è vuota.");
						}	
					}catch (Exception e){
							importo.setText("");
							
					}
			 	}	
	 		});
	 		
	 		importo = new JTextField();
			importo.setBounds(630,185,83,20);
			importo.setFont(courier);
			panelF.add(importo);

			JButton calcola = new JButton("calcola");
			calcola.setBounds(630,155,83,20);
			calcola.setBackground(coloreBackCombo);
			panelF.add(calcola);
			calcola.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					try{
						if (!quantita.getText().equalsIgnoreCase("") && !prezzo.getText().equalsIgnoreCase("")){

							importo.setText(nf
							.format(nf.parse(quantita.getText()).floatValue()*nf.parse(prezzo.getText()).floatValue()));
							//System.out.println(nf.parse(quantita.getText()).floatValue());
						}else{
							importo.setText("");

						}	
					}catch (Exception e){
							importo.setText("");
							
					}
			 	}	
	 		});
			modelloListaR = new DefaultListModel();
	 		listaR = new JList();
	 		listaR.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	 		listaR.setModel(modelloListaR);
		 	JScrollPane scrollList = new JScrollPane(listaR);
		 	scrollList.setBounds(10, 250, 603, 150);
	 	 	panelF.add(scrollList);
	 	 	
			inserisci = new JButton("Inserisci");
			inserisci.setBounds(630,250,90,20);
			inserisci.setBackground(coloreBackCombo);
			panelF.add(inserisci);
			inserisci.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					try{
						if (modificaRiga){
							nuovaFattura.replaceRiga(comboMisure.getSelectedItem().toString(),
										quantita.getText(),
										textArea.getText(),
										prezzo.getText(),
										importo.getText(),
										indiceModificaRiga);
							modelloListaR.remove(indiceModificaRiga);			
							modelloListaR.add(indiceModificaRiga, nuovaFattura.getRiga(indiceModificaRiga).toString());	
							modificaRiga = false;
							inserisci.setText("Inserisci");
							CalcolaTotali();
						}else{	
							nuovaFattura.addRiga(comboMisure.getSelectedItem().toString(),
										quantita.getText(),
										textArea.getText(),
										prezzo.getText(),
										importo.getText());
							modelloListaR.addElement(nuovaFattura.getUltimaRigaInserita());	
							CalcolaTotali();

						}
						quantita.setText("");
						textArea.setText("");
						prezzo.setText("");
						importo.setText("");
					}catch (FatturaException e){
						JOptionPane.showMessageDialog(null, 
										e.getMessage(), "Facom",
										JOptionPane.WARNING_MESSAGE);
					}
										
											
			 	}	
	 		});
			modifica = new JButton("Modifica");
			modifica.setBounds(630,280,90,20);
			modifica.setBackground(coloreBackCombo);
			panelF.add(modifica);
			modifica.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					if (listaR.getSelectedIndex() != -1){
						inserisci.setText("Aggiorna");
						indiceModificaRiga = listaR.getSelectedIndex();
						modificaRiga = true;
						comboMisure.setSelectedItem(
							nuovaFattura.getRiga(indiceModificaRiga).getUnitaObject());
						quantita.setText(nuovaFattura.getRiga(indiceModificaRiga).getQuantita());
						textArea.setText(nuovaFattura.getRiga(indiceModificaRiga).getDescrizione());
						prezzo.setText(nuovaFattura.getRiga(indiceModificaRiga).getPrezzo());
						importo.setText(nuovaFattura.getRiga(indiceModificaRiga).getImporto());
							
					}	
			 	}	
	 		});
	 	 	JButton elimina = new JButton("Elimina");
			elimina.setBounds(630,310,90,20);
			elimina.setBackground(coloreBackCombo);
			panelF.add(elimina);
			elimina.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					if (listaR.getSelectedIndex() != -1){
						if (modificaRiga){
							new MessageInformation("Terminare l'aggiornamento della riga\n prima di eliminare.", "Facom");
						}else if (Conferma("Rimuovere la riga selezionata?")){
							nuovaFattura.removeRiga(listaR.getSelectedIndex());
							modelloListaR.remove(listaR.getSelectedIndex());
							CalcolaTotali();
						}	
					}	
						
			 	}	
	 		});
	 		
	 		JLabel impo = new JLabel("Imponibile");
			impo.setFont(smallFancy);
		 	impo.setBounds(10,420,100,30);
			panelF.add(impo);
	 		
	 		
	 	 	imponibile = new JTextField();
	 	 	imponibile.setEditable(false);
			imponibile.setBounds(110,425,83,20);
			imponibile.setFont(courier);
			imponibile.setHorizontalAlignment(JTextField.RIGHT);
			panelF.add(imponibile);

	 		JLabel lblIva = new JLabel("I.V.A.");
			lblIva.setFont(smallFancy);
		 	lblIva.setBounds(10,450,100,30);
			panelF.add(lblIva);
	 		
	 		
	 	 	iva = new JTextField();
	 	 	iva.setEditable(false);
			iva.setBounds(110,455,83,20);
			iva.setFont(courier);
			iva.setHorizontalAlignment(JTextField.RIGHT);
			panelF.add(iva);

	 		JLabel lblTot = new JLabel("Totale");
			lblTot.setFont(smallFancy);
		 	lblTot.setBounds(10,480,100,30);
			panelF.add(lblTot);
	 		
	 		
	 	 	totale = new JTextField();
	 	 	totale.setEditable(false);
			totale.setBounds(110,485,83,20);
			totale.setFont(courier);
			totale.setHorizontalAlignment(JTextField.RIGHT);
			panelF.add(totale);

	 		JLabel lblIvap = new JLabel("% I.V.A.");
			lblIvap.setFont(smallFancy);
		 	lblIvap.setBounds(210,450,100,30);
			panelF.add(lblIvap);

		 	comboIva = new JComboBox(dbFatture.getIva());
		 	comboIva.setBounds(280, 455, 50, 20);
		 	comboIva.setEditable(true);
		 	comboIva.setBackground(coloreBackCombo);
		 	panelF.add(comboIva);

			comboIva.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					CalcolaTotali();
			 	}	
	 		});		
			JButton annulla = new JButton("Chiudi");
			annulla.setBounds(630,455,100,40);
			annulla.setBackground(coloreBackCombo);
			panelF.add(annulla);
			
			annulla.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
        			if (Conferma("Le modifiche non salvate\n andranno perse, chiudere?")){
						AbilitaMenu(false);
						PannelloVuoto();
					}	
			 	}	
	 		});
			JButton salva = new JButton("Salva");
			salva.setBounds(500,455,100,40);
			salva.setBackground(coloreBackCombo);
			panelF.add(salva);
			
			salva.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent evt){
					SalvaFattura();
			 	}	
	 		});
	 		
			
				
			contenitore.add(panelF, BorderLayout.CENTER);
			setContentPane(contenitore);
			CalcolaTotali();
		}	
	}
	private void CalcolaTotali(){
		try{
			imponibile.setText(nf.format(nuovaFattura.calcolaImponibile()));
			iva.setText(nf.format(nuovaFattura.calcolaImponibile()
						*Double.parseDouble(comboIva.getSelectedItem().toString())/100));
			totale.setText(nf.format(nuovaFattura.calcolaImponibile()
						*(1 + (Double.parseDouble(comboIva.getSelectedItem().toString())/100))));			
		}catch (NumberFormatException e){
			MessaggioWarning("Controllare che la percentuale IVA sia stata\ninserita correttemente");
			comboIva.setSelectedIndex(0);
		}					
	}	
			
	private void AbilitaMenu(boolean flag){
       	menuChiudi.setEnabled(flag);
       	menuSalva.setEnabled(flag);
       	menuElimina.setEnabled(flag);
       	buttonSalva.setEnabled(flag);
       	buttonElimina.setEnabled(flag);
       	
	}
	public void actionPerformed(ActionEvent e) {
	    /*
	     * crea l'oggetto con la fonte dell'evento
	     */
	    JMenuItem source = (JMenuItem)(e.getSource());
		/*
		 * in base alla voce selezionata invoca i vari metodi
		 */
        if (source.getText() == "Nuova..."){
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
        			AbilitaMenu(true);
        			PannelloVuoto();
        			NuovaFattura();
        		}
        	}else{
    			AbilitaMenu(true);
    			PannelloVuoto();
    			NuovaFattura();
        	}			
			
        }else if (source.getText() == "Chiudi"){
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
        			AbilitaMenu(false);
        			PannelloVuoto();
        		}
			}else{
    			AbilitaMenu(false);
    			PannelloVuoto();
    		}	
        }else if (source.getText() == "Salva"){
			SalvaFattura();	

        }else if (source.getText() == "Apri..."){
			OpenFatt();

        }else if (source.getText() == "Esci"){
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
		        	System.exit(0);
        		}
			}else{
	        	System.exit(0);
    		}	

        }else if (source.getText() == "Elimina"){
			deleteFattura();

        }else if (source.getText() == "Nuovo cliente..."){
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
        			AbilitaMenu(false);
					PannelloVuoto();
					NuovoCliente();
        		}
			}else{
       			AbilitaMenu(false);
				PannelloVuoto();
				NuovoCliente();
    		}	

        }else if (source.getText() == "Apri cliente..."){
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
        			AbilitaMenu(false);
					PannelloVuoto();
					AggiornaCliente();
        		}
			}else{
       			AbilitaMenu(false);
				PannelloVuoto();
				AggiornaCliente();
    		}	

        }else if (source.getText() == "Materiale"){
        	/*
        	 * crea la finestra materiale
        	 */
			Materiale materiale = new Materiale();
			materiale.setSize(700,500);
			materiale.setLocation(200,160);
			materiale.setIconImage(new ImageIcon("images/facom.gif").getImage());
			materiale.setVisible(true);	
			
       	}else if (source.getText() == "Ricavo"){
       		Fatturato();
		}else if (source.getText() == "Convertitore"){
			Convertitore conv = new Convertitore("Convertitore");
			conv.setVisible(true);
        	
       	}else if (source.getText() == "Guida in linea..."){
        	/*
        	 * crea la finestra della guida
        	 */
        }else if (source.getText() == "About..."){
        	/*
        	 * crea e visualizza il messaggio con le informazioni sull'applicazione
        	 */
			JOptionPane.showMessageDialog(null, 
							"Fatture Commerciali\nby Gianluca Tavella\ngianluca.tavella@libero.it\n" +
							"Luglio 2003\nVer 1.2.0", "About Facom",
							JOptionPane.INFORMATION_MESSAGE);


        }	
        					
        	

	}
	private boolean Conferma(String s){
		if (new MessageConfirm().isYes(s, "Facom", MessageConfirm.OPTION_NO)){
			return true;
		}else{
			return false;
		}		
	}
	private void Fatturato(){
   		String annoVolume = JOptionPane.showInputDialog(null,"Inserire l'anno","Facom - Calcolo Ricavo",
				JOptionPane.QUESTION_MESSAGE);
		if (annoVolume != null){
			try{
				Integer.parseInt(annoVolume);
				String fatturato = dbFatture.getFatturato(annoVolume);
				if (!fatturato.equalsIgnoreCase("")){
					JOptionPane.showMessageDialog(null, "Il ricavo per l'anno " +
						annoVolume + " è pari a € " + nf.format(Float.parseFloat(fatturato)) + ".",
						"Facom - Ricavo " + annoVolume,
						JOptionPane.INFORMATION_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(null, "Non sono presenti fatture relative all'anno " +
						annoVolume + ".",
						"Facom - Ricavo " + annoVolume,
						JOptionPane.WARNING_MESSAGE);
				}			
	
			}catch (Exception e) {
					JOptionPane.showMessageDialog(null, "Inserire l'anno in cifre.",
						"Facom - Ricavo",
						JOptionPane.WARNING_MESSAGE);
					Fatturato();	
			}		
		}		
	}			

	private void SalvaFattura(){
    	if (new MessageConfirm().isYes("Salvare la fattura", "Facom", MessageConfirm.OPTION_YES)){
	    	try{
	    		nuovaFattura.crea(new Cliente(comboClienti.getSelectedItem().toString()),
	    							dataFattura.getText(),
	    							numeroFattura.getText(),
	    							comboPagamenti.getSelectedItem().toString(),
	    							imponibile.getText(),
	    							comboIva.getSelectedItem().toString(),
	    							iva.getText(),
	    							totale.getText(),
	    							isNewFattura);
	    		nuovaFattura.creaFile();
	    		nuovaFattura.insertIntoDB(isNewFattura);
	    		modi = false;
	    	}catch (Exception ex){
	    		new MessageWarning(ex.getMessage(), "Facom");
	    	}
	    }			
	}
	public void ApriFattura(int parAnno, int parNumero){
    	try{
        	if (modi == true){
        		if (Conferma("Le modifiche non salvate\n andranno perse, continuare?")){
		    		PannelloVuoto();
		    		NuovaFattura();
					setTitle(titolo + " - Fattura");
		    		nuovaFattura = new Fattura(parAnno, parNumero);
		    		isNewFattura = false;
		    		dataFattura.setText(nuovaFattura.getData());
		    		numeroFattura.setText(nuovaFattura.getNumero());
		    		comboClienti.setSelectedItem(nuovaFattura.getNomeClienteObject());
		    		comboPagamenti.setSelectedItem(nuovaFattura.getPagamentoObject());
		    		comboIva.setSelectedItem(nuovaFattura.getIvaPercObject());
					Riga[] r = nuovaFattura.getRighe();
					for (int i = 0; i < r.length; i++){
						modelloListaR.add(i, nuovaFattura.getRiga(i).toString());
					}
					CalcolaTotali();
        		}
			}else{
	    		PannelloVuoto();
	    		NuovaFattura();
				setTitle(titolo + " - Fattura");
	    		nuovaFattura = new Fattura(parAnno, parNumero);
	    		isNewFattura = false;
	    		dataFattura.setText(nuovaFattura.getData());
	    		numeroFattura.setText(nuovaFattura.getNumero());
	    		comboClienti.setSelectedItem(nuovaFattura.getNomeClienteObject());
	    		comboPagamenti.setSelectedItem(nuovaFattura.getPagamentoObject());
	    		comboIva.setSelectedItem(nuovaFattura.getIvaPercObject());
				Riga[] r = nuovaFattura.getRighe();
				for (int i = 0; i < r.length; i++){
					modelloListaR.add(i, nuovaFattura.getRiga(i).toString());
				}
				CalcolaTotali();
    		}	
				

			
    	}catch (Exception e){
    		new MessageWarning(e.getMessage(), "Facom");
    	}		
		
	}
	private void OpenFatt(){
		Apri apri = new Apri(this);
		this.setEnabled(false);
		apri.setSize(500,200);
		apri.setLocation(200,160);
		apri.setTitle("Apri fattura");
		apri.setIconImage(new ImageIcon("images/facom.gif").getImage());
		apri.setVisible(true);	
	}
	private void deleteFattura(){
		if (isNewFattura){
			new MessageInformation("La fattura non è ancora stata salvata.", "Facom");
		}else{	
	    	if (new MessageConfirm().isYes("Eliminare la fattura corrente?", "Facom", MessageConfirm.OPTION_NO)){
				try{
					nuovaFattura.delete();
					new MessageInformation("Fattura eliminata", "Facom");
	    			AbilitaMenu(false);
	    			PannelloVuoto();
				}catch (FatturaException e){
					new MessageWarning(e.getMessage(), "Facom");
				}
			}
		}	
	}			
				
	private void MessaggioWarning(String s){
		JOptionPane.showMessageDialog(null, 
				s, "Facom",
				JOptionPane.WARNING_MESSAGE);
	}			
	private void MessaggioInformation(String s){
		JOptionPane.showMessageDialog(null, 
				s, "Facom",
				JOptionPane.INFORMATION_MESSAGE);
	}			
						
		
		
	public static void main(String[] args){
		Facom facom = new Facom();
		facom.setSize(800,630);
		facom.setLocation(100,60);
		facom.setIconImage(new ImageIcon("images/facom.gif").getImage());
		facom.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		facom.setVisible(true);	
	
	}
}	
