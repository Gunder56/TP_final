import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Classe représentant un joueur robot avec une stratégie simple dans le jeu Splendor.
 * Ce robot suit une stratégie "gloutonne" : il essaie d'abord d'acheter des cartes,
 * puis de prendre des jetons, en suivant un ordre de priorité fixe.
 */
public class DumbRobotPlayer extends Player {

    /**
     * Constructeur du joueur robot.
     * Initialise un joueur avec le nom "Robot".
     */
    public DumbRobotPlayer() {
        super("Robot");
    }

    /**
     * Retourne une action que le robot doit effectuer en suivant sa stratégie.
     * La stratégie est la suivante (par ordre de priorité):
     * 1) Essaie d'acheter une carte (des tiers 3 à 1)
     * 2) Prend 2 jetons identiques si possible
     * 3) Prend 3 jetons différents si possible
     * 4) Passe son tour
     *
     * @return L'action à effectuer
     */
    @Override
    public Action chooseAction() {
        return new Action(){
            /**
             * Traite l'action choisie par le robot selon sa stratégie.
             *
             * @param player Le joueur robot qui effectue l'action
             * @param board Le plateau de jeu
             */
            @Override
            public void process(Player player, Board board) {
                for(int tier = 2; tier >= 0; tier--){
                    for(int col = 0; col < 4; col++){
                        DevCard card = board.getCard(tier, col);
                        if(card != null && player.canBuyCard(card)){
                            new BuyCardAction(tier, col).process(player, board);
                            return;
                        }
                    }
                }
                
                for(Resource r : Resource.values()){
                    if(board.canGiveSameTokens(r)){
                        new PickSameTokensAction(r).process(player, board);
                        return;
                    }
                }

                Resources picked = new Resources();
                int count = 0;
                for(Resource r : Resource.values()){
                    if(board.getNbResource(r) > 0){
                        picked.setNbResource(r, 1);
                        count++;
                        if(count == 3) break;
                    }
                }
                if(count == 3 && board.canGiveDiffTokens(picked)){
                    new PickDiffTokensAction(picked).process(player, board);
                    return;
                }

                new PassAction().process(player, board);
            }
        };
    }

    /**
     * Le robot choisit aléatoirement les jetons à défausser si le nombre dépasse 10.
     * Il crée une "bourse" contenant tous ses jetons, les mélange, puis sélectionne
     * les premiers jetons à défausser.
     *
     * @return Une liste des ressources à défausser sélectionnées aléatoirement
     */
    @Override
    public List<Resource> chooseDiscardingTokens() {
        int excess = getNbTokens() - 10;
        List<Resource> discard = new ArrayList<>();
        if(excess <= 0) return discard;
        
        List<Resource> bag = new ArrayList<>();
        for(Resource r : Resource.values()){
            for(int i=0; i<getNbResource(r); i++){
                bag.add(r);
            }
        }
        Collections.shuffle(bag);

        for(int i=0; i<excess && i<bag.size(); i++){
            discard.add(bag.get(i));
        }
        return discard;
    }
}
