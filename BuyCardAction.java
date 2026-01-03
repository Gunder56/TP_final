/**
 * La classe BuyCardAction représente l'action d'acheter une carte de développement.
 * Vérifie les ressources disponibles, effectue le paiement, et ajoute la carte au joueur.
 */

public class BuyCardAction implements Action {

    private int level;   // 0..2
    private int column;  // 0..3

    /**
     * Constructeur de l'action d'achat de carte.
     * @param level Le niveau (tier) de la carte (0-2)
     * @param column La colonne de la carte (0-3)
     */
    public BuyCardAction(int level, int column) {
        this.level = level;
        this.column = column;
    }

    /**
     * Exécute l'action d'achat de carte.
     * Vérifie la disponibilité et les ressources du joueur, puis effectue le paiement.
     * @param player Le joueur qui achète la carte
     * @param board Le plateau de jeu
     */
    @Override
    public void process(Player player, Board board) {
        DevCard card = board.getCard(level, column);

        if (card == null) {
            Game.display.out.println("❌ Pas de carte à cet endroit.");
            return;
        }

        if (!player.canBuyCard(card)) {
            Game.display.out.println("❌ Tu ne peux pas acheter cette carte (ressources insuffisantes).");
            // (Debug utile) montre ce qui manque
            for (Resource r : Resource.values()) {
                int cost = card.getCost().getNbResource(r);
                int bonus = player.getResFromCards(r);
                int need = Math.max(0, cost - bonus);
                int have = player.getNbResource(r);
                if (need > 0) {
                    Game.display.out.println(" - " + r.toSymbol() + " besoin " + need + ", tu as " + have + " (bonus " + bonus + ")");
                }
            }
            return;
        }

        // Paiement
        for (Resource r : Resource.values()) {
            int cost = card.getCost().getNbResource(r);
            int bonus = player.getResFromCards(r);
            int pay = Math.max(0, cost - bonus);

            if (pay > 0) {
                player.updateNbResource(r, -pay);
                board.updateNbResource(r, +pay);
            }
        }

        // Ajout carte (ajoute déjà les points via Player.addPurchasedCard)
        player.addPurchasedCard(card);

    /**
     * Retourne une description textuelle de l'action.
     * @return La description de l'action
     */
        // Remplacer la carte sur le board
        board.updateCard(card);
        
        Game.display.out.println(player.getName() + " achète une carte.");
    }
    
    @Override
    public String toString() {
        return "Achète une carte";
    }
}
