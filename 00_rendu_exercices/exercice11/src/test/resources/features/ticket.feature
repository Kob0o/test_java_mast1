Feature: API de gestion des tickets
  Scenarios BDD sur l'API REST des tickets.

  Scenario: Creation valide d un ticket
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Bug login" et la priorite "HIGH"
    Then la reponse HTTP doit etre 201
    And la reponse contient le titre "Bug login"

  Scenario: Recuperation d un ticket cree
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Ticket a retrouver" et la priorite "LOW"
    And je demande le ticket cree
    Then la reponse HTTP doit etre 200
    And la reponse contient le titre "Ticket a retrouver"

  Scenario: Refus d une creation invalide
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "" et la priorite "LOW"
    Then la reponse HTTP doit etre 400
    And la reponse contient un message d erreur

  Scenario: Ticket introuvable
    Given aucun ticket n existe dans l API
    When je demande le ticket avec l identifiant 99
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur

  Scenario: Mise a jour du statut d un ticket
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Bug API" et la priorite "MEDIUM"
    And je mets a jour le statut du ticket cree vers "IN_PROGRESS"
    Then la reponse HTTP doit etre 200
    And la reponse contient le statut "IN_PROGRESS"
