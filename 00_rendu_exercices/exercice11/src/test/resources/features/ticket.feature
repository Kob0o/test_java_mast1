Feature: API de gestion des tickets
  Scenarios BDD sur l'API REST des tickets.

  Scenario: Creation valide d un ticket
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Bug login" et la priorite "HIGH"
    Then la reponse HTTP doit etre 201
    And la reponse contient le titre "Bug login"

  Scenario: Resolution d un ticket
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Bug urgent" et la priorite "HIGH"
    And je mets a jour le statut du ticket cree vers "RESOLVED"
    Then la reponse HTTP doit etre 200
    And la reponse contient le statut "RESOLVED"

  Scenario: Refus de modification d un ticket deja resolu
    Given aucun ticket n existe dans l API
    When je cree un ticket avec le titre "Ticket ferme" et la priorite "LOW"
    And je mets a jour le statut du ticket cree vers "RESOLVED"
    And je mets a jour le statut du ticket cree vers "IN_PROGRESS"
    Then la reponse HTTP doit etre 409
    And la reponse contient un message d erreur

  Scenario: Ticket introuvable
    Given aucun ticket n existe dans l API
    When je demande le ticket avec l identifiant 99
    Then la reponse HTTP doit etre 404
    And la reponse contient un message d erreur
