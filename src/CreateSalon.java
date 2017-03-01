import java.io.*;
import java.net.*;
import java.util.*;

public class CreateSalon {
	private String nom;
	private String mdp;
	private Vector clients = new Vector();
	private int nbClients=0;

	public CreateSalon(String nom){
		this.nom=nom;
	}

	public CreateSalon(String nom, String mdp){
		this.mdp=mdp;
		this.nom=nom;
	}


	public void initialisation() throws IOException {
		//File dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom);
		File dir = new File ("/home/thibault/.palantir/"+nom);
		dir.mkdirs();
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", 1111);
		ServerSocket ss = new ServerSocket(1111);

		while (true){
			try {
				new ClientThread(ss.accept(),this, mdp);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	synchronized public void sendAll(String message){
		System.out.println(message);
		PrintWriter out;
		for (int i=0; i < clients.size(); i++){
			out = (PrintWriter) clients.elementAt(i);
			if (out != null){
				out.print(message);
				out.flush();
			}
		}
	}

	synchronized public void delClient(int i){
		nbClients--;
		if(clients.elementAt(i) != null)
			clients.removeElementAt(i);
	}

	synchronized public int addClient(PrintWriter out){
		nbClients++;
		clients.addElement(out);
		sendAll("Un nouveau client est dans la conversation");
		return clients.size()-1;
	}

	synchronized public int getNbClients(){
		return nbClients;
	}

}

