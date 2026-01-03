import java.io.File;
import java.io.FileNotFoundException;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Set;
import java.util.Collections;
import java.util.Map;

/**
 * Auteur : Brelier Vincent
 * La classe Board modélise le plateau de jeu dans Splendor.
 * Elle gère les cartes visibles, les piles de cartes cachées, et les ressources disponibles sur le plateau.
 */
public class Board implements Displayable {
    
    protected ArrayList<DevCard> cardList;
    protected Resources resources;
    protected int nbresources;
    protected DevCard[][] visibleCards;
    protected ArrayList<Stack<DevCard>> ListStack;
    
    /**
     * Constructeur du plateau de jeu. Initialise les cartes, les piles mélangées et les ressources.
     */
    public Board(int nbJoueurs){
        // les pile de cartes faces cacher
        cardList = getCardList();
        ListStack = new ArrayList<Stack<DevCard>>();
        Stack<DevCard> stackOne = getStackList(1);
        ListStack.add(stackOne);
        Stack<DevCard> stackTwo = getStackList(2);
        ListStack.add(stackTwo);
        Stack<DevCard> stackTree = getStackList(3);
        ListStack.add(stackTree);
        
        // Initialisation des resources.
        if (nbJoueurs == 2){
            nbresources = 2*nbJoueurs;
        }else{
            nbresources = (2*nbJoueurs)-1;
        }
        
        resources = new Resources();
        resources.setNbResource(Resource.DIAMOND, nbresources);
        resources.setNbResource(Resource.SAPPHIRE, nbresources);
        resources.setNbResource(Resource.EMERALD, nbresources);
        resources.setNbResource(Resource.RUBY, nbresources);
        resources.setNbResource(Resource.ONYX, nbresources);
        
        // tableau à deux dimensions de 3 lignes et 4 colonnes.
        visibleCards = new DevCard[3][4];
        for (int i = 0; i < 3; i++) {  // i = 0 pour tier 1, etc.
            for (int j = 0; j < 4; j++) {
                if (!ListStack.get(i).isEmpty()) {
                    visibleCards[i][j] = ListStack.get(i).pop();
                } else {
                    visibleCards[i][j] = null;
                }
            }
        }
    }
    
    /**
     * Crée et retourne une pile de cartes mélangées pour un niveau donné.
     * @param tier le niveau des cartes (1, 2 ou 3)
     * @return une pile Stack<DevCard> mélangée pour ce niveau
     */
    private Stack<DevCard> getStackList(int tier){
        Stack<DevCard> cardList = new Stack<DevCard>();
        for (DevCard carte : this.cardList){
            if (tier == carte.getTier()){
                cardList.push(carte);
            }
        }
        Collections.shuffle(cardList);
        return cardList;
    } 
    
    /**
     * Lit le fichier CSV et crée une liste de toutes les cartes de développement.
     * Ignore les lignes "NOBLE".
     * @return une ArrayList<DevCard> contenant toutes les cartes
     */
    private ArrayList<DevCard> getCardList(){
        ArrayList<DevCard> cardList = new ArrayList<DevCard>();
        try{
            File file = new File("stats.csv");
            Scanner readableFile = new Scanner(file);
            if (readableFile.hasNextLine()){
                readableFile.nextLine();
            }
            //boucle de fabrication d'une liste de toutes les cartes.
            while (readableFile.hasNextLine()) {
                String ligne = readableFile.nextLine();
                String[] valeurs = ligne.split(",");
                int tier = Integer.parseInt(valeurs[0]);
                int coutDiamond = Integer.parseInt(valeurs[1]);
                int coutSapphire = Integer.parseInt(valeurs[2]);
                int coutEmerald = Integer.parseInt(valeurs[3]);
                int coutRuby = Integer.parseInt(valeurs[4]);
                int coutOnyx = Integer.parseInt(valeurs[5]);
                int points = Integer.parseInt(valeurs[6]);
                String typeStr = valeurs[7];
                
                if ("NOBLE".equals(typeStr)) {
                    continue;
                }
                Resource resourceType = Resource.valueOf(typeStr);
                Resources cost = new Resources();
                cost.setNbResource(Resource.DIAMOND, coutDiamond);
                cost.setNbResource(Resource.SAPPHIRE, coutSapphire);
                cost.setNbResource(Resource.EMERALD, coutEmerald);
                cost.setNbResource(Resource.RUBY, coutRuby);
                cost.setNbResource(Resource.ONYX, coutOnyx);
                
                DevCard carte = new DevCard(tier, resourceType, points, cost);
                cardList.add(carte);
            }
        }
        catch (FileNotFoundException ignore){}
        return cardList;
    }
    
    /**
     * Retourne la quantité d'une ressource disponible sur le plateau.
     * @param r le type de ressource
     * @return le nombre de ressources disponibles
     */
    public int getNbResource(Resource r){
        return resources.getNbResource(r);
    }
    
    /**
     * Définit la quantité d'une ressource sur le plateau.
     * @param r le type de ressource
     * @param v la nouvelle quantité (doit être >= 0)
     */
    public void setNbResource(Resource r, int v)
    {
        resources.setNbResource(r,v);
    }
    
    /**
     * Met à jour la quantité d'une ressource sur le plateau (ajoute ou retire).
     * La quantité ne peut pas descendre en dessous de 0.
     * @param r le type de ressource
     * @param v la quantité à ajouter (positive) ou retirer (négative)
     */
    public void updateNbResource(Resource r, int v){
        resources.updateNbResource(r, v);
    }
    
    /**
     * Retourne l'ensemble des ressources disponibles (quantité > 0) sur le plateau.
     * @return un Set<Resource> des ressources disponibles
     */
    public Set<Resource> getAvailableResources(){
        return resources.getAvailableResources();
    }
    
    /**
     * Retourne la carte visible à une position donnée sur le plateau.
     * @param tierIndex L'indice du niveau (0-2)
     * @param colIndex L'indice de la colonne (0-3)
     * @return La carte à cette position, ou null si invalide
     */
    public DevCard getCard(int tierIndex, int colIndex){
    if (tierIndex < 0 || tierIndex > 2) return null;
    if (colIndex < 0 || colIndex > 3) return null;
    return visibleCards[tierIndex][colIndex]; // [tier][col]
    }
    
    /**
     * Remplace une carte par une nouvelle tirée de la pile correspondante.
     * @param carte La carte à remplacer
     */
    public void updateCard(DevCard carte){
        if (carte == null) return;

        int tierIndex = carte.getTier() - 1; // tier 1..3 -> index 0..2
        if (tierIndex < 0 || tierIndex > 2) return;

        for (int col = 0; col < 4; col++){
            if (visibleCards[tierIndex][col] == carte){
                visibleCards[tierIndex][col] = drawCard(carte.getTier());
                return;
            }
        }
    }
    
    /**
     * Tire une carte de la pile d'un niveau donné.
     * @param tiers le niveau de la carte (1, 2 ou 3)
     * @return la carte tirée, ou null si la pile est vide
     */
    public DevCard drawCard(int tiers){
        if (tiers>0 && tiers<4 && !ListStack.get(tiers-1).isEmpty()){
            return ListStack.get(tiers-1).pop();
        }else{
            return null;
        }
    }
    
    /**
     * Vérifie si on peut donner 2 jetons de la même ressource (nécessite au moins 4 jetons disponibles).
     * @param token le type de ressource
     * @return true si possible, false sinon
     */
    public boolean canGiveSameTokens(Resource token){
        if (resources.getNbResource(token) >= 4){
            return true;
        } else{
            return false;
        }
    }
    
    /**
     * Vérifie si on peut donner 3 jetons de ressources différentes.
     * @param listResources les ressources demandées
     * @return true si toutes les ressources demandées sont disponibles en quantité suffisante, false sinon
     */
    public boolean canGiveDiffTokens(Resources listResources){
        Set<Resource> listeResourceDispo = listResources.getAvailableResources();
        for (Resource resource : listeResourceDispo){
            if (listResources.getNbResource(resource) > resources.getNbResource(resource)){
                return false;
            }
        }
        return true;
    }
    
    /* --- Stringers --- */

    /**
     * Retourne une représentation graphique d'une pile de cartes pour un niveau donné.
     * @param tier le niveau de la pile (1, 2 ou 3)
     * @return un tableau de chaînes représentant la pile
     */
    private String[] deckToStringArray(int tier){
        /** EXAMPLE
         * ┌────────┐
         * │        │╲ 
         * │ reste: │ │
         * │   16   │ │
         * │ cartes │ │
         * │ tier 3 │ │
         * │        │ │
         * └────────┘ │
         *  ╲________╲│
         */
        int nbCards = ListStack.get(tier-1).size();
        String[] deckStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510  ",
                            "\u2502        \u2502\u2572 ",
                            "\u2502 reste: \u2502 \u2502",
                            "\u2502   "+String.format("%02d", nbCards)+"   \u2502 \u2502",
                            "\u2502 carte"+(nbCards>1 ? "s" : " ")+" \u2502 \u2502",
                            "\u2502 tier "+tier+" \u2502 \u2502",
                            "\u2502        \u2502 \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518 \u2502",
                            " \u2572________\u2572\u2502"};
        return deckStr;
    }

    /**
     * Retourne une représentation textuelle des ressources disponibles sur le plateau.
     * @return un tableau de chaînes listant les ressources
     */
    private String[] resourcesToStringArray(){
        /** EXAMPLE
         * Resources disponibles : 4♥R 4♣E 4♠S 4♦D 4●O
         */
        String[] resStr = {"Resources disponibles : "};
        
        for(Resource res : resources.keySet()){
            resStr[0] += resources.getNbResource(res)+res.toSymbol()+" ";
        }
        resStr[0] += "        ";
        return resStr;
    }

    /**
     * Retourne une représentation complète du plateau de jeu.
     * @return un tableau de chaînes représentant le board entier
     */
    private String[] boardToStringArray(){
        String[] res = Display.emptyStringArray(0, 0);
        //Deck display
        String[] deckDisplay = Display.emptyStringArray(0, 0);
        for(int i=ListStack.size();i>0;i--){
            deckDisplay = Display.concatStringArray(deckDisplay, deckToStringArray(i), true);
        }

        //Card display
        String[] cardDisplay = Display.emptyStringArray(0, 0);
        for(int i = 2; i>=0; i--){ //-- parcourir les différents niveaux de carte (i)
            String[] tierCardsDisplay = Display.emptyStringArray(8, 0);
            for(int j = 0; j<4;j++){ //-- parcourir les 4 cartes faces visibles pour un niveau donné (j)
                tierCardsDisplay = Display.concatStringArray(tierCardsDisplay, visibleCards[i][j]!=null ? visibleCards[i][j].toStringArray() : DevCard.noCardStringArray(), false);
            }
            cardDisplay = Display.concatStringArray(cardDisplay, Display.emptyStringArray(1, 40), true);
            cardDisplay = Display.concatStringArray(cardDisplay, tierCardsDisplay, true);
        }
        
        res = Display.concatStringArray(deckDisplay, cardDisplay, false);
        res = Display.concatStringArray(res, Display.emptyStringArray(1, 52), true);
        res = Display.concatStringArray(res, resourcesToStringArray(), true);
        res = Display.concatStringArray(res, Display.emptyStringArray(35, 1, " \u250A"), false);
        res = Display.concatStringArray(res, Display.emptyStringArray(1, 54, "\u2509"), true);
        
        return res;
    }

    /**
     * Retourne la représentation graphique du plateau.
     * @return un tableau de chaînes pour l'affichage
     */
    @Override
    public String[] toStringArray() {
        return boardToStringArray();
    }
}