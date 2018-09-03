package mandiri.finance.faith.Model;

/**
 * Created by dwi.prayogo on 8/4/2017.
 */

public class ZipCode {

    String Kelurahan,Kecamatan,Kota,zipcode;
    String OfficeKelurahan,OfficeKecamatan,OfficeKota;
    public ZipCode(){}
    public ZipCode(String Kelurahan,String Kecamatan,String Kota, String zipcode, String officeKelurahan, String officeKecamatan, String officeKota){
        this.Kelurahan = Kelurahan;
        this.Kecamatan = Kecamatan;
        this.Kota = Kota;
        this.zipcode = zipcode;
        this.OfficeKecamatan = officeKecamatan;
        this.OfficeKelurahan = officeKelurahan;
        this.OfficeKota = officeKota;
    }

    public void setKecamatan(String kecamatan) {
        Kecamatan = kecamatan;
    }

    public void setKelurahan(String kelurahan) {
        Kelurahan = kelurahan;
    }

    public void setKota(String kota) {
        Kota = kota;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getKecamatan() {
        return Kecamatan;
    }

    public String getKelurahan() {
        return Kelurahan;
    }

    public String getKota() {
        return Kota;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setOfficeKecamatan(String officeKecamatan) {
        OfficeKecamatan = officeKecamatan;
    }

    public void setOfficeKelurahan(String officeKelurahan) {
        OfficeKelurahan = officeKelurahan;
    }

    public void setOfficeKota(String officeKota) {
        OfficeKota = officeKota;
    }

    public String getOfficeKecamatan() {
        return OfficeKecamatan;
    }

    public String getOfficeKelurahan() {
        return OfficeKelurahan;
    }

    public String getOfficeKota() {
        return OfficeKota;
    }
}
