
import java.net.*;
import java.io.*;

public class ClientThread implements Runnable{

	private Thread thread; 
	private Socket sc;
	private OutputStream out;
	private InputStream in;
	private CreateSalon salon;
	private int numClient=0;
	private boolean receveur;
	private String cleSymetique;

	public ClientThread (Socket s, CreateSalon salon, String mdp, boolean b){
		receveur=b;
		this.salon=salon;
		sc=s;
		try{
			out=sc.getOutputStream();
			in = sc.getInputStream();

			//ENVOI CLE PUBLIC
			File source = new File("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon.getNom()+"/cle_pub");
			InputStream sourceFile = new FileInputStream(source);  
			                                                                                                
			try {
				byte buffer[] = new byte[(int)source.length()+1]; 
				int nbLecture;
				while ((nbLecture = sourceFile.read(buffer)) != -1){ 
					out.write(buffer); 
				} 
			} catch (IOException e){ 
				e.printStackTrace(); 
			}
			
			//RECEPTION CLE SYMETRIQUE CHIFFRE
			byte buffCleSymetique[]=new byte[256];
			if((in.read(buffCleSymetique, 0, 256))!=-1){
				DechiffrementRsa dechif = new DechiffrementRsa("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon.getNom()+"/cle_priv",buffCleSymetique);
				dechif.dechiffrement();
				cleSymetique=new String(dechif.getMessDeChiffree());
			}
			
			
			
		                                                                                              
		/*	if(mdp!=null){
				char buffer[] = new char[1];
				String message="";
				while(in.read(buffer, 0, 1)!=-1){
					if (buffer[0] != '\n' && buffer[0] != '\r')
						message += buffer[0];
					else {
						System.out.println(message);
						if(!message.equals(mdp)){
							sc.close();
							System.out.println("Erreur mot de passe");
						}

					}
				}
			}*/
			numClient = this.salon.addClient(out);
			thread = new Thread(this);
			thread.start();

		}catch (IOException e){ }
	}

	public void run(){
		String message = "";
		int t=0;
		ChiffrementAes dechiffMessage = new ChiffrementAes();
		try{
			//RECEPTION PSEUDO
			byte tmp[]=new byte[256];
			if((t=in.read(tmp, 0, 256))!=-1){
				byte buf[]=new byte[t];
				for (int i = 0; i < t; i++)
					buf[i]=tmp[i];
				dechiffMessage.setCle(cleSymetique);
				dechiffMessage.setMessageChiffre(buf);
				if(dechiffMessage.dechiffrement()){
					salon.setPseudo(new String(dechiffMessage.getMess_dechiff()));
				}
			}
			//RECEPTION MESSAGE
			do{
				if((t=in.read(tmp, 0, 256))!=-1){
					byte buf[]=new byte[t];
					for (int i = 0; i < t; i++)
						buf[i]=tmp[i];
					dechiffMessage.setMessageChiffre(buf);
					if(dechiffMessage.dechiffrement()){
						message=new String(dechiffMessage.getMess_dechiff());
					//	if(!message.substring(0,t).equals("/quit"))
							salon.sendAll(message, numClient, true);
					}
				}
			}while(!message.equals("/quit"));
		}catch (Exception e){
		}
		finally{
			try{
				salon.sendAll("Le client "+salon.getPseudo(numClient)+" s'est déconnecté", numClient, false);
				salon.delClient(numClient);
				sc.close();
			}
			catch (IOException e){ }
		}
	}
	
	public String getCleSymetrique(){
		return cleSymetique;
	}
}
