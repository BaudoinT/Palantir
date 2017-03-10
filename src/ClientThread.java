
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

	public ClientThread (Socket s, CreateSalon salon, String mdp, boolean b){
		receveur=b;
		this.salon=salon;
		sc=s;
		try{
			out=sc.getOutputStream();
			in = sc.getInputStream();
			                                                                                                
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

		}
		catch (IOException e){ }


	}

	public void run(){
		String message = "";
		int t=0;
		try{
			do{
				byte buf[]=new byte[250];
				if((t=in.read(buf, 0, 250))!=-1){
					message=new String(buf).substring(0,t);
					if(!message.substring(0,t).equals("/quit"))
						salon.sendAll("<"+numClient+"> "+message, numClient);
				}
			}while(!message.substring(t).equals("/quit"));
		}catch (Exception e){
		}
		finally{
			try{
				salon.sendAll("Le client "+numClient+" s'est déconnecté", numClient);
				salon.delClient(numClient);
				sc.close();
			}
			catch (IOException e){ }
		}
	}
}
