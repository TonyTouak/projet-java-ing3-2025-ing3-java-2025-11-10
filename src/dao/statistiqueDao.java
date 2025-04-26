package dao;

import modele.Article;

import java.util.Map;

public interface statistiqueDao {
    int getNombreTotalCommandes();

    double getMontantTotalVentes();

    Map<Article, Integer> getQuantitesVenduesParArticle();
}
