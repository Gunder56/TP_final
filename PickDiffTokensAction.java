/**
 * Auteur : ghiles seddiki
 * La classe PickDiffTokensAction représente l'action de prendre 3 jetons différents.
 * Le joueur prend 3 jetons de types différents du plateau.
 */

public class PickDiffTokensAction implements Action {

    private Resources pickedResources;

    /**
     * Constructeur de l'action de prendre 3 jetons différents.
     * @param pickedResources Les ressources sélectionnées
     */
    public PickDiffTokensAction(Resources pickedResources) {
        this.pickedResources = pickedResources;
    }

    /**
     * Exécute l'action de prendre 3 jetons différents.
     * @param player Le joueur qui prend les jetons
     * @param board Le plateau de jeu
     */
    @Override
    public void process(Player player, Board board) {
        if (!board.canGiveDiffTokens(pickedResources)) {
            Game.display.out.println("✗ Impossible : jetons insuffisants sur le board.");
            return;
        }

        for (Resource r : pickedResources.getAvailableResources()) {
            int n = pickedResources.getNbResource(r);
            for(int i=0; i<n; i++){
                board.updateNbResource(r, -1);
                player.updateNbResource(r, 1);
            }
        }
        
        Game.display.out.println(player.getName() + " prend 3 jetons différents.");
    }

    @Override
    public String toString() {
        return "Prend 3 jetons différents";
    }
}
