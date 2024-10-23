package org.poo.cb;

//import com.fasterxml.jackson.databind.deser.std.CollectionDeserializer;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Main {

    public static int schimbaValutaInInt(String valuta) {
        //pentru a face mai usoaara citirea fisierului de exchange rates,
        // am asociat un tip de valuta cu indexul liniei/ coloanei pe care o vom accesa
        return switch (valuta) {
            case "EUR" -> 0; //ex: Euro se afla pe linia 0 si coloana 0
            case "GBP" -> 1;
            case "JPY" -> 2;
            case "CAD" -> 3;
            default -> 4;
        };
    }
    public static double[][] citesteExchangeRates(String fileName) {
        //citim din fisierul de exchange rates
        double[][] rateSchimbValutar = null;

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            //prima linie contine tipurile de valuta
            //la fel si prima coloana
            boolean primaLinie = true;
            int linie = 0;

            while ((line = br.readLine()) != null) {
                String[] parti = line.split(",");
                if (primaLinie) {
                    //initializam matricea de exchange rates
                    rateSchimbValutar = new double[parti.length - 1][parti.length - 1];
                    primaLinie = false;
                } else {
                    for (int col = 1; col < parti.length; col++) {
                        //citim valorile din fisier si le adaugam in matrice
                        rateSchimbValutar[linie][col - 1] = Double.parseDouble(parti[col]);
                    }
                    linie++;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        //la final, returnam matricea de exchange rates
        return rateSchimbValutar;
    }
    public static void citesteStockValues(String numeFisier, EbankingApp aplicatie) throws IOException {

        //metoda pentru a citi si procesa valorile din fisierul de stock values
        //voi crea o actiune pentru fiecare linie din fisier, linie care contine numele companiei si valorile stocului
        try (BufferedReader br = new BufferedReader(new FileReader(numeFisier))) {
            String line;
            boolean primaLinie = true;

            while ((line = br.readLine()) != null) {
                //initializam lista de valori pentru fiecare stoc
                ArrayList<Double> stockValues = new ArrayList<Double>();
                String[] parti = line.split(",");
                if (primaLinie) {
                    primaLinie = false;
                } else {
                    //primul element din linie este numele stocului
                    String numeStock = parti[0];
                    for (int i = 1; i < parti.length; i++) {
                        //restul elementelor sunt valorile stocului
                        stockValues.add(Double.parseDouble(parti[i]));
                    }
                    //cream actiunea si ii setam valorile
                    Actiuni actiune = new Actiuni(numeStock);
                    actiune.setValoriInUltimele10Zile(stockValues);
                    //adaugam actiunea in lista de stocuri, in caz ca nu este adaugata deja
                    if(!EbankingApp.listaStocks.contains(actiune)) {
                        aplicatie.adaugaInListaStocks(actiune);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void procesareComanda(String line, double[][] exchangeRates, String stockValuesFile,
                                        EbankingApp aplicatie) throws IOException {
        //metoda pentru a procesa fiecare comanda din fisierele de comenzi

        String[] comanda = line.split(" ");
        //segementam fiecare linie citita dupa spatiu
        if(comanda[0].equals("CREATE") && comanda[1].equals("USER")) {
            //daca comanda incepe cu "CREATE USER" atunci vom crea un nou utilizator
            String email = comanda[2];
            String prenume = comanda[3];
            String nume = comanda[4];
            String adresa = comanda[5];
            for(int i = 6; i < comanda.length; i++) {
                adresa = adresa + " " + comanda[i];
            }
            adresa.trim();
            //folosim Builder pentru a crea un utilizator
            Utilizator utilizator = new Utilizator.UtilizatorBuilder()
                    .setEmail(email)
                    .setNume(prenume)
                    .setPrenume(nume)
                    .setAdresa(adresa)
                    .build();
            if(aplicatie.existaUtilizator(email) != null) {
                //daca utilizatorul exista deja, afisam un mesaj de eroare
                System.out.println("User with " + email + " already exists");
            }else {
                aplicatie.adaugaUtilizator(utilizator); //adaugam utilizatorul in lista de utilizatori
            }
        }else if(comanda[0].equals("LIST") && comanda[1].equals("USER")) {
            //daca comanda incepe cu "LIST USER" atunci vom afisa informatiile despre utilizator
            String email = comanda[2];
            if (aplicatie.existaUtilizator(email) == null) {
                //daca utilizatorul nu exista, afisam un mesaj de eroare
                System.out.println("User with " + email + " doesn't exist");
            } else {
                //altfel, afisam informatiile despre utilizator
                Utilizator utilizator = aplicatie.existaUtilizator(email);
                System.out.println(utilizator.toString());
            }
        } else if(comanda[0].equals("ADD") && comanda[1].equals("FRIEND")) {
            //daca comanda incepe cu "ADD FRIEND" atunci vom adauga un prieten cu un email dat
            String emailUtilizator = comanda[2];
            String emailPrieten = comanda[3];
            //Daca utilizatorul sau utilizatorul ce poate fi adaugat la prieteni nu exista
            // se va afisa un mesaj de eroare
            if(aplicatie.existaUtilizator(emailUtilizator) == null) {
                System.out.println("User with email " +  emailUtilizator + " doesn't exist");
            } else if(aplicatie.existaUtilizator(emailPrieten) == null) {
                System.out.println("User with email " +  emailPrieten+ " doesn't exist");
            } else if(aplicatie.existaUtilizator(emailPrieten).
                    estePrieten(aplicatie.existaUtilizator(emailUtilizator))) {
                //verificam daca utilizatorul cu emailul dat are deja prietenul cu emailul dat
                System.out.println("User with email " +  emailPrieten+ " is already a friend");
            } else {
                //fiecare utilizator va fi adaugat in lista de prieteni a celuilalt
                aplicatie.existaUtilizator(emailUtilizator).adaugaPrieten(aplicatie.existaUtilizator(emailPrieten));
                aplicatie.existaUtilizator(emailPrieten).adaugaPrieten(aplicatie.existaUtilizator(emailUtilizator));
            }
        }else if(comanda[0].equals("ADD") && comanda[1].equals("ACCOUNT")) {
            //daca comanda incepe cu "ADD ACCOUNT" atunci vom adauga un cont in valuta data in lista de conturi
            //a utilizatorului cu emailul dat
            String email = comanda[2];
            String tipValuta = comanda[3];
            Cont cont  = ContFactoryImplement.createCont(tipValuta);
            if(aplicatie.existaUtilizator(email).existaCont(cont)) {
                //daca utilizatorul are deja un cont in valuta data, afisam un mesaj de eroare
                System.out.println("Account in currency " +  tipValuta + " already exists");
            } else {
                //altfel, adaugam contul in lista de conturi a utilizatorului
                aplicatie.existaUtilizator(email).adaugaCont(cont);
            }
        } else if(comanda[0].equals("ADD") && comanda[1].equals("MONEY")) {
            //daca comanda incepe cu "ADD MONEY" atunci vom adauga o suma de bani in contul utilizatorului
            String email = comanda[2];
            String tipValuta = comanda[3];
            double suma = Double.parseDouble(comanda[4]);
            aplicatie.adaugaBani(email, tipValuta,  suma);

        }else if(comanda[0].equals("LIST") && comanda[1].equals("PORTFOLIO")) {
            //daca comanda incepe cu "LIST PORTFOLIO" atunci vom afisa portofoliul utilizatorului
            String email = comanda[2];
            if (aplicatie.existaUtilizator(email) == null) {
                System.out.println("User with email " + email + " doesn't exist");
            } else {
                Utilizator utilizator = aplicatie.existaUtilizator(email);
                System.out.println(utilizator.portofoliuToString());
            }
        }else if(comanda[0].equals("EXCHANGE") && comanda[1].equals("MONEY")) {
            //daca comanda incepe cu "EXCHANGE MONEY" atunci vom schimba suma de bani dintr-un cont in altul
            String email = comanda[2];
            String valutaSursa = comanda[3];
            String valutaDestinatie = comanda[4];
            double suma = Double.parseDouble(comanda[5]);
            //cream obiectul de tip Utilizator si gasim conturile sursa si destinatie
            Utilizator utilizator = aplicatie.existaUtilizator(email);
            Cont contSursa = utilizator.gasesteCont(valutaSursa);
            Cont contDestinatie = utilizator.gasesteCont(valutaDestinatie);
            if (contSursa.getSuma() < suma) {
                //verificam daca suma de bani din contul sursa este suficienta pentru schimb
                System.out.println("Insufficient amount in account " + contSursa.getTipValuta() + " for exchange");
            } else {
                //rata de schimb din contul sursa in contul destinatie este data de matricea de exchange rates
                double exchangeRate = exchangeRates[schimbaValutaInInt(valutaDestinatie)]
                        [schimbaValutaInInt(valutaSursa)];
                //calculam suma schimbata
                double sumaSchimbata = suma * exchangeRate;
                // Aplicăm comisionul de 1% dacă suma transferată depășește 50% din valoarea contului sursă
                double comision = 0.01 * sumaSchimbata;
                /*if(utilizator.estePremium()) {
                    comision = 0;
                }*/
                //daca utilizatorul are cont premium, nu se aplica comision
                if (sumaSchimbata > 0.5 * contSursa.getSuma()) {
                    contSursa.setSuma(contSursa.getSuma() - sumaSchimbata - comision);
                } else {
                    contSursa.setSuma(contSursa.getSuma() - sumaSchimbata);
                }
                contDestinatie.setSuma(contDestinatie.getSuma() + suma);
            }
        } else if(comanda[0].equals("TRANSFER") && comanda[1].equals("MONEY")) {
            //daca comanda incepe cu "TRANSFER MONEY" atunci vom transfera o suma de bani dintr-un cont in altul
            String email1 = comanda[2];
            String email2 = comanda[3];
            String tipValuta = comanda[4];
            double suma = Double.parseDouble(comanda[5]);
            //cream si initializam utilizatorul sursa si cel destinatie
            Utilizator utilizator1 = aplicatie.existaUtilizator(email1);
            Utilizator utilizator2 = aplicatie.existaUtilizator(email2);
            //gasim contul sursa si verificam daca suma de bani este suficienta pentru transfer
            Cont contSursa = utilizator1.gasesteCont(tipValuta);

            if(contSursa.getSuma() < suma) {
                //daca nu este sufiicienta suma de bani, afisam un mesaj de eroare
                System.out.println("Insufficient amount in account " + tipValuta + " for transfer");
            }else if(utilizator2.estePrieten(utilizator1) == false) {
                //daca utilizatorul sursa nu este prieten cu utilizatorul destinatie, afisam un mesaj de eroare
                System.out.println("You are not allowed to transfer money to " + email2);
            } else {
                //altfel, scadem suma de bani din contul sursa si o adaugam in contul destinatie
                contSursa.setSuma(contSursa.getSuma() - suma);
                Cont contDestinatie = utilizator2.gasesteCont(tipValuta);
                contDestinatie.setSuma(contDestinatie.getSuma() + suma);
            }
        } else if(comanda[0].equals("RECOMMEND") && comanda[1].equals("STOCKS")) {
            //daca comanda incepe cu "RECOMMEND STOCKS" atunci vom recomanda stocurile care trebuie cumparate
            //cream un ArrayList pentru a adauga stocurile care sunt recomandate
            ArrayList<String> stocksToBuy = new ArrayList<>();
            if(EbankingApp.listaStocks.isEmpty()) {
                //daca lista de stocuri este goala, citim din fisierul de stock values
                citesteStockValues(stockValuesFile, aplicatie);
            }
            //pentru fiecare stoc, verificam daca shortTermSMA este mai mare decat longTermSMA pentru fiecare stoc
            //in caz afirmativ adaugam stocul in lista de stocuri recomandate
            for(Actiuni actiune : aplicatie.listaStocks) {
                if (actiune.shortTermSMA() > actiune.longTermSMA()) {
                    stocksToBuy.add(actiune.getNumeCompanie());
                }
            }
            //afisam lista de stocuri recomandate
            System.out.println(String.format("{\"stocksToBuy\":[\"%s\"]}", String.join("\",\"", stocksToBuy)));

        }else if(comanda[0].equals("BUY") && comanda[1].equals("STOCKS")) {
            //daca comanda incepe cu "BUY STOCKS" atunci vom cumpara un numar de actiuni dintr-o companie
            String email = comanda[2];
            String companie = comanda[3];
            int numarActiuni = Integer.parseInt(comanda[4]);
            Utilizator utilizator = aplicatie.existaUtilizator(email);
            //consideram ca utilizatorul cumpara actiuni doar in valuta USD
            Cont cont = utilizator.gasesteCont("USD");
            //daca lista de stocuri este goala, citim din fisierul de stock values
            if(EbankingApp.listaStocks.isEmpty()) {
                citesteStockValues(stockValuesFile, aplicatie);
            }
            //parcurgem lista de stocuri si verificam daca exista actiunea cu numele dat
            //in caz afirmativ, verificam daca suma de bani din cont este suficienta pentru a cumpara actiunile
            //in caz afirmativ, adaugam actiunile in lista de actiuni a utilizatorului si scadem suma de bani din cont
            for(Actiuni actiune : EbankingApp.listaStocks) {
                if (actiune.getNumeCompanie().equals(companie)) {    //verificam daca exista actiunea cu numele dat
                    if (cont.getSuma() < numarActiuni * actiune.getUltimaValoare()) {
                        System.out.println("Insufficient amount in account for buying stock");
                    } else {
                        actiune.setNumar(numarActiuni);
                        utilizator.adaugaActiune(actiune);
                        //daca utilizatorul este premium si actiunea este recomandata, se aplica o reducere de 5%
                        if(utilizator.estePremium() && actiune.shortTermSMA() > actiune.longTermSMA()) {

                            double pretRedus = actiune.getUltimaValoare() * 0.95;
                            //se aplica reducerea de 5% pentru utilizatorii premium
                            cont.setSuma(cont.getSuma() - numarActiuni * pretRedus);
                        } else {
                            cont.setSuma(cont.getSuma() - numarActiuni * actiune.getUltimaValoare());
                        }
                        actiune.getValoriInUltimele10Zile().add(actiune.getUltimaValoare());

                    }
                }
            }

        }else if(comanda[0].equals("BUY") && comanda[1].equals("PREMIUM")) {
            //daca comanda incepe cu "BUY PREMIUM" atunci vom cumpara un abonament premium
            String email = comanda[2];
            if(aplicatie.existaUtilizator(email) == null) {
                //daca utilizatorul nu exista, afisam un mesaj de eroare
                System.out.println("User with email " + email + " doesn't exist");
            } else if(aplicatie.existaUtilizator(email).gasesteCont("USD").getSuma() < 100) {
                //daca suma de bani din contul utilizatorului este mai mica decat 100, afisam un mesaj de eroare
                System.out.println("Insufficient amount in account for buying premium");
            } else {
                //altfel, setam utilizatorul ca fiind premium si scadem suma de bani din cont
                Utilizator utilizator = aplicatie.existaUtilizator(email);
                Cont contUSD = utilizator.gasesteCont("USD");
                utilizator.setPremium(); //utilizatorul devine premium
                contUSD.setSuma(contUSD.getSuma() - 100); //scadem suma de bani din cont
            }
        }
    }

    public static void main(String[] args) throws IOException {
        if(args == null) {
            System.out.println("Running Main");
            return;
        }
        //folosim Singleton pentru a ne asigura ca avem o singura instanta a aplicatiei
        EbankingApp aplicatie = EbankingApp.getInstance();
        //citim din fisierele date ca argumente
        String exchangeRatesFile = "src/main/resources/" + args[0];
        String stockValuesFile = "src/main/resources/" + args[1];
        String commandsFile = "src/main/resources/" + args[2];
        //tabelul cu ratele de schimb
        double[][] exchangeRates = citesteExchangeRates(exchangeRatesFile);

        BufferedReader br = new BufferedReader(new FileReader(commandsFile));
        //citim fiecare linie din fisierul de comenzi si procesam comanda
        String line;
        while ((line = br.readLine()) != null) {
            procesareComanda(line, exchangeRates, stockValuesFile, aplicatie);
        }
        //la final, stergem toate datele din aplicatie
        aplicatie.clean();

    }
}