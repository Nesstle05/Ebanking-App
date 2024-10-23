package org.poo.cb;

public class ContFactoryImplement {
    //clasa care implementeaza interfata ContFactory
    public static Cont createCont(String tipValuta) {
        //metoda care creeaza un obiect de tip Cont in functie de valuta
        switch (tipValuta) {
            case "EUR":
                return new EurCont();
            case "GBP":
                return new GbpCont();
            case "JPY":
                return new JpyCont();
            case "CAD":
                return new CadCont();
            case "USD":
                return new UsdCont();
            default:
                throw new IllegalArgumentException("Valuta necunoscută: " + tipValuta);
        }
    }
}
