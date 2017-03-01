package chiff_rsa;


import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

public class GenerateurCleRsa {
	
	private KeyPairGenerator generateur = null;
	private PrivateKey priv = null;
	private PublicKey pub = null; 
	
	public boolean generator(){
		try {
		    generateur = KeyPairGenerator.getInstance("RSA");
		    generateur.initialize(2048);
		} catch(Exception e) {
			 System.out.println("Erreur lors de l'initialisation du générateur de clés : " + e);
			return false;
		}
		KeyPair paireCles = generateur.generateKeyPair();
		priv = paireCles.getPrivate();
		pub = paireCles.getPublic();
		return true;
	}
	
	public PublicKey getPublicKey(){
		return pub;
	}
	
	public PrivateKey getPrivateKey(){
		return priv;
	}
}