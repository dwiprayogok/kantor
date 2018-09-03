package mandiri.finance.faith.Model;

/**
 * Created by dwi.prayogo on 10/17/2017.
 */

public class Tempatlahir {
    String TempatID,namakota;
    public Tempatlahir(){}
    public Tempatlahir(String TempatID,String namakota){
        this.TempatID = TempatID;
        this.namakota = namakota;

    }

    public void setNamakota(String namakota) {
        this.namakota = namakota;
    }

    public void setTempatID(String tempatID) {
        TempatID = tempatID;
    }

    public String getNamakota() {
        return namakota;
    }

    public String getTempatID() {
        return TempatID;
    }
}
