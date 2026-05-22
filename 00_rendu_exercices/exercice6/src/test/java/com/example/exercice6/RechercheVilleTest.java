package com.example.exercice6;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Tests de RechercheVille")
class RechercheVilleTest {

    private static final List<String> TOUTES_LES_VILLES = List.of(
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

    private RechercheVille rechercheVille;

    @BeforeEach
    void setUp() {
        rechercheVille = new RechercheVille();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "a"})
    @DisplayName("Lève NotFoundException lorsque le texte de recherche contient moins de 2 caractères")
    void shouldThrowNotFoundExceptionWhenSearchTextHasLessThanTwoCharacters(String searchText) {
        assertThrows(NotFoundException.class, () -> rechercheVille.rechercher(searchText));
    }

    @Test
    @DisplayName("Retourne les villes commençant par le texte de recherche (au moins 2 caractères)")
    void shouldReturnCitiesStartingWithSearchText() {
        List<String> result = rechercheVille.rechercher("Va");

        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    @DisplayName("La recherche est insensible à la casse")
    void shouldBeCaseInsensitive() {
        List<String> result = rechercheVille.rechercher("va");

        assertEquals(List.of("Valence", "Vancouver"), result);
    }

    @Test
    @DisplayName("Retourne une ville lorsque le texte de recherche est contenu dans le nom")
    void shouldReturnCityWhenSearchTextIsSubstringOfName() {
        List<String> result = rechercheVille.rechercher("ape");

        assertEquals(List.of("Budapest"), result);
    }

    @Test
    @DisplayName("Retourne toutes les villes lorsque le texte de recherche est *")
    void shouldReturnAllCitiesWhenSearchTextIsAsterisk() {
        List<String> result = rechercheVille.rechercher("*");

        assertEquals(TOUTES_LES_VILLES, result);
    }
}
