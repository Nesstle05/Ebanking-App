package org.poo.cb;

public class EurCont extends Cont{
    //Clasa pentru conturile in euro
    //provine din clasa Cont
    public EurCont(double suma) {
        //apelam constructorul cu parametrii al clasei de baza
        super("EUR", suma);
    }
    public EurCont() {
        //apelam constructorul fara parametrii al clasei de baza
        super("EUR");
    }
}
