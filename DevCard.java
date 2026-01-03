/**
 * Auteur : Brelier Vincent
 * La classe DevCard représente une carte de développement dans le jeu Splendor.
 * Elle contient le niveau (tier), le type de ressource, les points de victoire, et le coût en ressources.
 */
public class DevCard implements Displayable {
    final int tier;
    final Resource resourceType;
    final int points;
    Resources cost;
    
    /**
     * Constructeur d'une carte de développement avec un objet Resources pour le coût.
     * @param tier le niveau de la carte (1, 2 ou 3)
     * @param resourceType le type de ressource que la carte produit
     * @param nbPoints le nombre de points de victoire de la carte
     * @param resources l'objet Resources contenant le coût de la carte
     */
    public DevCard(int tier, Resource resourceType, int nbPoints, Resources resources){
        this.tier = tier;
        this.resourceType = resourceType;
        this.points = nbPoints;
        this.cost = resources;
        }
        
    /**
     * Constructeur d'une carte de développement avec des valeurs individuelles pour le coût.
     * @param tier le niveau de la carte (1, 2 ou 3)
     * @param resourceType le type de ressource que la carte produit
     * @param nbPoints le nombre de points de victoire de la carte
     * @param nbDIAMOND nombre de diamants requis
     * @param nbSAPPHIRE nombre de saphirs requis
     * @param nbEMERALD nombre d'émeraudes requis
     * @param nbRUBY nombre de rubis requis
     * @param nbONYX nombre d'onyx requis
     */
    public DevCard(int tier, Resource resourceType, int nbPoints, int nbDIAMOND, int nbSAPPHIRE, int nbEMERALD,
                    int nbRUBY, int nbONYX){
        this.tier = tier;
        this.resourceType = resourceType;
        
        this.points = nbPoints;
        this.cost = new Resources();
        this.cost.setNbResource(Resource.DIAMOND, nbDIAMOND);
        this.cost.setNbResource(Resource.SAPPHIRE, nbSAPPHIRE);
        this.cost.setNbResource(Resource.EMERALD, nbEMERALD);
        this.cost.setNbResource(Resource.RUBY, nbRUBY);
        this.cost.setNbResource(Resource.ONYX, nbONYX);
        }
        
    /**
     * Retourne le niveau (tier) de la carte.
     * @return le tier (1, 2 ou 3)
     */
    public int getTier(){
        return tier;
    }
    
    /**
     * Retourne le type de ressource que la carte produit.
     * @return le type de ressource (enum Resource)
     */
    public Resource getResourceType(){
        return resourceType;
    }
    
    /**
     * Retourne le nombre de points de victoire de la carte.
     * @return le nombre de points
     */
    public int getPoints(){
        return points;
    }
    
    /**
     * Retourne l'objet Resources contenant le coût de la carte.
     * @return l'objet Resources du coût
     */
    public Resources getCost(){
        return cost;
    }
    
    /**
     * Retourne la quantité de diamants requise pour acheter la carte.
     * @return le nombre de diamants
     */
    public double getDIAMOND(){
        return cost.getNbResource(Resource.DIAMOND);
    }
    
    /**
     * Retourne la quantité de saphirs requise pour acheter la carte.
     * @return le nombre de saphirs
     */
    public double getSAPPHIRE(){
        return cost.getNbResource(Resource.SAPPHIRE);
    }
    
    /**
     * Retourne la quantité d'émeraudes requise pour acheter la carte.
     * @return le nombre d'émeraudes
     */
    public double getEMERALD(){
        return cost.getNbResource(Resource.EMERALD);
    }
    
    /**
     * Retourne la quantité de rubis requise pour acheter la carte.
     * @return le nombre de rubis
     */
    public double getRUBY(){
        return cost.getNbResource(Resource.RUBY);
    }
    
    /**
     * Retourne la quantité d'onyx requise pour acheter la carte.
     * @return le nombre d'onyx
     */
    public double getONYX(){
        return cost.getNbResource(Resource.ONYX);
    }
        
    /**
     * Retourne une représentation graphique de la carte sous forme de tableau de chaînes.
     * @return un tableau de chaînes représentant la carte
     */
    public String[] toStringArray(){
        /** EXAMPLE
         * ┌────────┐
         * │①    ♠S│
         * │        │
         * │        │
         * │2 ♠S    │
         * │2 ♣E    │
         * │3 ♥R    │
         * └────────┘
         */
        String pointStr = "  ";
        if(getPoints()>0){
            pointStr = new String(new int[] {getPoints()+9311}, 0, 1);
        }
        String[] cardStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510",
                            "\u2502"+pointStr+"    "+resourceType.toSymbol()+"\u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2502        \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518"};
        //update cost of the repr
        int i=6;
        for(Resource res : cost.keySet()){ //-- parcourir l'ensemble des resources (res)en utilisant l'énumération Resource
            if(getCost().getNbResource(res)>0){
                cardStr[i] = "\u2502"+getCost().getNbResource(res)+" "+res.toSymbol()+"    \u2502";
                i--;
            }
        }
        return cardStr;
    }

    /**
     * Retourne une représentation graphique d'une case vide sous forme de tableau de chaînes.
     * @return un tableau de chaînes pour une case sans carte
     */
    public static String[] noCardStringArray(){
        /** EXAMPLE
         * ┌────────┐
         * │ \    / │
         * │  \  /  │
         * │   \/   │
         * │   /\   │
         * │  /  \  │
         * │ /    \ │
         * └────────┘
         */
        String[] cardStr = {"\u250C\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2510",
                            "\u2502 \\    / \u2502",
                            "\u2502  \\  /  \u2502",
                            "\u2502   \\/   \u2502",
                            "\u2502   /\\   \u2502",
                            "\u2502  /  \\  \u2502",
                            "\u2502 /    \\ \u2502",
                            "\u2514\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2500\u2518"};
        
        return cardStr;
    }

    /**
     * Retourne une description textuelle de la carte.
     * @return une chaîne décrivant la carte (points, type, coût)
     */
    public String toString(){
        String cardStr = "";
        cardStr = getPoints()+"pts, type "+resourceType.toSymbol()+" | coût: ";
        for(Resource res : cost.keySet()){ //-- parcourir l'ensemble des resources (res) en utilisant l'énumération Resource
            if(getCost().getNbResource(res)>0){
                cardStr += getCost().getNbResource(res)+res.toSymbol()+" ";
            }
        }
        return cardStr;
    }
}