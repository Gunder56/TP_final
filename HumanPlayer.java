import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Classe représentant un joueur humain dans le jeu Splendor.
 * Un joueur humain prend ses décisions par interaction avec l'utilisateur via la console.
 */
public class HumanPlayer extends Player {

    /**
     * Constructeur du joueur humain.
     * Initialise un joueur avec le nom "Humain".
     */
    public HumanPlayer() {
        super("Humain");
    }

    /**
    * Traite l'action choisie par le joueur humain.
    * Affiche un menu et demande au joueur de sélectionner une action:
    * 1) Acheter une carte
    * 2) Prendre 2 jetons identiques
    * 3) Prendre 3 jetons différents
    * 4) Passer son tour
    *
    * @param player Le joueur qui effectue l'action
    * @param board Le plateau de jeu
    */
    @Override
    public Action chooseAction() {
        return new Action() {
            @Override
            public void process(Player player, Board board) {

                Scanner sc = new Scanner(Game.display.in);

                while(true){
                    Game.display.out.println("\n=== " + getName() + " : Choisis une action ===");
                    Game.display.out.println("1) Acheter une carte");
                    Game.display.out.println("2) Prendre 2 jetons identiques");
                    Game.display.out.println("3) Prendre 3 jetons différents");
                    Game.display.out.println("4) Passer");

                    int choice = askInt(sc, "Ton choix (1-4) : ", 1, 4);

                    if(choice == 4){
                        new PassAction().process(player, board);
                        return;
                    }

                    if(choice == 1){
                        int tier = askInt(sc, "Tier (1-3) : ", 1, 3);
                        int col  = askInt(sc, "Colonne (1-4) : ", 1, 4);

                        int tierIdx = tier - 1;
                        int colIdx  = col - 1;

                        DevCard card = board.getCard(tierIdx, colIdx);
                        if(card == null){
                            Game.display.out.println("❌ Pas de carte à cet endroit.");
                            continue;
                        }
                        if(!player.canBuyCard(card)){
                            Game.display.out.println("❌ Tu ne peux pas acheter cette carte (ressources insuffisantes).");
                            continue;
                        }

                        new BuyCardAction(tierIdx, colIdx).process(player, board);
                        return;
                    }

                    if(choice == 2){
                        Resource r = askResource(sc, "Choisis la ressource (1-5) : ");
                        if(!board.canGiveSameTokens(r)){
                            Game.display.out.println("❌ Impossible : il faut au moins 4 jetons de ce type sur le board.");
                            continue;
                        }
                        new PickSameTokensAction(r).process(player, board);
                        return;
                    }

                    if(choice == 3){
                        // demander 3 ressources différentes
                        Game.display.out.println("Donne 3 numéros différents séparés par espaces (ex: 1 3 5)");
                        Game.display.out.println(resourceMenuLine());

                        String line = sc.nextLine().trim();
                        String[] parts = line.split("\\s+");
                        if(parts.length != 3){
                            Game.display.out.println("❌ Il faut exactement 3 choix.");
                            continue;
                        }

                        try{
                            int a = Integer.parseInt(parts[0]);
                            int b = Integer.parseInt(parts[1]);
                            int c = Integer.parseInt(parts[2]);

                            if(a<1||a>5||b<1||b>5||c<1||c>5){
                                Game.display.out.println("❌ Choix hors limites (1-5).");
                                continue;
                            }

                            Resource r1 = Resource.values()[a-1];
                            Resource r2 = Resource.values()[b-1];
                            Resource r3 = Resource.values()[c-1];

                            Set<Resource> set = new HashSet<>();
                            set.add(r1); set.add(r2); set.add(r3);
                            if(set.size() != 3){
                                Game.display.out.println("❌ Les 3 ressources doivent être différentes.");
                                continue;
                            }

                            Resources picked = new Resources();
                            picked.setNbResource(r1, 1);
                            picked.setNbResource(r2, 1);
                            picked.setNbResource(r3, 1);

                            if(!board.canGiveDiffTokens(picked)){
                                Game.display.out.println("❌ Impossible : le board n'a pas ces jetons disponibles.");
                                continue;
                            }

                            new PickDiffTokensAction(picked).process(player, board);
                            return;
                        } catch(NumberFormatException e){
                            Game.display.out.println("❌ Entrée invalide (il faut des nombres).");
                            continue;
                        }
                    }
                }
            }
        };
    }
    
    /**
         * Demande au joueur humain de choisir les jetons à défausser s'il en a plus de 10.
         * Affiche les jetons actuels et demande une ressource à la fois jusqu'à ce que
         * le nombre de jetons soit réduit à 10 ou moins.
         *
         * @return Une liste des ressources à défausser
         */
    @Override
    public List<Resource> chooseDiscardingTokens() {
        int excess = getNbTokens() - 10;
        List<Resource> discard = new ArrayList<>();
        if(excess <= 0) return discard;

        Scanner sc = new Scanner(Game.display.in);

        // copie locale des quantités (pour vérifier pendant que user choisit)
        Resources tmp = new Resources();
        for(Resource r : Resource.values()){
            tmp.setNbResource(r, getNbResource(r));
        }

        Game.display.out.println("\n⚠ Tu as trop de jetons (" + getNbTokens() + "). Tu dois en défausser " + excess + ".");

        for(int i=0; i<excess; i++){
            while(true){
                Game.display.out.println("Jetons actuels: " + tokensLine(tmp));
                Resource r = askResource(sc, "Choisis une ressource à défausser (1-5) : ");

                if(tmp.getNbResource(r) <= 0){
                    Game.display.out.println("❌ Tu n'as pas ce jeton.");
                    continue;
                }

                tmp.updateNbResource(r, -1);
                discard.add(r);
                break;
            }
        }

        return discard;
    }

    /* ------------------ Helpers ------------------ */

    /**
     * Demande à l'utilisateur un nombre entier dans une plage spécifiée.
     * Réaffiche le message d'erreur et le prompt tant que l'entrée est invalide.
     *
     * @param sc Le scanner pour lire l'entrée utilisateur
     * @param prompt Le message à afficher pour demander l'entrée
     * @param min La valeur minimale acceptable (incluse)
     * @param max La valeur maximale acceptable (incluse)
     * @return L'entier saisi valide
     */
    private int askInt(Scanner sc, String prompt, int min, int max){
        while(true){
            Game.display.out.println(prompt);
            String line = sc.nextLine().trim();
            try{
                int v = Integer.parseInt(line);
                if(v >= min && v <= max) return v;
            } catch(NumberFormatException ignore){}
            Game.display.out.println("❌ Entrée invalide.");
        }
    }

    /**
    * Demande à l'utilisateur de choisir une ressource (1-5).
    * Réaffiche le menu et le message d'erreur tant que le choix est invalide.
    *
    * @param sc Le scanner pour lire l'entrée utilisateur
    * @param prompt Le message à afficher pour demander l'entrée
    * @return La ressource choisie
    */
    private Resource askResource(Scanner sc, String prompt){
        while(true){
            Game.display.out.println(prompt);
            Game.display.out.println(resourceMenuLine());
            String line = sc.nextLine().trim();
            try{
                int v = Integer.parseInt(line);
                if(v >= 1 && v <= 5){
                    return Resource.values()[v-1];
                }
            } catch(NumberFormatException ignore){}
            Game.display.out.println("❌ Choix invalide.");
        }
    }

    /**
    * Génère une ligne affichant toutes les ressources disponibles avec leurs numéros.
    *
    * @return Une chaîne formatée affichant le menu des ressources
    */
    private String resourceMenuLine(){
        StringBuilder sb = new StringBuilder();
        Resource[] arr = Resource.values();
        for(int i=0; i<arr.length; i++){
            sb.append((i+1)).append(") ").append(arr[i].toSymbol()).append("   ");
        }
        return sb.toString();
    }
    
    /**
    * Génère une ligne affichant le nombre de jetons de chaque ressource.
    *
    * @param r L'objet Resources contenant les quantités de jetons
    * @return Une chaîne formatée affichant les quantités de jetons
    */
    private String tokensLine(Resources r){
        StringBuilder sb = new StringBuilder();
        for(Resource res : Resource.values()){
            sb.append(res.toSymbol()).append(":").append(r.getNbResource(res)).append("  ");
        }
        return sb.toString();
    }
}
