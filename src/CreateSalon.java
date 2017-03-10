import java.io.*;
import java.net.*;
import java.util.*;

public class CreateSalon {
	private String nom;
	private String mdp;
	private Vector clients = new Vector();
	private int nbClients=0;
	private FileWriter fw;
	private File dir;

	public CreateSalon(String nom){
		this.nom=nom;
	}

	public CreateSalon(String nom, String mdp){
		this.mdp=mdp;
		this.nom=nom;
	}

	public String getNom(){
		return nom;
	}

	public void initialisation() throws IOException {
		dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom);
		dir.mkdirs();
		//fw = new FileWriter (dir);
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", 1111);
		ServerSocket ss = new ServerSocket(1111);


		GenerateurCleRsa gene = new GenerateurCleRsa();
		GestionClesRsa ges = new GestionClesRsa();
		gene.generator();

		ges.sauvegardeClePublic(gene.getPublicKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_pub");
		ges.sauvegardeClePrivee(gene.getPrivateKey(),"/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/cle_priv");

		while (true){
			try {
				new ClientThread(ss.accept(),this, mdp, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	synchronized public void sendAll(String message, int num){
	/*	try{
			fw.write(message);
		}catch(Exception e){
			System.out.println(e);
		}*/
		System.out.println(message);
		OutputStream out;
		for (int i=0; i < clients.size(); i++){
			out = (OutputStream) clients.elementAt(i);
			if (out != null && i != num){
				try{
					out.write(message.getBytes());
					out.flush();
				}catch(Exception e){
					System.out.println("test: "+e);
				}
			}
		}
	}

	synchronized public void delClient(int i){
		nbClients--;
		if(clients.elementAt(i) != null)
			clients.removeElementAt(i);
		System.out.println(clients.size());
	}

	synchronized public int addClient(OutputStream out){
		nbClients++;
		clients.addElement(out);
		sendAll("Un nouveau client est dans la conversation", clients.size()-1);
		return clients.size()-1;
	}

	synchronized public int getNbClients(){
		return nbClients;
	}

}

