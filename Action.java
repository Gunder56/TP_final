/**
 * Interface représentant une action que peut effectuer un joueur dans le jeu Splendor.
 * Une action modifie l'état du jeu et peut être l'achat d'une carte, la prise de jetons, ou passer son tour.
 */

public interface Action
{
    /**
     * Exécute l'action sur le joueur et le plateau de jeu.
     * @param player Le joueur qui effectue l'action
     * @param board Le plateau de jeu à modifier
     */
    public void process(Player player, Board board);
    
    /**
     * Retourne une description textuelle de l'action.
     * @return La description de l'action
     */
    public String toString();
}
