/* � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �
   �  by Gianluca Tavella - gianluca.tavella@libero.it                       �
   �  Ott 2002                                                               �
   �  Facom - Fatture Commerciali                                            �
   � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � � �
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Classe mater: oggetto per la gestione del materiale
 *
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

class mater{
	private String descrizione;
	private float prezzo;
	private int anno;
	public mater(String desc, float pre, int an){
		descrizione = desc;
		prezzo = pre;
		anno = an;
	}
	public String getDescrizione(){
		return descrizione;
	}
	public float getPrezzo(){
		return prezzo;
	}
	public int getAnno(){
		return anno;
	}
	public Object[] toObject(){
		Object[] obj = {descrizione, new Float(prezzo), new Integer(anno)};
		return obj;
	}	
}					
