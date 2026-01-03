/**
 * Auteur : ghiles seddiki
 * La classe PassAction représente l'action de passer son tour.
 * Le joueur ne fait rien et cède son tour au joueur suivant.
 */

public class PassAction implements Action {

    /**
     * Exécute l'action de passer son tour.
     * N'effectue aucune modification de l'état du jeu.
     * @param player Le joueur qui passe
     * @param board Le plateau de jeu
     */
    @Override
    public void process(Player player, Board board) {
        // Ne fait rien : le joueur passe son tour
        Game.display.out.println(player.getName() + " passe son tour.");
    }

    /**
     * Retourne une description textuelle de l'action.
     * @return La description de l'action
     */
    @Override
    public String toString() {
        return "Passer le tour";
    }
}
