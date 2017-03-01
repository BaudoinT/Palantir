package chiff_rsa;

public class TestMain {

	public static void main(String[] args) {
	
		// generation des cleés
		GenerateurCleRsa gen = new GenerateurCleRsa();
		gen.generator();
		
		// sauvegarde des clées
		GestionClesRsa ges = new GestionClesRsa();
		ges.sauvegardeClePrivee(gen.getPrivateKey(),"/home/baudryo/cle_priv");
		ges.sauvegardeClePublic(gen.getPublicKey(),"/home/baudryo/cle_pub");

		String mess_clair = "Hello world !!";
		System.out.println("Le message clair est : "+mess_clair+"\n");
		
		
		// chiffrement du message 
		ChiffrementRsa chif = new ChiffrementRsa("/home/baudryo/cle_pub",mess_clair.getBytes());
		chif.chiffrement();
		
		byte[] mess_chif = chif.getMessChiffre();
		System.out.println("Le message chiffré est : "+mess_chif+"\n");
		
		
		// dechiffrement du message
		DechiffrementRsa dechif = new DechiffrementRsa("/home/baudryo/cle_priv", mess_chif);
		dechif.dechiffrement();
		
		
		String mess_dechif =  new String(dechif.getMessDeChiffree());
		System.out.println("Le message dechiffré est : "+mess_dechif+"\n");
		

	}

}
