package com.municipal.dashboard.exception;

/**
 * Exception métier pour la gestion des erreurs du système
 * Utilisée pour les erreurs spécifiques au domaine métier
 */
public class StockException extends Exception {
    
    private String codeErreur;
    
    public StockException(String message) {
        super(message);
    }
    
    public StockException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public StockException(String codeErreur, String message) {
        super(message);
        this.codeErreur = codeErreur;
    }
    
    public StockException(String codeErreur, String message, Throwable cause) {
        super(message, cause);
        this.codeErreur = codeErreur;
    }
    
    public String getCodeErreur() {
        return codeErreur;
    }
    
    public void setCodeErreur(String codeErreur) {
        this.codeErreur = codeErreur;
    }
    
    @Override
    public String toString() {
        if (codeErreur != null) {
            return String.format("[%s] %s", codeErreur, getMessage());
        }
        return getMessage();
    }
}

