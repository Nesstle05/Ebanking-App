package org.poo.cb;

public class GbpCont extends Cont{
    //Clasa pentru conturile in lire sterline
    //provine din clasa Cont
    public GbpCont(double suma) {
        //apelam constructorul cu parametrii al clasei de baza
        super("GBP", suma);
    }
    public GbpCont() {
        //apelam constructorul fara parametrii al clasei de baza
        super("GBP");
    }
}
