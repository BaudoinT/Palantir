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
	
	public void setMessageChiffre(byte[] t){
		mess_chiff = t;
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
			specification = new SecretKeySpec(cle.getBytes(), "AES");
		    Cipher dechiffreur = Cipher.getInstance("AES");
		    dechiffreur.init(Cipher.DECRYPT_MODE, specification);
		    mess_dechiff = dechiffreur.doFinal(mess_chiff);
		} catch(Exception e) {
			System.out.println("erreur dechiffrement aes :"+e);
		    return false;
		}
		return true;
	}
	


}

