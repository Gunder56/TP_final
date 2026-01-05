import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Auteur : Marc-Eloi
 * La classe Resources représente un ensemble de ressources dans le jeu Splendor.
 * Elle étend HashMap pour stocker le nombre de chaque type de ressource.
 * Permet de gérer les jetons et les bonus des cartes achetées.
 */
public class Resources extends HashMap<Resource, Integer>{

    /**
     * Constructeur de Resources avec celui de HashMap.
     */
    public Resources() {
        super();
    }

    /**
     * Retourne le nombre de jetons d'une ressource donnée.
     * @param r La ressource recherchée
     * @return Le nombre de jetons de cette ressource (0 si non trouvée)
     */
    public int getNbResource(Resource r){
        return this.getOrDefault(r, 0);
    }

    /**
     * Définit le nombre de jetons pour une ressource donnée.
     * @param r La ressource
     * @param v Le nombre de jetons à définir
    */
    public void setNbResource(Resource r, int v){
        this.put(r, v);
    }
    
    /**
     * Modifie le nombre de jetons pour une ressource donnée.
     * @param r La ressource
     * @param v Le nombre de jetons à ajouter (positif) ou retirer (négatif)
     */
    public void updateNbResource(Resource r, int v){
        int Nb = this.getOrDefault(r, 0);
        Nb += v;
        if(Nb >= 0){
            this.put(r, Nb);
        } else {
            this.put(r, 0);
        }
    }

    /**
     * Retourne l'ensemble des ressources disponibles (avec quantité > 0).
     * @return Un ensemble des ressources possédées
     */
    public Set<Resource> getAvailableResources(){
        Set<Resource> available = new HashSet<>();
        for(Resource r : this.keySet()){
            if(this.get(r) > 0){
                available.add(r);
            }
        }
        return available;
    }
}
