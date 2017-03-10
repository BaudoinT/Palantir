package projet_secu;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class ChiffrementAes {

	private String cle="";
	private byte[] mess_clair;
	private byte[] mess_chiff;
	private byte[] mess_dechiff;
	private SecretKeySpec specification = null;
	
	
	
	public void generationcle(){
		char car = 'a';
		int c = (int) car;
		for(int i = 0 ; i <16 ;i++){
			int x = (int) (0 + (Math.random() * (26 - 0)));			
			cle+= (char) (c+x);
		}
	}
	public void setMessage(byte []s){
		mess_clair = s;
	}

	public void setCle(String s){
		cle = s;
	}
	public String getCle() {
		return cle;
	}

	public byte[] getMess_clair() {
		return mess_clair;
	}

	public byte[] getMess_chiff() {
		return mess_chiff;
	}
	
	public byte[] getMess_dechiff(){
		return mess_dechiff;
	}
	

	public boolean chiffrement(){
		try {
			specification = new SecretKeySpec(cle.getBytes(), "AES");
		    Cipher chiffreur = Cipher.getInstance("AES");
		    chiffreur.init(Cipher.ENCRYPT_MODE, specification);
		    mess_chiff = chiffreur.doFinal(mess_clair);
		} catch(Exception e) {
			System.out.println("erreur chiffrement aes :"+e);
		    return false;
		}
		return true;
	}
	
	public boolean dechiffrement(){

		try {
		    Cipher dechiffreur = Cipher.getInstance("AES");
		    dechiffreur.init(Cipher.DECRYPT_MODE, specification);
		    mess_dechiff = dechiffreur.doFinal(mess_chiff);
		} catch(Exception e) {
			System.out.println("erreur dechiffrement aes :"+e);
		    return false;
		}
		return true;
	}
	
/*	
	public static void main(String [] args){
		
		String message = "Hello world !!";
		ChiffrementAes chiff = new ChiffrementAes(message.getBytes());
		chiff.generationcle();
		String cle = chiff.getCle();
		
		System.out.println("Le message clair est : "+message);
		System.out.println("La cle est :"+cle );
		
		chiff.chiffrement();
		byte[] mess_chiff= chiff.getMess_chiff();
		
		System.out.println("Le message chiffre est : "+new String(mess_chiff));
		chiff.dechiffrement();
		byte[] mess_dechiff = chiff.getMess_dechiff();
		System.out.println("Le message dechiffre est : "+new String(mess_dechiff));
	}
*/
}

