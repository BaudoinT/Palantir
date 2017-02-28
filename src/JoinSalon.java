import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class JoinSalon {

	private String serv, salon, mdp;
	public JoinSalon(String serv, String salon, String mdp){
		this.serv=serv;
		this.salon=salon;
		this.mdp=mdp;
	}

	public JoinSalon(String serv, String salon) {
		this.serv=serv;
		this.salon=salon;
	}
	
	public void connect() throws UnknownHostException, IOException{
		Socket sc = new Socket (serv, 1111);
		BufferedReader in = new BufferedReader(new InputStreamReader(sc.getInputStream()));
		OutputStream out=sc.getOutputStream();
		if(mdp!=null)
			out.write(mdp.getBytes());
		Scanner scan= new Scanner(System.in);
		String message="";
		char buffer[] = new char[1]; 
		while(!(message=scan.nextLine()).equals("/quit")){
			out.write(message.getBytes());
			message="";
			while(in.read(buffer, 0, 1)!=-1){
				if (buffer[0] != '\n' && buffer[0] != '\r')
					message += buffer[0];
				else {
					System.out.println(message);
				}
			}
			message="";
		}
		
		sc.close();
	}


}
