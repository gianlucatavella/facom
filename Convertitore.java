/* ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
   ø  by Gianluca Tavella - gianluca.tavella@libero.it                       ø
   ø                                                                         ø
   ø  JAVA ISLAND PROJECT - http://www.geocities.com/javaisland2001/         ø
   ø  Ottobre 2002                                                           ø
   ø  Convertitore -  Converte Lire-Euro                                     ø
   ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø ø
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *                                                                              
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *
 */
/*
 * import dei package necessari
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.*;
import java.util.*;

class Convertitore extends JFrame{
	///////////////////////////////////////
	//////// Oggetti di classe ////////////
	///////////////////////////////////////
	JButton bottoneE = new JButton("Euro");
	JButton bottoneL = new JButton("Lire");
	JLabel l = new JLabel("Lire");
	JLabel e = new JLabel("Euro");
	JTextField txtQuery = new JTextField();
	Container cp = this.getContentPane();
	//DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.ENGLISH));
	private NumberFormat df = NumberFormat.getInstance(Locale.ITALIAN);
	private NumberFormat dfLire = NumberFormat.getInstance(Locale.ITALIAN);
	// costruttore
	public Convertitore(String title){
		/*
		 * creazione della finestra e dei vari elementi che la compongono
		 */
		super(title);
		this.setSize(200,130);
		this.setLocation(200,100);
		
		this.setResizable(false);
		cp.setBackground(Color.red);
		cp.setLayout(null);

		Font courier = new Font("Monospaced", Font.BOLD, 12);
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        dfLire.setMaximumFractionDigits(0);
        dfLire.setMinimumFractionDigits(0);
		

		bottoneE.setBounds(10,60,60,20);
		bottoneE.setBackground(Color.orange);
		
		cp.add(bottoneE);

		bottoneL.setBounds(80,60,60,20);
		bottoneL.setBackground(Color.orange);
		
		cp.add(bottoneL);

		e.setBounds(160,10,60,30);
		e.setVisible(false);
		cp.add(e);

		l.setBounds(160,10,60,30);
		l.setVisible(false);
		cp.add(l);

		txtQuery.setBounds(10, 10, 130, 30);
		txtQuery.setFont(courier);
		cp.add(txtQuery);
		
		
		// gestione dell'evento chiusura finestra
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing (WindowEvent e){
				setVisible(false);
			}
		});
		
		// gestione evento pressione pulsante
		// gestione evento pressione pulsante
		bottoneE.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				// controlla che la stringa non sia vuota
				if (txtQuery.getText() != null){
					try{
						e.setVisible(true);
						l.setVisible(false);
						txtQuery.setText(df.format(df.parse(txtQuery.getText()).doubleValue()/1936.27));
					}catch (Exception e){
						txtQuery.setText("Error");	
					}
						
				}	
		
			}
		});		

		// gestione evento pressione pulsante
		bottoneL.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				// controlla che la stringa non sia vuota
				if (txtQuery.getText() != null){
					try{
						l.setVisible(true);
						e.setVisible(false);
						txtQuery.setText(dfLire.format(df.parse(txtQuery.getText()).doubleValue()*1936.27));
					}catch (Exception e){
						txtQuery.setText("Error");	
					}
						
				}	
		
			}
		});		

	}
	// inizio dell'applicazione
	public static void main(String[] args){
			Convertitore finestra = new Convertitore("Convertitore");
			finestra.setVisible(true);
	}		
}		