
public class Main {
	public static void main(String [] args) {
		
		if(args.length!=2 && args.length!=3 && args.length != 4 && args.length!=5){
			System.out.println("Utilisation : Palentir [OPTION] ... [NOM DU SALON] [MOT DE PASSE]");
			System.out.println("Permet de créer ou de rejoindre un chat en ligne.");
			System.out.println("\t-h, --help\t\tAffiche l'aide");
			System.out.println("\t-c, --create\t\tCréer un nouveau salon héberger sur la machine");
			System.out.println("\t-j, --join\t\tRejoindre un salon existant");
		
		}else if(args[0].equals("-c") || args[0].equals("--create")){
			CreateSalon serv= new CreateSalon(args[1]);
			try {
				serv.initialisation();
			} catch (Exception e) {
				System.out.println("Erreur lors de l'initialisation du serveur");
			}
		
		}else if(args[0].equals("-j") || args[0].equals("--join")){
			JoinSalon join;
			
			try {
				join= new JoinSalon(args[1], args[2], args[3]);
			
				join.connect();
			} catch (Exception e) {
				System.out.println("Erreur lors de la connection au serveur");
			}
		}
	}
}
