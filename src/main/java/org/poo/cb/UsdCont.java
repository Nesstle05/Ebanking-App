package org.poo.cb;

public class UsdCont extends Cont{
    //Clasa pentru conturile in dolari americani
    //provine din clasa Cont
    public UsdCont(double suma) {
        //apelam constructorul cu parametrii al clasei de baza
        super("USD", suma);
    }
    public UsdCont() {
        //apelam constructorul fara parametrii al clasei de baza
        super("USD");
    }
}
