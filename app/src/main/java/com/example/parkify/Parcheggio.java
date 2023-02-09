package com.example.parkify;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Parcheggio implements Serializable{

    private String nomeParcheggio;
    private int idParcheggio;

    private Float latitudine;
    private Float longitudine;

    private boolean free;
    private int numValutazioni;

    private String sMattina, sSera, sNotte;
    private String fsMattina, fsSera, fsNotte;
    private Float ratingSicurezza;
    private List<String> commenti;

    public Parcheggio(){}

    public Parcheggio(String nomeParcheggio, int idParcheggio, Float latitudine, Float longitudine, boolean free, int numValutazioni, String sMattina, String sSera, String sNotte, String fsMattina, String fsSera, String fsNotte, Float ratingSicurezza, List<String> commenti) {
        this.nomeParcheggio = nomeParcheggio;
        this.idParcheggio = idParcheggio;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.free = free;
        this.numValutazioni = numValutazioni;
        this.sMattina = sMattina;
        this.sSera = sSera;
        this.sNotte = sNotte;
        this.fsMattina = fsMattina;
        this.fsSera = fsSera;
        this.fsNotte = fsNotte;
        this.ratingSicurezza = ratingSicurezza;
        this.commenti = commenti;
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

    public Float getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(Float latitudine) {
        this.latitudine = latitudine;
    }

    public Float getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(Float longitudine) {
        this.longitudine = longitudine;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public int getNumValutazioni() {
        return numValutazioni;
    }

    public void setNumValutazioni(int numValutazioni) {
        this.numValutazioni = numValutazioni;
    }

    public String getsMattina() {
        return sMattina;
    }

    public void setsMattina(String sMattina) {
        this.sMattina = sMattina;
    }

    public String getsSera() {
        return sSera;
    }

    public void setsSera(String sSera) {
        this.sSera = sSera;
    }

    public String getsNotte() {
        return sNotte;
    }

    public void setsNotte(String sNotte) {
        this.sNotte = sNotte;
    }

    public String getFsMattina() {
        return fsMattina;
    }

    public void setFsMattina(String fsMattina) {
        this.fsMattina = fsMattina;
    }

    public String getFsSera() {
        return fsSera;
    }

    public void setFsSera(String fsSera) {
        this.fsSera = fsSera;
    }

    public String getFsNotte() {
        return fsNotte;
    }

    public void setFsNotte(String fsNotte) {
        this.fsNotte = fsNotte;
    }

    public Float getRatingSicurezza() {
        return ratingSicurezza;
    }

    public void setRatingSicurezza(Float ratingSicurezza) {
        this.ratingSicurezza = ratingSicurezza;
    }

    public List<String> getCommenti() {
        return commenti;
    }

    public void setCommenti(List<String> commenti) {
        this.commenti = commenti;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parcheggio that = (Parcheggio) o;
        return idParcheggio == that.idParcheggio;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idParcheggio);
    }
}
