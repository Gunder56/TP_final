/*
 * @author    ghiles
 * @version     1.1
 * @since       15/12
 */

import java.util.List;
import java.util.ArrayList;

public class Game {
    /* L'affichage et la lecture d'entrée avec l'interface de jeu se fera entièrement via l'attribut display de la classe Game.
     * Celui-ci est rendu visible à toutes les autres classes par souci de simplicité.
     * L'intéraction avec la classe Display est très similaire à celle que vous auriez avec la classe System :
     *    - affichage de l'état du jeu (méthodes fournies): Game.display.outBoard.println("Nombre de joueurs: 2");
     *    - affichage de messages à l'utilisateur: Game.display.out.println("Bienvenue sur Splendor ! Quel est ton nom?");
     *    - demande d'entrée utilisateur: new Scanner(Game.display.in);
     */
    private static final int ROWS_BOARD=36, ROWS_CONSOLE=8, COLS=82;
    public static final  Display display = new Display(ROWS_BOARD, ROWS_CONSOLE, COLS);
    private int nbre_joueurs;
    private Board board;
    private List<Player> players;

    public static void main(String[] args) {
        //-- à modifier pour permettre plusieurs scénarios de jeu
        display.outBoard.println("Bienvenue sur Splendor !");
        Game game = new Game(2);
        game.play();
        display.close();
    }

    public Game(int nbOfPlayers) throws IllegalArgumentException {
        //verifier si compris entre 2 et 4
        if (nbOfPlayers<2 || nbOfPlayers>4){
            throw new IllegalArgumentException("nombre de joueurs invalides, reesaye bogosse");
        }
        this.nbre_joueurs=nbOfPlayers;   
        board =new Board(nbOfPlayers);
        players = new ArrayList<>();
        players.add(new HumanPlayer());
        int i =1;
        while (i < nbOfPlayers){
            players.add(new DumbRobotPlayer());
            i++;
        }
        
        
    }

    public int getNbPlayers(){
        return nbre_joueurs;
    }

    private void display(int currentPlayer){
        // affiche visuellement le plateau de jeu a l'instant T
        String[] boardDisplay = board.toStringArray();
        String[] playerDisplay = Display.emptyStringArray(0, 0);
        for(int i=0;i<players.size();i++){
            String[] pArr = players.get(i).toStringArray();
            if(i==currentPlayer){
                pArr[0] = "\u27A4 " + pArr[0];
            }
            playerDisplay = Display.concatStringArray(playerDisplay, pArr, true);
            playerDisplay = Display.concatStringArray(playerDisplay, Display.emptyStringArray(1, COLS-54, "\u2509"), true);
        }
        String[] mainDisplay = Display.concatStringArray(boardDisplay, playerDisplay, false);

        display.outBoard.clean();
        display.outBoard.println(String.join("\n", mainDisplay));
    }

    public void play(){
        int currentPlayer = 0;
        while(!isGameOver()){
            display(currentPlayer);
            Player player =players.get(currentPlayer);
            move(player);
            discardToken(player);
            currentPlayer = (currentPlayer+1)%getNbPlayers();
        }
        gameOver();
    }

    private void move(Player player){
    // Le joueur choisit directement l'action à effectuer
    Action action = player.chooseAction();

    if(action != null){
        action.process(player, board);
    } else {
        Game.display.out.println("Le joueur n'a pas choisi d'action valide !");
    }
}

    private void discardToken(Player player){
        // Récupérer les ressources que le joueur doit défausser
        List<Resource> toDiscard = player.chooseDiscardingTokens();
    
        // Retirer ces ressources du joueur
        for(Resource res : toDiscard){
            player.updateNbResource(res, -1);
        }
    }



    public boolean isGameOver(){
        for (Player player : players){
            if (player.getPoints()>=16){
                return true;}
        }
        return false; 
    }

    private void gameOver() {
        Player winner = null;
        int maxPoints = -1;
    
        // Chercher le(s) joueur(s) ayant le plus de points
        for(Player p : players){
            if(p.getPoints() > maxPoints){
                maxPoints = p.getPoints();
                winner = p;
            } else if(p.getPoints() == maxPoints){
                // En cas d'égalité en points, comparer le nombre de cartes achetées
                if(p.getNbPurchasedCards() < winner.getNbPurchasedCards()){
                    winner = p;
                } else if(p.getNbPurchasedCards() == winner.getNbPurchasedCards()){
                    winner = null; // égalité parfaite
                }
            }
        }
    
        if(winner != null){
            display.out.println("Le gagnant est " + winner.getName() + " avec " + maxPoints + " points !");
        } else {
            display.out.println("Partie nulle !");
        }
    }



}
