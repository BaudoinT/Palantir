
import java.net.*;
import java.io.*;

public class ClientThread implements Runnable{

	private Thread thread; 
	private Socket sc;
	private PrintWriter out;
	private InputStream in;
	private CreateSalon salon;
	private int numClient=0;

	public ClientThread (Socket s, CreateSalon salon, String mdp){
		this.salon=salon;
		sc=s;
		try{
			out = new PrintWriter(sc.getOutputStream());
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
		try{
			byte buf[]=new byte[250];
			while(in.read(buf, 0, 250)!=-1){
				System.out.println(new String(buf));
			}
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
