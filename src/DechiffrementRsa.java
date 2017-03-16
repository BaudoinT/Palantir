
import java.security.PrivateKey;

import javax.crypto.Cipher;

public class DechiffrementRsa {
	
	private String nom_fich_cle_privee;
	private byte[] mess_dechiffre;
	private byte[] mess_chiffre;
	
	public DechiffrementRsa(String cle, byte[] mess){
		nom_fich_cle_privee = cle;
		mess_chiffre = mess;
	}
	
	public boolean dechiffrement(){
		PrivateKey clePrivee = GestionClesRsa.lectureClePrivee(nom_fich_cle_privee);
		byte[] tab = null;
		try {
		    Cipher dechiffreur = Cipher.getInstance("RSA");
		    dechiffreur.init(Cipher.DECRYPT_MODE, clePrivee);
		    tab = dechiffreur.doFinal(mess_chiffre);
		} catch(Exception e) {
			System.out.println("erreur dechiffrement rsa :"+e);
		    return false;
		} 
		mess_dechiffre = tab;
		return true;
	}
	
	public byte[] getMessDeChiffree(){
		return mess_dechiffre;
	}
}
