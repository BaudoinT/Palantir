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
	ChiffrementAes chiffAes;

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
		String cle_symetrique;
		
		File dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon+"/"+pseudo);
		dir.mkdirs();
		File cle_pub = new File("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon+"/"+pseudo+"/cle_pub");
		OutputStream destinationFile = new FileOutputStream(cle_pub); 
		
		//RECEPTION CLE PUBLIC
		int nbr;
		byte buff[]=new byte[1000];
		if((nbr = in.read(buff, 0, 1000))!=-1){
			destinationFile.write(buff, 0, nbr);
		}
		//CREATION CLE SYMETRIQUE
		chiffAes = new ChiffrementAes();
		chiffAes.generationcle();
		cle_symetrique = chiffAes.getCle();
		
		//Envoi CLE SYMETRIQUE CHIFFRE
		ChiffrementRsa chif = new ChiffrementRsa("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon+"/"+pseudo+"/cle_pub",cle_symetrique.getBytes());
		chif.chiffrement();
		out.write(chif.getMessChiffre());

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
			byte buf[]=pseudo.getBytes();
			chiffAes.setMessage(buf);
			if(chiffAes.chiffrement()){
				out.write(chiffAes.getMess_chiff());
			}
			
			//ENVOI MESSAGE
			Scanner scan= new Scanner(System.in);
			String message="";
			while(!(message=scan.nextLine()).equals("/quit")){
				chiffAes.setMessage(message.getBytes());
				if(chiffAes.chiffrement()){
					out.write(chiffAes.getMess_chiff());
				}
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
