package org.poo.cb;

public class CadCont extends Cont{
    //Clasa pentru conturile in dolari canadieni
    //provine din clasa Cont
    public CadCont(double suma) {
        //apelam constructorul cu parametrii al clasei de baza
        super("CAD", suma);
    }
    public CadCont() {
        //apelam constructorul fara parametrii al clasei de baza
        super("CAD");
    }
}
