package com.example.exercice6;

import java.util.List;

public class RechercheVille {

    private final List<String> villes;

    public RechercheVille() {
        this.villes = List.of(
                "Paris",
                "Budapest",
                "Skopje",
                "Rotterdam",
                "Valence",
                "Vancouver",
                "Amsterdam",
                "Vienne",
                "Sydney",
                "New York",
                "Londres",
                "Bangkok",
                "Hong Kong",
                "Dubaï",
                "Rome",
                "Istanbul"
        );
    }

    public List<String> rechercher(String mot) {
        if ("*".equals(mot)) {
            return villes;
        }

        if (mot.length() < 2) {
            throw new NotFoundException("Texte de recherche invalide : moins de 2 caractères");
        }

        String motLower = mot.toLowerCase();
        return villes.stream()
                .filter(ville -> ville.toLowerCase().contains(motLower))
                .toList();
    }
}
