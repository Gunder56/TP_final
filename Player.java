import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Classe abstraite représentant un joueur dans le jeu Splendor.
 * Un joueur dispose de ressources, de cartes achetées et accumule des points.
 * Cette classe fournit les fonctionnalités communes à tous les types de joueurs.
 */
public abstract class Player implements Displayable {

    protected static int NEXT_ID = 0;

    protected int id;
    protected String name;
    protected int points;

    protected Resources resources;
    protected ArrayList<DevCard> purchasedCards;

    /**
     * Constructeur du joueur.
     * @param name Le nom du joueur
     */
    protected Player(String name){
        this.id = NEXT_ID++;
        this.name = name;
        this.points = 0;
        this.resources = new Resources();
        this.purchasedCards = new ArrayList<>();
    }

    /* ---------- GETTERS ---------- */

    /**
     * Retourne le nom du joueur.
     * @return Le nom du joueur
     */
    public String getName(){
        return name;
    }

    /**
     * Retourne le nombre de points du joueur.
     * @return Le nombre de points
     */
    public int getPoints(){
        return points;
    }

    /**
     * Retourne le nombre de cartes achetées par le joueur.
     * @return Le nombre de cartes achetées
     */
    public int getNbPurchasedCards(){
        return purchasedCards.size();
    }

    /**
     * Retourne le nombre de jetons d'une ressource donnée que possède le joueur.
     * @param r La ressource
     * @return Le nombre de jetons de cette ressource
     */
    public int getNbResource(Resource r){
        return resources.getNbResource(r);
    }

    /**
     * Retourne l'ensemble des ressources disponibles pour le joueur.
     * @return Un ensemble des ressources possédées
     */
    public Set<Resource> getAvailableResources(){
        return resources.getAvailableResources();
    }

    /**
     * Retourne le nombre total de jetons que possède le joueur.
     * @return Le nombre total de jetons
     */
    public int getNbTokens(){
        int total = 0;
        for(Resource r : Resource.values()){
            total += resources.getNbResource(r);
        }
        return total;
    }

    /* ---------- CARTES ---------- */

    /**
     * Retourne le bonus de ressources obtenu grâce aux cartes achetées.
     * @param r La ressource recherchée
     * @return Le nombre de bonus pour cette ressource
     */
    public int getResFromCards(Resource r){
        int bonus = 0;
        for(DevCard card : purchasedCards){
            if(card.getResourceType() == r){
                bonus++;
            }
        }
        return bonus;
    }

    /**
     * Ajoute une carte achetée à la collection du joueur.
     * @param card La carte à ajouter
     */
    public void addPurchasedCard(DevCard card){
        purchasedCards.add(card);
        points += card.getPoints();
    }


    /* ---------- RESSOURCES ---------- */

    /**
     * Modifie le nombre de jetons d'une ressource donnée.
     * @param r La ressource
     * @param v Le nombre de jetons à ajouter (positif) ou retirer (négatif)
     */
    public void updateNbResource(Resource r, int v){
        resources.updateNbResource(r, v);
    }

    /**
     * Vérifie si le joueur peut acheter une carte donnée avec ses ressources actuelles.
     * @param card La carte à vérifier
     * @return true si le joueur peut acheter la carte, false sinon
     */
    public boolean canBuyCard(DevCard card){
        for(Resource r : Resource.values()){
            int cost = card.getCost().getNbResource(r);
            int bonus = getResFromCards(r);
            int needed = cost - bonus;
            if(needed > 0 && getNbResource(r) < needed){
                return false;
            }
        }
        return true;
    }

    /* ---------- AFFICHAGE ---------- */

    /**
     * Retourne une représentation textuelle du joueur pour l'affichage.
     * @return Un tableau de chaînes représentant l'état du joueur
     */
    @Override
    public String[] toStringArray(){
        String pointStr;
        String[] strPlayer = new String[8];

        if(points > 0){
            pointStr = new String(new int[] {points + 9311}, 0, 1);
        } else {
            pointStr = "\u24EA"; 
        }

        strPlayer[0] = "Player " + (id + 1) + ": " + name;
        strPlayer[1] = pointStr + "pts";
        strPlayer[2] = "";

        for(Resource res : Resource.values()){
            strPlayer[3 + (Resource.values().length - 1 - res.ordinal())] =
                res.toSymbol() + " (" + resources.getNbResource(res) + ") [" + getResFromCards(res) + "]";
        }

        return strPlayer;
    }

    /* ---------- MÉTHODES ABSTRAITES ---------- */

    /**
     * Permet au joueur de choisir une action à effectuer.
     * @return L'action choisie par le joueur
     */
    public abstract Action chooseAction();

    /**
     * Permet au joueur de choisir les jetons à défausser si le nombre dépasse 10.
     * @return Une liste des ressources à défausser
     */
    public abstract List<Resource> chooseDiscardingTokens();
}
