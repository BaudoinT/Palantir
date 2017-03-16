import java.io.*;
import java.net.*;
import java.util.*;

public class CreateSalon {
	private String nom;
	private File dir;
	private ArrayList<ClientThread> listeClients=new ArrayList<>();
	ChiffrementAes chiffAes= new ChiffrementAes();

	public CreateSalon(String nom){
		this.nom=nom;
	}

	public void initialisation() throws IOException {
		//CREATION DU DOSSIER DU SALON
		//dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom);
		dir = new File ("/home/thibault/.palantir/"+nom);
		dir.mkdirs();

		//CREATION DE LA SOCKET SERVEUR
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", 1111);
		ServerSocket ss = new ServerSocket(1111);

		//GENERATION ET SAUVEGARDE DE LA PAIRE DE CLE
		GenerateurCleRsa gene = new GenerateurCleRsa();
		GestionClesRsa ges = new GestionClesRsa();
		gene.generator();
		//ges.sauvegardeClePublic(gene.getPublicKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_pub");
		//ges.sauvegardeClePrivee(gene.getPrivateKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_priv");

		ges.sauvegardeClePublic(gene.getPublicKey(),"/home/thibault/.palantir/"+nom+"/cle_pub");
		ges.sauvegardeClePrivee(gene.getPrivateKey(),"/home/thibault/.palantir/"+nom+"/cle_priv");

		//LECTURE DES NOUVEAUX UTILISATEURS
		while (true){
			try {
				new ClientThread(ss.accept(),this, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	synchronized public void sendAll(String message, int num, boolean b){
		
		if(b)
			message=("<"+listeClients.get(num).getPseudo()+"> "+message);
		System.out.println(message);
		OutputStream out;
		for (int i=0; i < listeClients.size(); i++){
			out = listeClients.get(i).getOut();
			if (out != null && i != num){
				try{
					chiffAes.setCle(listeClients.get(i).getCleSymetrique());
					chiffAes.setMessage(message.getBytes());
					if(chiffAes.chiffrement())
						out.write(chiffAes.getMess_chiff());
					out.flush();
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}

	synchronized public void sendList(int num){
		OutputStream out=listeClients.get(num).getOut();
		String message="[";
		for(ClientThread c : listeClients)
			message+=""+c.getPseudo()+", ";
		message=message.substring(0,message.length()-2);
		message+="]";
		try{
			chiffAes.setCle(listeClients.get(num).getCleSymetrique());
			chiffAes.setMessage(message.getBytes());
			if(chiffAes.chiffrement())
				out.write(chiffAes.getMess_chiff());
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	}

	synchronized public void delClient(int i){
		listeClients.remove(i);
	}

	synchronized public int addClient(ClientThread c){
		listeClients.add(c);
		sendAll("'"+listeClients.get(listeClients.size()-1).getPseudo()+"' a rejoint la conversation", listeClients.size()-1, false);
		return listeClients.size()-1;
	}

	synchronized public int getNbClients(){
		return listeClients.size()-1;
	}

	public String getNom(){
		return nom;
	}

}

