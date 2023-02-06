package com.example.parkify;

import java.io.Serializable;

public class Valutazione implements Serializable {
    private Person utente;

    private String sMattina, sSera, sNotte;
    private String fsMattina, fsSera, fsNotte;
    private Float ratingSicurezza;
    private String commento;

    public Valutazione() {}

    public Valutazione(String sMattina, String sSera, String sNotte, String fsMattina, String fsSera, String fsNotte, Float ratingSicurezza, String commento) {
        this.sMattina = sMattina;
        this.sSera = sSera;
        this.sNotte = sNotte;
        this.fsMattina = fsMattina;
        this.fsSera = fsSera;
        this.fsNotte = fsNotte;
        this.ratingSicurezza = ratingSicurezza;
        this.commento = commento;
    }

    public Person getUtente() {
        return utente;
    }

    public void setUtente(Person utente) {
        this.utente = utente;
    }

    public String getsMattina() {
        return sMattina;
    }

    public String getsSera() {
        return sSera;
    }

    public String getsNotte() {
        return sNotte;
    }

    public String getFsMattina() {
        return fsMattina;
    }

    public String getFsSera() {
        return fsSera;
    }

    public String getFsNotte() {
        return fsNotte;
    }

    public Float getRatingSicurezza() {
        return ratingSicurezza;
    }

    public String getCommento() {
        return commento;
    }

    public void setsMattina(String sMattina) {
        this.sMattina = sMattina;
    }

    public void setsSera(String sSera) {
        this.sSera = sSera;
    }

    public void setsNotte(String sNotte) {
        this.sNotte = sNotte;
    }

    public void setFsMattina(String fsMattina) {
        this.fsMattina = fsMattina;
    }

    public void setFsSera(String fsSera) {
        this.fsSera = fsSera;
    }

    public void setFsNotte(String fsNotte) {
        this.fsNotte = fsNotte;
    }

    public void setRatingSicurezza(Float ratingSicurezza) {
        this.ratingSicurezza = ratingSicurezza;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }
}
