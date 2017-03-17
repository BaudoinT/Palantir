
import java.net.*;
import java.io.*;

public class ClientThread implements Runnable{

	private Thread thread; 
	private Socket sc;
	private OutputStream out;
	private InputStream in;
	private CreateSalon salon;
	private int numClient=0;
	private String cleSymetique;
	private String pseudo;
	ChiffrementAes dechiffMessage = new ChiffrementAes();

	public ClientThread (Socket s, CreateSalon salon, boolean b){
		this.salon=salon;
		sc=s;
		try{
			
			//CREATION DES TUBES ENTREE/SORTIE
			out=sc.getOutputStream();
			in = sc.getInputStream();

			//ENVOI CLE PUBLIC
			File source = new File("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon.getNom()+"/cle_pub");
			InputStream sourceFile = new FileInputStream(source);  
			                                                                                                
			try {
				byte buffer[] = new byte[(int)source.length()+1];
				while (sourceFile.read(buffer) != -1) 
					out.write(buffer);
			} catch (IOException e){ 
				e.printStackTrace(); 
			}
			sourceFile.close();
			
			//RECEPTION CLE SYMETRIQUE CHIFFRE
			byte buffCleSymetique[]=new byte[256];
			if((in.read(buffCleSymetique, 0, 256))!=-1){
				//dechiffrement de la clé
				DechiffrementRsa dechif = new DechiffrementRsa("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon.getNom()+"/cle_priv",buffCleSymetique);
				dechif.dechiffrement();
				cleSymetique=new String(dechif.getMessDeChiffree());
			}
			
			//RECEPTION PSEUDO UTILISATEUR
			int t=0;
			byte tmp[]=new byte[256];
			if((t=in.read(tmp, 0, 256))!=-1){
				byte buf[]=new byte[t];
				for (int i = 0; i < t; i++)
					buf[i]=tmp[i];
				//dechiffrement du pseudo
				dechiffMessage.setCle(cleSymetique);
				dechiffMessage.setMessageChiffre(buf);
				if(dechiffMessage.dechiffrement())
					this.pseudo=new String(dechiffMessage.getMess_dechiff());
			}

			//AJOUT DU CLIENT AU SERVEUR
			numClient = this.salon.addClient(this);
			
			thread = new Thread(this);
			thread.start();

		}catch (IOException e){ }
	}

	public void run(){
		String message = "";
		int t=0;
		byte tmp[]=new byte[256];
		try{
			//RECEPTION MESSAGE CLIENT
			do{
				if((t=in.read(tmp, 0, 256))!=-1){
					byte buf[]=new byte[t];
					for (int i = 0; i < t; i++)
						buf[i]=tmp[i];
					//dechiffrement du message
					dechiffMessage.setMessageChiffre(buf);
					if(dechiffMessage.dechiffrement()){
						message=new String(dechiffMessage.getMess_dechiff());
						//gestion de la commande '/list'
						if(message.equals("/list"))
							salon.sendList(numClient);
						//si l'utilisateur ne demande pas à quitter le serveur, on envoie le message reçu aux autres utilisateurs
						else if(!message.equals("/quit"))
							salon.sendAll(message, numClient, true);		
					}
				}
			}while(!message.equals("/quit"));
		}catch (Exception e){
			System.out.println(e);
		}finally{
			try{
				//deconnection et suppresion d'un client
				salon.sendAll("Le client "+this.pseudo+" s'est déconnecté", numClient, false);
				salon.delClient(numClient);
				sc.close();
			}
			catch (IOException e){ }
		}
	}
	
	public String getCleSymetrique(){
		return cleSymetique;
	}

	public OutputStream getOut(){
		return out;
	}

	public String getPseudo(){
		return pseudo;
	}
}
