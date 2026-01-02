public class BuyCardAction implements Action {

    private int level;
    private int column;

    public BuyCardAction(int level, int column) {
        this.level = level;
        this.column = column;
    }

    @Override
    public void process(Player player, Board board) {
        DevCard card = board.getCard(level, column);
        if (card == null) return;

        if (!player.canBuyCard(card)) return;

        for (Resource r : card.getCost().getAvailableResources()) {
            int cost = card.getCost().getNbResource(r)
                     - player.getResFromCards(r);
            if (cost > 0) {
                player.updateNbResource(r, -cost);
                board.updateNbResource(r, cost);
            }
        }

        player.addPurchasedCard(card);
        player.updatePoints(card.getPoints());
        board.updateCard(card);
    }

    @Override
    public String toString() {
        return "Achète une carte";
    }
}
