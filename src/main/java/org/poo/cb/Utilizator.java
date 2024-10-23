package org.poo.cb;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;

public class Utilizator implements Iterable<Cont>{
    //folosim Iterator drept Design Pattern pentru a parcurge lista de conturi a fiecarui utilizator
    //furnizează o modalitate de acces la elementele clasei Utilizator fără a expune structura internă a acesteia
    private String email;
    private String nume;
    private String prenume;
    private String adresa;
    private boolean estePremium;
    private ArrayList<Cont> conturi; //fiecare utilizator are o lista de conturi
    private ArrayList<Actiuni> actiuni; //fiecare utilizator are o lista de actiuni
    private ArrayList<Utilizator> prieteni; //lista de prieteni a fiecarui utilizator


    private Utilizator(String email, String prenume, String nume, String adresa){
        //constructorul clasei Utilizator care este private pentru a nu putea fi apelat din afara clasei
        //astfel, nu vom dezvalui structura interna a clasei
        this.email = email;
        this.nume = nume;
        this.prenume = prenume;
        this.adresa = adresa;
        this.estePremium = false;
        conturi = new ArrayList<Cont>();
        actiuni = new ArrayList<Actiuni>();
        prieteni = new ArrayList<Utilizator>();
    }
    //getteri si setteri pentru a putea accesa atributele private ale clasei
    public void adaugaCont(Cont cont){
        conturi.add(cont);
    }
    public void adaugaActiune(Actiuni actiune){
        actiuni.add(actiune);
    }
    public String getEmail(){
        return this.email;
    }
    public String getNume(){
        return this.nume;
    }
    public String getPrenume(){
        return this.prenume;
    }
    public String getAdresa(){
        return this.adresa;
    }
    public ArrayList<Cont> getConturi(){
        return this.conturi;
    }

    public ArrayList<Utilizator> getPrieteni(){
        return this.prieteni;
    }
    public boolean estePremium(){
        return this.estePremium;
    }
    public void setPremium(){
        //setam utilizatorul ca fiind premium
        this.estePremium = true;
    }
    public boolean estePrieten(Utilizator utilizator){
        //verificam daca utilizatorul dat este prieten cu utilizatorul curent
        if(utilizator.getPrieteni().contains(this)){
            return true;
        }
        return false;
    }
    public void adaugaPrieten(Utilizator utilizator){
        //adaugam un utilizar in lista de prieteni a utilizatorului curent
        prieteni.add(utilizator);
    }
    public boolean existaCont(Cont cont){
        //verificam daca mai exista un cont cu aceeasi valuta in lista de conturi a utilizatorului
        for(Cont contCurent : conturi){
            if(contCurent.getTipValuta().equals(cont.getTipValuta())){
                return true;
            }
        }
        return false;
    }
    public String toString() {
        //returnam un String formatat pentru a putea afisa informatiile despre utilizator
        String listaPrieteni = getPrieteni().isEmpty() ? "[]" : "[\"" + getPrieteni().get(0).getEmail() + "\"]";
        return String.format("{\"email\":\"%s\",\"firstname\":\"%s\",\"lastname\":\"%s\",\"address\":\"%s\",\"friends\":%s}",
                this.getEmail(), this.getPrenume(), this.getNume(), this.getAdresa(), listaPrieteni);
    }
    public String portofoliuToString() {
        //returnam un String formatat pentru a putea afisa informatiile despre portofoliul utilizatorului
        String stocks = "";
        //mai intai afisam informatiile despre actiuni
        if (!actiuni.isEmpty()) {
            for (Actiuni actiune : actiuni) {
                stocks += String.format(Locale.US, "{\"stockName\":\"%s\",\"amount\":%d},", actiune.getNumeCompanie(), actiune.getNumar());
            }
            // Eliminam ultima virgula adaugata
            stocks = stocks.substring(0, stocks.length() - 1);
        }
        //afisam informatiile despre conturi
        String accounts = "";
        //folosim un iterator pentru a parcurge lista de conturi, fara sa tinem cont de tipul de cont
        Iterator<Cont> iterator = this.iterator();
        while(iterator.hasNext()) {
            Cont cont = iterator.next();
            accounts += String.format(Locale.US, "{\"currencyName\":\"%s\",\"amount\":\"%.2f\"},", cont.getTipValuta(), cont.getSuma());
        }
        accounts = accounts.substring(0, accounts.length() - 1);
        //afisam informatiile despre prieteni
        String listaPrieteni = prieteni.isEmpty() ? "[]" : "[\"" + prieteni.get(0).getEmail() + "\"]";

        return String.format("{\"stocks\":[%s],\"accounts\":[%s]}", stocks, accounts);
    }
    public Cont gasesteCont(String tipValuta){
        //metoda care gaseste un cont in lista de conturi a utilizatorului in functie de valuta
        //folosim un iterator pentru a parcurge lista de conturi, fara sa tinem cont de tipul de cont
        Iterator<Cont> iterator = this.iterator();
        while(iterator.hasNext()) {
            Cont cont = iterator.next();
            if(cont.getTipValuta().equals(tipValuta)){
                return cont;
            }
        }
        return null;
    }

    public static class UtilizatorBuilder {
        //folosim Builder drept Design Pattern pentru a construi obiecte de tip Utilizator
        private String email;
        private String nume;
        private String prenume;
        private String adresa;
        private boolean estePremium;
        private ArrayList<Cont> conturi;
        private ArrayList<Actiuni> actiuni;
        private ArrayList<Utilizator> prieteni;
        //getteri si setteri pentru a putea accesa atributele private ale clasei
        public UtilizatorBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UtilizatorBuilder setNume(String nume) {
            this.nume = nume;
            return this;
        }

        public UtilizatorBuilder setPrenume(String prenume) {
            this.prenume = prenume;
            return this;
        }

        public UtilizatorBuilder setAdresa(String adresa) {
            this.adresa = adresa;
            return this;
        }
        public UtilizatorBuilder setPremium() {
            this.estePremium = true;
            return this;
        }

        // Metoda pentru construirea efectiva a obiectului Utilizator
        public Utilizator build() {
            Utilizator utilizator = new Utilizator(email, nume, prenume, adresa);
            utilizator.estePremium = false;
            if (conturi != null) {
                utilizator.conturi = new ArrayList<>();
            }
            if (actiuni != null) {
                utilizator.actiuni = new ArrayList<>();
            }
            if (prieteni != null) {
                utilizator.prieteni = new ArrayList<>();
            }
            return utilizator;
        }
    }

    @NotNull
    @Override
    public Iterator<Cont> iterator() {
        //metoda care returneaza un iterator pentru a parcurge lista de conturi a utilizatorului
        return new ContIterator();
    }
    private class ContIterator implements Iterator<Cont> {
        //clasa interna privata care implementeaza interfata Iterator pentru a parcurge lista de conturi a utilizatorului
        private int currentIndex = 0; //parcurgem lista de conturi de la indexul 0

        //metodele specifice interfetei Iterator
        @Override
        public boolean hasNext() {
            return currentIndex < conturi.size();
        }

        @Override
        public Cont next() {
            return conturi.get(currentIndex++);
        }
    }
}
