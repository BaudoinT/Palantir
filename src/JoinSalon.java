import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JoinSalon extends Thread{
	
	OutputStream out;
	Socket sc=null;
	private String serv, salon, mdp;
	ChiffrementAes chiff;
	private String cle;
	private String pseudo;

	public JoinSalon(String serv, String salon, String mdp, String pseudo) throws UnknownHostException, IOException{
		this.serv=serv;
		this.salon=salon;
		this.mdp=mdp;
		this.pseudo=pseudo;
		chiff = new ChiffrementAes();
		chiff.generationcle();
		cle = chiff.getCle();

	}

	public JoinSalon(String serv, String salon, String pseudo) throws UnknownHostException, IOException {
		this(serv,salon,null, pseudo);	
	}
	
	public void connect() throws UnknownHostException, IOException{
		sc = new Socket (serv, 1111);
		InputStream in = sc.getInputStream();
		out=sc.getOutputStream();
		String message="";

		//RECEPTION CLE PUBLIC
	/*	byte buff[]=new byte[250];
		if((in.read(buff, 0, 250))!=-1){
			message=new String(buff);
			System.out.println(message);
		}*/

		//if(mdp!=null)
		//	out.println(mdp);

		this.start();

		//RECPTION DES MESSAGES DU SERVEUR
		while(true){
			message="";
			byte buf[]=new byte[250];
			if((in.read(buf, 0, 250))!=-1){
				message=new String(buf);
				System.out.println(message);

			}
		}
	}

	public void run(){
		try{
			//ENVOI PSEUDO
			out.write(pseudo.getBytes());
			Scanner scan= new Scanner(System.in);
			String message="";
			while(!(message=scan.nextLine()).equals("/quit")){
				out.write(message.getBytes());
				message="";
			}
			out.write(message.getBytes());
			sc.close();
		}catch(Exception e){
			System.out.println(e);
		}finally{
			try{
				System.exit(0);
			}catch(Exception e){
				System.out.println(e);
			}

		}
	}
}
