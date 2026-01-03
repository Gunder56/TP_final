/**
 * Auteur : ghiles seddiki
 * Représente une action permettant à un joueur de défausser des jetons.
 * Les jetons sont retirés du joueur et rendus au plateau de jeu (board).
 */
public class DiscardTokensAction implements Action {

    private final Resources resourcesToDiscard;

    /**
     * Crée une action de défausse de jetons.
     *
     * @param resourcesToDiscard Les ressources (jetons) à défausser.
     */
    public DiscardTokensAction(Resources resourcesToDiscard) {
        this.resourcesToDiscard = resourcesToDiscard;
    }

    /**
     * Traite l'action de défausse de jetons pour un joueur donné.
     * Les jetons spécifiés sont retirés du joueur et ajoutés au plateau.
     *
     * @param player Le joueur qui défausse les jetons.
     * @param board  Le plateau de jeu où les jetons sont rendus.
     */
    @Override
    
    public void process(Player player, Board board) {
        if (resourcesToDiscard == null) return;

        for (Resource r : resourcesToDiscard.getAvailableResources()) {
            int asked = resourcesToDiscard.getNbResource(r);
            if (asked <= 0) continue;

            int have = player.getNbResource(r);
            int real = Math.min(asked, have); // ✅ on ne peut pas défausser plus que ce qu'on a

            if (real > 0) {
                player.updateNbResource(r, -real);
                board.updateNbResource(r, +real);
            }
        }
    }

    /**
     * Retourne une description textuelle de l'action.
     *
     * @return Description de l'action.
     */
    @Override
    public String toString() {
        return "Défausse des jetons";
    }
}
