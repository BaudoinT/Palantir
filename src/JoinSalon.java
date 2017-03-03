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
		Socket sc = new Socket (serv, 8080);
		//BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(sc.getOutputStream()),true);
		if(mdp!=null)
			out.println(mdp);
		Scanner scan= new Scanner(System.in);
		String message="";
		this.start();
		while(!(message=scan.nextLine()).equals("/quit")){
			out.println(message);
			message="";
			
			/*char buffer[] = new char[250]; 
			int t=in.read(buffer, 0, 250);
			if(t!=-1){
				for(int i=0; i<t; i++){
					message+=buffer[i];
				}
			}

			message="";*/
		}
		
		sc.close();
		System.exit(0);
	}

	public void run (){
		try{
			Socket sc = new Socket (serv, 1111);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));

		

		String message="";
		while(true){
			char buffer[] = new char[250]; 
			int t=in.read(buffer, 0, 250);
			if(t!=-1){
				for(int i=0; i<t; i++){
					message+=buffer[i];
				}
			}
			message="";
		}
		}catch(Exception e){
			System.out.println("erreur run "+e);
		}


	}


}
