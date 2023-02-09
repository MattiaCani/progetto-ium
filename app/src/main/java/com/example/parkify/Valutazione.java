package com.example.parkify;

import java.io.Serializable;

public class Valutazione implements Serializable {
    private Person utente;

    private String nomeParcheggio;
    private int idParcheggio;

    private int sMattina, sSera, sNotte;
    private int fsMattina, fsSera, fsNotte;

    private Float ratingSicurezza;
    private String commento;

    public Valutazione() {}

    public Valutazione(Person utente, String nomeParcheggio, int idParcheggio, int sMattina, int sSera, int sNotte, int fsMattina, int fsSera, int fsNotte, Float ratingSicurezza, String commento) {
        this.utente = utente;
        this.nomeParcheggio = nomeParcheggio;
        this.idParcheggio = idParcheggio;
        this.sMattina = sMattina;
        this.sSera = sSera;
        this.sNotte = sNotte;
        this.fsMattina = fsMattina;
        this.fsSera = fsSera;
        this.fsNotte = fsNotte;
        this.ratingSicurezza = ratingSicurezza;
        this.commento = commento;
    }

    public String getNomeParcheggio() {
        return nomeParcheggio;
    }

    public void setNomeParcheggio(String nomeParcheggio) {
        this.nomeParcheggio = nomeParcheggio;
    }

    public int getIdParcheggio() {
        return idParcheggio;
    }

    public void setIdParcheggio(int idParcheggio) {
        this.idParcheggio = idParcheggio;
    }

    public Person getUtente() {
        return utente;
    }

    public void setUtente(Person utente) {
        this.utente = utente;
    }

    public Float getRatingSicurezza() {
        return ratingSicurezza;
    }

    public String getCommento() {
        return commento;
    }

    public int getsMattina() {
        return sMattina;
    }

    public void setsMattina(int sMattina) {
        this.sMattina = sMattina;
    }

    public int getsSera() {
        return sSera;
    }

    public void setsSera(int sSera) {
        this.sSera = sSera;
    }

    public int getsNotte() {
        return sNotte;
    }

    public void setsNotte(int sNotte) {
        this.sNotte = sNotte;
    }

    public int getFsMattina() {
        return fsMattina;
    }

    public void setFsMattina(int fsMattina) {
        this.fsMattina = fsMattina;
    }

    public int getFsSera() {
        return fsSera;
    }

    public void setFsSera(int fsSera) {
        this.fsSera = fsSera;
    }

    public int getFsNotte() {
        return fsNotte;
    }

    public void setFsNotte(int fsNotte) {
        this.fsNotte = fsNotte;
    }

    public void setRatingSicurezza(Float ratingSicurezza) {
        this.ratingSicurezza = ratingSicurezza;
    }

    public void setCommento(String commento) {
        this.commento = commento;
    }
}
