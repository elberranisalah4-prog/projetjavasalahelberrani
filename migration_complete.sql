-- ============================================================
-- SCRIPT DE MIGRATION COMPLET
-- Crée la base de données et migre toutes les tables
-- Compatible MySQL/phpMyAdmin avec charset UTF-8
-- PROJET: Dashboard Services Publics - Gestion Municipale
-- ============================================================

-- Créer la base de données si elle n'existe pas
CREATE DATABASE IF NOT EXISTS services_publics 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- Utiliser la base de données
USE services_publics;

-- ============================================================
-- ÉTAPE 1 : SUPPRIMER LES TABLES SPORTS INUTILES
-- ============================================================
DROP TABLE IF EXISTS accommodations;
DROP TABLE IF EXISTS transports;
DROP TABLE IF EXISTS matches;
DROP TABLE IF EXISTS stadiums;
DROP TABLE IF EXISTS teams;

SELECT '✓ Tables Sports supprimées' AS Étape;

-- ============================================================
-- ÉTAPE 2 : CRÉER LES TABLES NÉCESSAIRES
-- ============================================================

-- ============================================================
-- TABLE: admin
-- Administrateurs du système
-- ============================================================
CREATE TABLE IF NOT EXISTS admin (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    telephone VARCHAR(20),
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    statut VARCHAR(20) DEFAULT 'ACTIF',
    derniere_connexion DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: personnel
-- Personnel municipal
-- ============================================================
CREATE TABLE IF NOT EXISTS personnel (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telephone VARCHAR(20),
    poste VARCHAR(100),
    departement VARCHAR(100),
    date_embauche DATETIME,
    statut VARCHAR(20) DEFAULT 'ACTIF',
    matricule VARCHAR(50) UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: poste
-- Postes de travail disponibles
-- ============================================================
CREATE TABLE IF NOT EXISTS poste (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    description TEXT,
    departement VARCHAR(100),
    niveau VARCHAR(50),
    salaire_min DOUBLE,
    salaire_max DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: consommation
-- Données de consommation de services
-- ============================================================
CREATE TABLE IF NOT EXISTS consommation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    valeur DOUBLE NOT NULL,
    date DATETIME NOT NULL,
    service_type VARCHAR(50),
    zone VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: production
-- Données de production de services
-- ============================================================
CREATE TABLE IF NOT EXISTS production (
    id INT PRIMARY KEY AUTO_INCREMENT,
    valeur DOUBLE NOT NULL,
    date DATETIME NOT NULL,
    service_type VARCHAR(50),
    zone VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: indicator
-- Indicateurs de performance
-- ============================================================
CREATE TABLE IF NOT EXISTS indicator (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    valeur DOUBLE,
    valeur_cible DOUBLE,
    date_calcul DATETIME,
    unite VARCHAR(20),
    description TEXT,
    statut VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: prediction
-- Prédictions et analyses
-- ============================================================
CREATE TABLE IF NOT EXISTS prediction (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_type VARCHAR(50),
    date_prediction DATETIME,
    valeur_predite DOUBLE,
    confiance DOUBLE,
    zone VARCHAR(100),
    type_prediction VARCHAR(50),
    recommandation TEXT,
    date_calcul DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: decision
-- Décisions prises par la municipalité
-- ============================================================
CREATE TABLE IF NOT EXISTS decision (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titre VARCHAR(200) NOT NULL,
    description TEXT,
    type VARCHAR(50),
    statut VARCHAR(50),
    date_decision DATETIME,
    date_application DATETIME,
    auteur VARCHAR(100),
    service_concerne VARCHAR(100),
    zone_concernee VARCHAR(100),
    justification TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: note
-- Notes et annotations
-- ============================================================
CREATE TABLE IF NOT EXISTS note (
    id INT PRIMARY KEY AUTO_INCREMENT,
    contenu TEXT NOT NULL,
    date_creation DATETIME DEFAULT CURRENT_TIMESTAMP,
    auteur VARCHAR(100),
    type VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: service
-- Types de services municipaux
-- ============================================================
CREATE TABLE IF NOT EXISTS service (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(50) NOT NULL,
    nom VARCHAR(100) NOT NULL,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: zone
-- Zones géographiques
-- ============================================================
CREATE TABLE IF NOT EXISTS zone (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    code VARCHAR(20) UNIQUE,
    description TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: service_data
-- Données historiques de services (utilisé par Database.java)
-- ============================================================
CREATE TABLE IF NOT EXISTS service_data (
    id INT PRIMARY KEY AUTO_INCREMENT,
    service_type VARCHAR(50) NOT NULL,
    date DATETIME NOT NULL,
    consommation DOUBLE,
    production DOUBLE,
    cout DOUBLE,
    zone VARCHAR(100),
    notes TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- FIN DE LA MIGRATION
-- ============================================================

-- Afficher toutes les tables créées
SHOW TABLES;

-- Message de confirmation
SELECT 
    '✓ Migration terminée avec succès!' AS Message,
    COUNT(*) AS 'Nombre de tables',
    DATABASE() AS 'Base de données'
FROM information_schema.tables 
WHERE table_schema = 'services_publics';

