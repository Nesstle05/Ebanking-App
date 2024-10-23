package org.poo.cb;

public interface ContFactory {
    //interfata pentru crearea de conturi
    //am folosit factory drept design patter pentru a crea obiecte de tip Cont intr-un mod mai flexibil
    //de asemenea, permite utilizarea polimorfismului pentru a trata diferite tipuri de obiecte în mod uniform
    public Cont createCont(double suma);
    public Cont createCont();
}
