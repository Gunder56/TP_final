import java.util.List;
import java.util.ArrayList;

/**
 * Auteur : ghiles seddiki
 * 
 * La classe Game gère le déroulement principal du jeu Splendor.
 * Elle orchestre les tours de jeu, les actions des joueurs, et détermine le gagnant.
 */
public class Game {

    private static final int ROWS_BOARD = 36, ROWS_CONSOLE = 8, COLS = 82;
    public static final Display display = new Display(ROWS_BOARD, ROWS_CONSOLE, COLS);

    private int nbre_joueurs;
    private Board board;
    private List<Player> players;

    public static void main(String[] args) {
        display.outBoard.println("Bienvenue sur Splendor !");
        Game game = new Game(2);
        game.play();
        display.close();
    }

    /**
     * Constructeur du jeu Splendor.
     * Initialise les joueurs et le plateau de jeu.
     * @param nbOfPlayers Le nombre de joueurs (doit être entre 2 et 4)
     * @throws IllegalArgumentException si le nombre de joueurs n'est pas valide
     */
    public Game(int nbOfPlayers) throws IllegalArgumentException {
        if (nbOfPlayers < 2 || nbOfPlayers > 4) {
            throw new IllegalArgumentException("Nombre de joueurs invalide (2 à 4).");
        }
        this.nbre_joueurs = nbOfPlayers;

        
        this.board = new Board(nbOfPlayers);

        this.players = new ArrayList<>();
        players.add(new HumanPlayer());
        for (int i = 1; i < nbOfPlayers; i++) {
            players.add(new DumbRobotPlayer());
        }
    }

    /**
     * Retourne le nombre de joueurs dans la partie.
     * @return Le nombre de joueurs
     */
    public int getNbPlayers() {
        return nbre_joueurs;
    }

    /**
     * Affiche l'état actuel du plateau et des joueurs.
     * @param currentPlayer L'index du joueur courant à mettre en évidence
     */
    private void display(int currentPlayer) {
        String[] boardDisplay = board.toStringArray();
        String[] playerDisplay = Display.emptyStringArray(0, 0);

        for (int i = 0; i < players.size(); i++) {
            String[] pArr = players.get(i).toStringArray();
            if (i == currentPlayer) {
                pArr[0] = "\u27A4 " + pArr[0];
            }
            playerDisplay = Display.concatStringArray(playerDisplay, pArr, true);
            playerDisplay = Display.concatStringArray(
                playerDisplay,
                Display.emptyStringArray(1, COLS - 54, "\u2509"),
                true
            );
        }

        String[] mainDisplay = Display.concatStringArray(boardDisplay, playerDisplay, false);

        display.outBoard.clean();
        display.outBoard.println(String.join("\n", mainDisplay));
    }

    /**
     * Lance la boucle principale du jeu.
     * Alterne les tours des joueurs jusqu'à la fin du jeu.
     */
    public void play() {
        int currentPlayer = 0;

        while (!isGameOver()) {
            display(currentPlayer);

            Player player = players.get(currentPlayer);

            move(player);
            discardToken(player); // ne doit agir que si > 10 jetons

            currentPlayer = (currentPlayer + 1) % players.size();
        }

        gameOver();
    }

    /**
     * Traite l'action choisie par le joueur.
     * @param player Le joueur qui effectue l'action
     */
    private void move(Player player) {
        Action action = player.chooseAction();

        if (action == null) {
            Game.display.out.println("Action invalide -> tour passé.");
            return;
        }

        action.process(player, board);
    }

    /**
     * Gère la défausse de jetons si le joueur en a plus de 10.
     * @param player Le joueur qui doit potentiellement défausser
     */
    private void discardToken(Player player) {
        if (player.getNbTokens() <= 10) return;
    
        List<Resource> toDiscardList = player.chooseDiscardingTokens();
    
        Resources toDiscard = new Resources();
        for (Resource r : toDiscardList) {
            toDiscard.updateNbResource(r, 1);
        }
    
        new DiscardTokensAction(toDiscard).process(player, board);
    }


    /**
     * Vérifie si la partie est terminée.
     * @return true si un joueur a plus de 15 points, false sinon
     */
    public boolean isGameOver() {
        // le sujet: vrai si un joueur a PLUS de 15 points :contentReference[oaicite:7]{index=7}
        for (Player player : players) {
            if (player.getPoints() > 15) {
                return true;
            }
        }
        return false;
    }

    /**
     * Détermine le gagnant et affiche le résultat.
     */
    private void gameOver() {
        Player winner = null;
        int maxPoints = -1;

        for (Player p : players) {
            if (p.getPoints() > maxPoints) {
                maxPoints = p.getPoints();
                winner = p;
            } else if (p.getPoints() == maxPoints) {
                // égalité : moins de cartes achetées gagne (tu l'avais déjà)
                if (winner != null && p.getNbPurchasedCards() < winner.getNbPurchasedCards()) {
                    winner = p;
                } else if (winner != null && p.getNbPurchasedCards() == winner.getNbPurchasedCards()) {
                    winner = null; // égalité parfaite
                }
            }
        }

        if (winner != null) {
            display.out.println("Le gagnant est " + winner.getName() + " avec " + maxPoints + " points !");
        } else {
            display.out.println("Partie nulle !");
        }
    }
}
