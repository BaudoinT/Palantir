import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JoinSalon extends Thread{
	
	private OutputStream out;
	private Socket sc=null;
	private String serv, salon, cle, pseudo;
	private ChiffrementAes chiff, chiffAes;

	public JoinSalon(String serv, String salon, String pseudo) throws UnknownHostException, IOException{
		this.serv=serv;
		this.salon=salon;
		this.pseudo=pseudo;
		chiff = new ChiffrementAes();
		chiff.generationcle();
		cle = chiff.getCle();
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
		
		//ENVOI CLE SYMETRIQUE CHIFFRE
		ChiffrementRsa chif = new ChiffrementRsa("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+salon+"/"+pseudo+"/cle_pub",cle_symetrique.getBytes());
		chif.chiffrement();
		out.write(chif.getMessChiffre());

		this.start();

		//RECPTION DES MESSAGES DU SERVEUR
		while(true){
			message="";
			int t=0;
			byte tmp[]=new byte[256];
			if((t=in.read(tmp, 0, 256))!=-1){
				byte buf[]=new byte[t];
				for (int i = 0; i < t; i++)
					buf[i]=tmp[i];
				chiffAes.setMessageChiffre(buf);
				if(chiffAes.dechiffrement()){
					message=new String(chiffAes.getMess_dechiff());
					System.out.println(message);
				}
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
			chiffAes.setMessage(message.getBytes());
			if(chiffAes.chiffrement()){
				out.write(chiffAes.getMess_chiff());
			}
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
