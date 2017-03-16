
public class Main {
	public static void main(String [] args) {
		
		if(args.length==2 || args.length == 4){
			if(args[0].equals("-c") || args[0].equals("--create")){
				try {
					CreateSalon serv= new CreateSalon(args[1]);
					serv.initialisation();
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Erreur lors de l'initialisation du serveur");
					Main.afficherAide();
				}
			}else if(args[0].equals("-j") || args[0].equals("--join")){
				try {
					JoinSalon join= new JoinSalon(args[1], args[2], args[3]);
					join.connect();
					System.exit(0);
				} catch (Exception e) {
					System.out.println("Erreur lors de la connection au serveur");
					Main.afficherAide();
				}
			}
		}else{
			Main.afficherAide();
		}
	}

	public static void afficherAide(){
		System.out.println("Utilisation : Palentir [-h, -c , -j] [IP DU SERVEUR] NOM DU SALON");
		System.out.println("Permet de créer ou de rejoindre un chat en ligne.");
		System.out.println("\t-h, --help\t\tAffiche l'aide");
		System.out.println("\t-c, --create\t\tCréer un nouveau salon héberger sur la machine");
		System.out.println("\t-j, --join\t\tRejoindre un salon existant");
	}
}
