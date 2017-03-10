import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JoinSalon extends Thread{
	
	OutputStream out;
	Socket sc=null;
	private String serv, salon, mdp;
	ChiffrementAes chiff;
	private String cle;

	public JoinSalon(String serv, String salon, String mdp) throws UnknownHostException, IOException{
		this.serv=serv;
		this.salon=salon;
		this.mdp=mdp;

		chiff = new ChiffrementAes();
		chiff.generationcle();
		cle = chiff.getCle();

	}

	public JoinSalon(String serv, String salon) throws UnknownHostException, IOException {
		this(serv,salon,null);	
	}
	
	public void connect() throws UnknownHostException, IOException{
		sc = new Socket (serv, 1111);
		InputStream in = sc.getInputStream();
		out=sc.getOutputStream();

		//if(mdp!=null)
		//	out.println(mdp);
		String message="";
		this.start();
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
