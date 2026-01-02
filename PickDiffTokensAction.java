public class PickDiffTokensAction implements Action {

    private Resources pickedResources;

    public PickDiffTokensAction(Resources pickedResources) {
        this.pickedResources = pickedResources;
    }

    @Override
    public void process(Player player, Board board) {
        if (board.canGiveDiffTokens(pickedResources)) {
            for (Resource r : pickedResources.getAvailableResources()) {
                board.updateNbResource(r, -1);
                player.updateNbResource(r, 1);
            }
        }
    }

    @Override
    public String toString() {
        return "Prend 3 jetons de ressources différentes";
    }
}
