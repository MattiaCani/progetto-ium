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

    private Disp sMattina, sSera, sNotte;
    private Disp fsMattina, fsSera, fsNotte;
    private Float ratingSicurezza;
    private List<String> commenti;

    public Parcheggio(){}

    public Parcheggio(String nomeParcheggio, int idParcheggio, Float latitudine, Float longitudine, boolean free, int numValutazioni, Disp sMattina, Disp sSera, Disp sNotte, Disp fsMattina, Disp fsSera, Disp fsNotte, Float ratingSicurezza, List<String> commenti) {
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

    public Disp getsMattina() {
        return sMattina;
    }

    public void setsMattina(Disp sMattina) {
        this.sMattina = sMattina;
    }

    public Disp getsSera() {
        return sSera;
    }

    public void setsSera(Disp sSera) {
        this.sSera = sSera;
    }

    public Disp getsNotte() {
        return sNotte;
    }

    public void setsNotte(Disp sNotte) {
        this.sNotte = sNotte;
    }

    public Disp getFsMattina() {
        return fsMattina;
    }

    public void setFsMattina(Disp fsMattina) {
        this.fsMattina = fsMattina;
    }

    public Disp getFsSera() {
        return fsSera;
    }

    public void setFsSera(Disp fsSera) {
        this.fsSera = fsSera;
    }

    public Disp getFsNotte() {
        return fsNotte;
    }

    public void setFsNotte(Disp fsNotte) {
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
