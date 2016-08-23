import java.text.*;
import java.util.*;
import island.util.*;

class Riga{
	private NumberFormat nf = NumberFormat.getInstance(Locale.ITALIAN);
	private NumberFormat nfQP = NumberFormat.getInstance(Locale.ITALIAN);
	private String unita;
	private String quantita;
	private String descrizione;
	private String prezzo;
	private String importo;

	public Riga(String unita, String quantita, String descrizione, String prezzo, String importo)
				throws RigaException{
	    nf.setMaximumFractionDigits(2);
	    nf.setMinimumFractionDigits(2);
		if (descrizione.equalsIgnoreCase("")){
			throw new RigaException("E' necessario inserire una descrizione.");
		}	
	    if (!quantita.equalsIgnoreCase("") && !prezzo.equalsIgnoreCase("")){
	    	try{
	 			importo = nf.format(nf.parse(quantita).floatValue()*nf.parse(prezzo).floatValue());
	 			quantita = nfQP.format(nfQP.parse(quantita).floatValue());
	 			prezzo = nf.format(nf.parse(prezzo).floatValue());
	 			
	 		}catch (ParseException e){
	 			throw new RigaException("Controllare che quantità e prezzo siano stati inseriti correttamente,\n con la virgola come separatore dei decimali");	
	 		}	
	 	}else if(!importo.equalsIgnoreCase("")){
	 		try{
	 			importo = nf.format(nf.parse(importo).floatValue());
	 		}catch (ParseException e){
		 		throw new RigaException("Controllare che l'importo sia stato inserito correttamente,\n con la virgola come separatore dei decimali");	
 			}
 		}		
	 	
	 	// OR esclusivo (o solo uno o solo l'altro)
	 	if (quantita.equalsIgnoreCase("") ^ prezzo.equalsIgnoreCase("")){ 
	 			throw new RigaException("E' necessario inserire sia il prezzo\n che la quantità, oppure nessuno dei due.");	
	 	}	
	 	try{

	 		if (!quantita.equalsIgnoreCase("")){
	 			nf.parse(quantita).floatValue();
	 		}	
	 		if (!prezzo.equalsIgnoreCase("")){
	 			nf.parse(prezzo).floatValue();
	 		}	
	 		if (!importo.equalsIgnoreCase("")){
	 			nf.parse(importo).floatValue();
	 		}	
	 	}catch (Exception e){
	 		throw new RigaException("Controllare che quantità, prezzo e importo siano stati inseriti correttamente,\n con la virgola come separatore dei decimali");	
	 	}
	 	
	 	String[] sottoStringhe = StringMng.getSubStringsInt(descrizione, 51);
	 	if (sottoStringhe.length > 3){
	 		throw new RigaException("La descrizione è troppo lunga. La lunghezza massima\ndella descrione è pari a 51 caratteri per 3 righe.");
	 	}	
	 	try{
		 		
		 	this.unita = unita;
		 	this.quantita = quantita;
		 	this.descrizione = descrizione;
		 	this.prezzo = prezzo;
		 	this.importo = importo;
		}catch (Exception e){
	 		throw new RigaException("Controllare che non vi siano valori nulli.");
		}				 	
	}
	public float getValoreQuantita(){
		try{
			return nf.parse(quantita).floatValue();
		}catch (ParseException e){
			return 0;
		}
	}		
	public float getValorePrezzo(){
		try{
			return nf.parse(prezzo).floatValue();
		}catch (ParseException e){
			return 0;
		}
	}		
	public float getValoreImporto(){
		try{
			return nf.parse(importo).floatValue();
		}catch (ParseException e){
			return 0;
		}
	}
	public String getUnita(){
		return unita;
	}
	public Object getUnitaObject(){
		return (Object) unita;
	}	
	public String getQuantita(){
		return quantita;
	}
	public String getPrezzo(){
		return prezzo;
	}
	public String getImporto(){
		return importo;
	}

	public String getDescrizione(){
		return descrizione;
	}
	public String[] getDescrizioneDivisa(){
		return StringMng.getSubStringsInt(descrizione, 51);
	}
	public int getNumeroRigheDescrizione(){
		return StringMng.getSubStringsInt(descrizione, 51).length;
	}	
	public String toString(){
		return " " + unita + " | " + quantita + " | " + descrizione + " | " + prezzo + " | " + importo;
	}				

		
}	    
			
		