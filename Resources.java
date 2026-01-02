/**
 * Auteur : de BESOMBES Marc-Eloi 
 * La classe Resources modélise un ensemble de ressources dans le jeu.
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Resources extends HashMap<Resource, Integer>{

    /**
     * Constructeur d'objets de classe Resources
     */
    public Resources() {
        super();
    }

    /**
     * Retourne le nombre de ressources disponibles pour un type donné.
     *
     * @param r le type de ressource dont on veut connaître la quantité
     */
    public int getNbResource(Resource r)
    {
        return this.getOrDefault(r, 0);
    }
    
    /**
     * Définit le nombre de ressources disponibles pour un type donné.
     *
     * @param r le type de ressource
     * @param v le nombre de ressources à définir (≥ 0)
     */
    public void setNbResource(Resource r, int v)
    {
        this.put(r, v);
    }
    
    /**
     * Met à jour le nombre de ressources disponibles pour un type donné.
     * Si v > 0, ajoute v à la quantité actuelle.
     * Si v < 0, retire |v| de la quantité actuelle.
     * La quantité finale ne peut jamais être inférieure à 0.
     *
     * @param r le type de ressource
     * @param v la quantité à ajouter (v>0) ou à retirer (v<0)
     */
    public void updateNbResource(Resource r, int v)
    {
        int Nb = this.getOrDefault(r, 0);
        Nb += v;
        if (Nb >= 0)
        {
            this.put(r, Nb);
        }
        else{
            this.put(r, 0);
        }
    }
    
    /**
     * Retourne l'ensemble des ressources pour lesquelles la quantité disponible est strictement positive.
     *
     * @return un Set contenant les types de ressources disponibles
     */
    public Set<Resource> getAvailableResources()
    {
        Set<Resource> available = new HashSet<>();
        for(Resource r : this.keySet()) {
            if(this.get(r) > 0) {
                available.add(r);}
        }
        return available;
    }
}