/**
 * Énumération représentant les différents types de ressources (jetons) dans le jeu Splendor.
 */
public enum Resource {
    DIAMOND,
    SAPPHIRE,
    EMERALD,
    ONYX,
    RUBY;

    /**
     * Retourne le nom complet français de la ressource avec son symbole.
     * @return Le nom français et le symbole Unicode
     */
    public String toString(){
        switch(this){
            case EMERALD:
                return "EMERAUDE \u2663"; // EMERAUDE ♣
            case DIAMOND:
                return "DIAMANT \u2666"; // DIAMANT ♦
            case SAPPHIRE:
                return "SAPHIR \u2660"; // SAPHIR ♠
            case ONYX:
                return "ONYX \u25CF"; // ONYX ●
            case RUBY:
                return "RUBIS \u2665"; // RUBIS ♥
            default:
                return "";
        }
    }

    /**
     * Retourne le symbole Unicode de la ressource avec sa première lettre.
     * @return Le symbole et la première lettre
     */
    public String toSymbol(){
        switch(this){
            case EMERALD:
                return "\u2663E"; // ♣E
            case DIAMOND:
                return "\u2666D"; // ♦D
            case SAPPHIRE:
                return "\u2660S"; // ♠S
            case ONYX:
                return "\u25CFO"; // ●O
            case RUBY:
                return "\u2665R"; // ♥R
            default:
                return "";
        }
    }
}
