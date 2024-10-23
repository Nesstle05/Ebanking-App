package org.poo.cb;

public class Cont {
    private String tipValuta;
    private double suma;
    //am creat doi constructori cu parametrii pentru a putea initializa obiectele
    public Cont(String tipValuta){
        this.tipValuta = tipValuta;
    }

    public Cont(String tipValuta, double suma){
        this.tipValuta = tipValuta;
        this.suma = suma;
    }
    //am creat doi getteri si un setter pentru a putea accesa si modifica atributele private
    public String getTipValuta(){
        return this.tipValuta;
    }

    public double getSuma(){
        return this.suma;
    }
    public void setSuma(double suma){
        this.suma = suma;
    }


}
