import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class GestionClesRsa {

	
    public static void sauvegardeClePublic(PublicKey clePublic, String nomFichier) {
		RSAPublicKeySpec specification = null;
		try {
		    KeyFactory usine = KeyFactory.getInstance("RSA");
		    specification = usine.getKeySpec(clePublic, RSAPublicKeySpec.class);
		    
		    ObjectOutputStream fichier = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomFichier)));
		    fichier.writeObject(specification.getModulus());
		    fichier.writeObject(specification.getPublicExponent());
		    fichier.close();	
		    
		} catch(Exception e) {
			System.out.println("erreur sauvegarde cle public rsa : "+e);
		} 
    }
 
    public static void sauvegardeClePrivee(PrivateKey clePrivee, String nomFichier) {
		RSAPrivateKeySpec specification = null;
		try {
		    KeyFactory usine = KeyFactory.getInstance("RSA");
		    specification = usine.getKeySpec(clePrivee, RSAPrivateKeySpec.class);
		    
		    ObjectOutputStream fichier = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(nomFichier)));
		    fichier.writeObject(specification.getModulus());
		    fichier.writeObject(specification.getPrivateExponent());
		    fichier.close();	
		} catch(Exception e) {
			System.out.println("erreur sauvegarde cle privée rsa : "+e);
		} 
    }

    
    public static PrivateKey lectureClePrivee(String nomFichier){
		BigInteger modulo = null;
		BigInteger exposant = null;
		PrivateKey clePrivee = null;
		try {
		    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomFichier)));	    
		    modulo = (BigInteger) ois.readObject();
		    exposant = (BigInteger) ois.readObject();
		    
		    RSAPrivateKeySpec specification = new RSAPrivateKeySpec(modulo, exposant);
		    KeyFactory usine = KeyFactory.getInstance("RSA");
		    clePrivee = usine.generatePrivate(specification);
		} catch(Exception e) {
			System.out.println("erreur lecture cle privée rsa : "+e);
		}
		return clePrivee;
    }
 

    public static PublicKey lectureClePublic(String nomFichier) {
		BigInteger modulo = null;
		BigInteger exposant = null;
		PublicKey clePublique = null;
		try {
		    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(nomFichier)));	    
		    modulo = (BigInteger) ois.readObject();
		    exposant = (BigInteger) ois.readObject();
		    
		    RSAPublicKeySpec specification = new RSAPublicKeySpec(modulo, exposant);
		    KeyFactory usine = KeyFactory.getInstance("RSA");
		    clePublique = usine.generatePublic(specification);
		} catch(Exception e) {
			System.out.println("erreur lecture cle public rsa : "+e);
		}
		return clePublique;
    }
}