public class PassAction implements Action {

    @Override
    public void process(Player player, Board board) {
        // Le joueur passe son tour : aucune action
    }

    @Override
    public String toString() {
        return "Passe son tour";
    }
}
