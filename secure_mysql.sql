-- ============================================================
-- Script SQL pour sécuriser MySQL/phpMyAdmin
-- À exécuter dans phpMyAdmin ou via la ligne de commande MySQL
-- ============================================================

-- 1. Créer un utilisateur dédié pour l'application (plus sécurisé que root)
-- Remplacez 'votre_mot_de_passe_securise' par un mot de passe fort
CREATE USER IF NOT EXISTS 'dashboard_user'@'localhost' IDENTIFIED BY 'Dashboard2024!Secure';

-- 2. Accorder uniquement les privilèges nécessaires sur la base services_publics
GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP, INDEX, ALTER 
ON services_publics.* 
TO 'dashboard_user'@'localhost';

-- 3. Appliquer les changements
FLUSH PRIVILEGES;

-- 4. Sécuriser le compte root (optionnel mais recommandé)
-- Décommentez les lignes suivantes si vous voulez définir un mot de passe pour root
-- ALTER USER 'root'@'localhost' IDENTIFIED BY 'VotreMotDePasseRootFort123!';
-- FLUSH PRIVILEGES;

-- 5. Supprimer les utilisateurs anonymes (sécurité)
DELETE FROM mysql.user WHERE User='';
FLUSH PRIVILEGES;

-- 6. Supprimer les bases de données de test (sécurité)
DROP DATABASE IF EXISTS test;
DROP DATABASE IF EXISTS information_schema_test;

-- 7. Vérifier les utilisateurs créés
SELECT User, Host FROM mysql.user WHERE User IN ('root', 'dashboard_user');

-- ============================================================
-- INSTRUCTIONS DE SÉCURITÉ POUR phpMyAdmin
-- ============================================================
-- 1. Modifiez le fichier config.inc.php de phpMyAdmin
--    Chemin: C:\xampp\phpMyAdmin\config.inc.php
--    
-- 2. Ajoutez ou modifiez ces lignes:
--    $cfg['Servers'][$i]['auth_type'] = 'cookie';
--    $cfg['Servers'][$i]['AllowNoPassword'] = false;
--    $cfg['Servers'][$i]['host'] = '127.0.0.1';
--    
-- 3. Créez un fichier .htaccess dans C:\xampp\phpMyAdmin\:
--    AuthType Basic
--    AuthName "Accès phpMyAdmin"
--    AuthUserFile C:/xampp/security/.htpasswd
--    Require valid-user
--    
-- 4. Créez un mot de passe avec htpasswd:
--    htpasswd -c C:/xampp/security/.htpasswd admin
-- ============================================================


