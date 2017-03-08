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


	public void initialisation() throws IOException {
		dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom+"/history");
		dir.mkdirs();
		fw = new FileWriter (dir);
		InetSocketAddress serverAddr = new InetSocketAddress("localhost", 1111);
		ServerSocket ss = new ServerSocket(1111);
	/*	InetSocketAddress serverAddr2 = new InetSocketAddress("localhost", 1112);
		ServerSocket ss2 = new ServerSocket(1112);*/

		while (true){
			try {
				new ClientThread(ss.accept(),this, mdp, true);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

	}

	synchronized public void sendAll(String message, int num){
		try{
			fw.write(message);
		}catch(Exception e){
			System.out.println(e);
		}
		System.out.println(message);
		PrintWriter out;
		for (int i=0; i < clients.size(); i++){
			out = (PrintWriter) clients.elementAt(i);
			if (out != null && i != num){
				out.print(message);
				out.flush();
			}
		}
	}

	synchronized public void delClient(int i){
		nbClients--;
		if(clients.elementAt(i) != null)
			clients.removeElementAt(i);
		System.out.println(clients.size());
	}

	synchronized public int addClient(PrintWriter out){
		nbClients++;
		clients.addElement(out);
		sendAll("Un nouveau client est dans la conversation", clients.size()-1);
		return clients.size()-1;
	}

	synchronized public int getNbClients(){
		return nbClients;
	}

}

