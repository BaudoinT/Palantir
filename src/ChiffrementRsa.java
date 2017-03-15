

import java.security.PublicKey;

import javax.crypto.Cipher;

public class ChiffrementRsa {
	
	private String nom_fich_cle_public;
	private byte[] mess_clair;
	private byte[] mess_chiffre;
	
	
	public ChiffrementRsa(String cle, byte[] mess){
		nom_fich_cle_public = cle;
		mess_clair = mess;
	}
	
	public boolean chiffrement(){
		PublicKey clePublique = GestionClesRsa.lectureClePublic(nom_fich_cle_public);
		byte[] tab = null;
		try {
		    Cipher chiffreur = Cipher.getInstance("RSA");
		    chiffreur.init(Cipher.ENCRYPT_MODE, clePublique);
		    tab = chiffreur.doFinal(mess_clair);
		    mess_chiffre = tab;
		} catch(Exception e) {
			System.out.println("erreur chiffrement rsa :"+e);
		    return false;
		}
		return true;
	}
	
	public byte[] getMessChiffre(){
		return mess_chiffre;
	}
	
}
