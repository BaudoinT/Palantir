import java.io.*;
import java.net.*;
import java.util.Scanner;

public class JoinSalon extends Thread{

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
		Socket sc = new Socket (serv, 1111);
		BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		OutputStream out=sc.getOutputStream();
		//if(mdp!=null)
		//	out.println(mdp);
		Scanner scan= new Scanner(System.in);
		String message="";
		while(!(message=scan.nextLine()).equals("/quit")){
			out.write(message.getBytes());
			message="";
			
			char buffer[] = new char[250]; 
			int t=in.read(buffer, 0, 250);
			if(t!=-1){
				for(int i=0; i<t; i++){
					message+=buffer[i];
				}
			}

			message="";
		}
		
		sc.close();
		System.exit(0);
	}


}
