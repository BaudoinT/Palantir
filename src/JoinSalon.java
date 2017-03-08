import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JoinSalon extends Thread{
	
	OutputStream out;
	Socket sc=null;
	private String serv, salon, mdp;
	
	public JoinSalon(String serv, String salon, String mdp) throws UnknownHostException, IOException{
		this.serv=serv;
		this.salon=salon;
		this.mdp=mdp;
	}

	public JoinSalon(String serv, String salon) throws UnknownHostException, IOException {
		this.serv=serv;
		this.salon=salon;
	}
	
	public void connect() throws UnknownHostException, IOException{
		sc = new Socket (serv, 1111);
		BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		out=sc.getOutputStream();

		//if(mdp!=null)
		//	out.println(mdp);
		String message="";
		this.start();
		//while(!(message=scan.nextLine()).equals("/quit")){
		while(true){
			char buffer[] = new char[250];
			int t=in.read(buffer, 0, 250);
			if(t!=-1){
				for(int i=0; i<t; i++){
					message+=buffer[i];
				}

				System.out.println(message);
			}
			message="";
		}
		
		
		//sc.close();
		//System.exit(0);
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
