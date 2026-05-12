# Système de Gestion Restaurant

## Description
Application desktop Java Swing de gestion d'un restaurant
avec architecture MVC + DAO et base de données MySQL.

## Technologies utilisées
- Java (Swing)
- MySQL
- Architecture MVC + DAO

## Prérequis
- Java JDK 8+
- MySQL Server
- Eclipse IDE

## Installation
1. Cloner ou télécharger le projet
2. Importer dans Eclipse
3. Créer la base de données MySQL
4. Configurer conf1.properties
5. Lancer TestVue.java

## Configuration (conf1.properties)
jdbc.url=jdbc:mysql://localhost:3306/restaurant
jdbc.user=root
jdbc.password=ton_mot_de_passe

## Structure du projet
src/
├── modele/       → Classes entités
├── DAOc/         → Couche accès données
├── controlleur/  → Logique métier
├── vue/          → Interfaces Swing
└── util/         → Connexion BD

## Comptes de test
| Utilisateur | Mot de passe | Rôle      |
|-------------|--------------|-----------|
| ahmed       | 1234         | CLIENT    |
| sara        | 1234         | SERVEUSE  |
| karim       | 1234         | CUISINIER |
