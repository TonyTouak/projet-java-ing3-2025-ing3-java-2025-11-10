package vue;

import modele.Article;
import java.util.ArrayList;

public interface magasinVue {
    /**
     * Méthode pour afficher une liste d'articles sur l'interface.
     *
     * @param articles La liste des articles à afficher.
     */
    void afficherArticles(ArrayList<Article> articles);

    /**
     * Méthode pour appliquer les filtres disponibles.
     */
    void appliquerFiltres();
}
