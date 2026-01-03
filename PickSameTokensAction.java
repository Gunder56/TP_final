/**
 * Auteur : ghiles seddiki
 * La classe PickSameTokensAction représente l'action de prendre 2 jetons identiques.
 * Le joueur choisit un type de ressource et en prend 2 du plateau.
 */

public class PickSameTokensAction implements Action {

    private Resource resource;

    /**
     * Constructeur de l'action de prendre 2 jetons identiques.
     * @param resource Le type de ressource choisi
     */
    public PickSameTokensAction(Resource resource) {
        this.resource = resource;
    }

    /**
     * Exécute l'action de prendre 2 jetons identiques.
     * @param player Le joueur qui prend les jetons
     * @param board Le plateau de jeu
     */
    @Override
    public void process(Player player, Board board) {
        if (!board.canGiveSameTokens(resource)) {
            Game.display.out.println("✗ Impossible : il faut au moins 4 jetons de ce type sur le board.");
            return;
        }

        board.updateNbResource(resource, -2);
        player.updateNbResource(resource, 2);
        
        Game.display.out.println(player.getName() + " prend 2 jetons de " + resource.toString());
    }

    @Override
    public String toString() {
        return "Prend 2 jetons de " + resource;
    }
}
