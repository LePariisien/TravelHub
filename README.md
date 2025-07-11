# SupDeVinci Travel Hub

## Introduction

[cite\_start]SupDeVinci Travel Hub (STH) est une plateforme B2C conçue pour agréger des vols, des hébergements et des activités touristiques[cite: 3]. [cite\_start]Son objectif est de construire des itinéraires personnalisés en temps quasi-réel[cite: 3]. [cite\_start]Pour répondre à des exigences de faible latence (200 ms) et de forte charge, l'application adopte une architecture polyglotte en utilisant plusieurs bases de données NoSQL[cite: 4].

## Stack Technique

[cite\_start]Le projet repose sur une architecture de micro-service unique et une pile de bases de données NoSQL adaptée aux différents besoins métier[cite: 5, 7]:

- [cite\_start]**API HTTP/JSON :** Spring Boot, exposant les différentes routes du service[cite: 7].
- [cite\_start]**MongoDB :** Utilisé pour le catalogue d'offres de voyages, gérant des données semi-structurées au format JSON[cite: 5].
- [cite\_start]**Redis :** Employé pour le cache, les sessions et la gestion des notifications en temps réel via un système de pub/sub[cite: 5].
- [cite\_start]**Neo4j :** Une base de données de graphe utilisée pour les recommandations et la modélisation des relations entre les destinations[cite: 5].
- [cite\_start]**Docker & Docker Compose :** Pour la conteneurisation de l'ensemble de la stack, permettant un déploiement facile et reproductible[cite: 67].

## Fonctionnalités

Le micro-service expose plusieurs routes pour les fonctionnalités suivantes :

- [cite\_start]**Authentification (`POST /login`) :** Permet la connexion d'un utilisateur (`userId`) et retourne un token de session UUIDv4 et sa durée de validité (900 secondes)[cite: 25, 26, 27].
- **Recherche d'offres (`GET /offers?from=...&to=...`) :**
  - [cite\_start]Recherche des offres de voyage entre une ville de départ (`from`) et d'arrivée (`to`)[cite: 10].
  - [cite\_start]Utilise Redis pour mettre en cache les résultats (`offers:<from>:<to>`) avec un TTL de 60 secondes pour une latence moyenne de 200 ms[cite: 12, 65].
  - [cite\_start]En cas de cache miss, la requête est effectuée sur MongoDB, avec un tri par prix ascendant, avant d'être mise en cache[cite: 14, 15].
- **Détails d'une offre (`GET /offers/{id}`) :**
  - [cite\_start]Récupère les détails complets d'une offre spécifique à partir de MongoDB[cite: 30].
  - [cite\_start]Met en cache la réponse dans Redis (`offers:<id>`) avec un TTL de 300 secondes[cite: 30].
  - [cite\_start]Inclut une liste de 3 offres liées (`relatedOffers`), obtenues via une requête Neo4j sur les villes proches[cite: 31].
- **Notifications en temps réel :**
  - [cite\_start]Le service publie un message JSON sur le canal Redis Pub/Sub `offers:new` lorsqu'une nouvelle offre est créée[cite: 32, 33, 34].
  - [cite\_start]Cette fonctionnalité peut être testée en s'abonnant au canal avec `redis-cli SUBSCRIBE offers:new`[cite: 35].

## Comment démarrer le projet

### Prérequis

- **Docker**
- **Docker Compose**

### Lancement

1.  Clonez le dépôt :
    ```bash
    git clone <URL_DU_VOTRE_DEPOT>
    cd <NOM_DU_REPERTOIRE>
    ```
2.  Créez un fichier `.env` à la racine du projet si nécessaire, pour y définir les variables d'environnement (par exemple `NEO4J_USERNAME` et `NEO4J_PASSWORD`). [cite\_start]Le fichier `docker-compose.yml` par défaut utilise `neo4j` et `password`[cite: 67].
3.  Démarrez la stack avec Docker Compose :
    ```bash
    docker-compose up
    ```

### Accès aux services

Une fois les conteneurs démarrés :

- **API :** `http://localhost:8080`
- **MongoDB :** `localhost:27017`
- **Redis :** `localhost:6379`
- **Neo4j :**
  - Web UI : `localhost:7474`
  - Driver Bolt : `localhost:7687`
