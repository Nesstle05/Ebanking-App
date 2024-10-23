package org.poo.cb;

public class JpyCont extends Cont{
    //Clasa pentru conturile in yeni japonezi
    //provine din clasa Cont
    public JpyCont(double suma) {
        //apelam constructorul cu parametrii al clasei de baza
        super("JPY", suma);
    }
    public JpyCont() {
        //apelam constructorul fara parametrii al clasei de baza
        super("JPY");
    }
}
