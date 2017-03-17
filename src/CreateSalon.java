import java.io.*;
import java.net.*;
import java.util.*;

public class CreateSalon {
	private String nom;
	private File dir;
	private ArrayList<ClientThread> listeClients=new ArrayList<>();
	private ChiffrementAes chiffAes= new ChiffrementAes();

	public CreateSalon(String nom){
		this.nom=nom;
	}

	public void initialisation() throws IOException {
		//CREATION DU DOSSIER DU SALON
		dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom);
		dir.mkdirs();

		//CREATION DE LA SOCKET SERVEUR
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", 1111);
		ServerSocket ss = new ServerSocket(1111);

		//GENERATION ET SAUVEGARDE DE LA PAIRE DE CLE
		GenerateurCleRsa gene = new GenerateurCleRsa();
		GestionClesRsa ges = new GestionClesRsa();
		gene.generator();
		ges.sauvegardeClePublic(gene.getPublicKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_pub");
		ges.sauvegardeClePrivee(gene.getPrivateKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_priv");

		//LECTURE DES NOUVEAUX UTILISATEURS
		while (true){
			try {
				new ClientThread(ss.accept(),this, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	//Envoi d'un message à tous les utilisateurs sauf celui d'ou il provient (num)
	synchronized public void sendAll(String message, int num, boolean b){
		if(b)
			message=("<"+listeClients.get(num).getPseudo()+"> "+message);
		//affichage du message sur le serveur
		System.out.println(message);
		//pour chaque client:
		OutputStream out;
		for (int i=0; i < listeClients.size(); i++){
			//recuperation de la pipe de sortie
			out = listeClients.get(i).getOut();
			if (out != null && i != num){
				try{
					//chiffrement du message avec la cle symetrique correspondant au client
					chiffAes.setCle(listeClients.get(i).getCleSymetrique());
					chiffAes.setMessage(message.getBytes());
					if(chiffAes.chiffrement())
						//envoi du message au client
						out.write(chiffAes.getMess_chiff());
					out.flush();
				}catch(Exception e){
					System.out.println(e);
				}
			}
		}
	}

	//Méthode permettant d'envoyer la liste des client connecté
	synchronized public void sendList(int num){
		//Récuperation de la pipe de sortie du client demandant la liste
		OutputStream out=listeClients.get(num).getOut();
		//Creation de la liste
		String message="[";
		for(ClientThread c : listeClients)
			message+=""+c.getPseudo()+", ";
		message=message.substring(0,message.length()-2);
		message+="]";
		try{
			//chiffrement du message avec la cle correspondant au client
			chiffAes.setCle(listeClients.get(num).getCleSymetrique());
			chiffAes.setMessage(message.getBytes());
			if(chiffAes.chiffrement())
				//envoi du message
				out.write(chiffAes.getMess_chiff());
			out.flush();
		}catch(Exception e){
			System.out.println(e);
		}
	}

	//Supression d'un client de la liste
	synchronized public void delClient(int i){
		listeClients.remove(i);
	}

	//Ajout d'un client dans la liste
	synchronized public int addClient(ClientThread c){
		listeClients.add(c);
		//envoi d'un message pour prevenir les utilisateurs
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

