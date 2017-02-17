import java.io.File;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class CreateSalon {
	private String nom;
	private String mdp;
	
	public CreateSalon(String nom){
		this.nom=nom;
	}
	
	public CreateSalon(String nom, String mdp){
		this.mdp=mdp;
		this.nom=nom;
	}
	

	public boolean initialisation() {
		File dir = new File ("/home/infoetu/"+System.getProperty("user.name")+"/.palantir/"+nom);
		return dir.mkdirs();
	
	
		MulticastSocket ms=new MulticastSocket(8042);
		InetAddress group= InetAddress.getLocalHost();
		DatagramPacket dataSent = new DatagramPacket("bonjou".getBytes(), "bonjour".length(), group, 8042);
		ms.send(dataSent);
		
		while(true){
			byte[] buf = new byte[1000];
			DatagramPacket r = new DatagramPacket(buf, buf.length);
			ms.receive(r);
			System.out.println(new String(r.getData()));
		}
	}
	
}
