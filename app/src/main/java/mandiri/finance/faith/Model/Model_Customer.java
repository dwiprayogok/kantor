package mandiri.finance.faith.Model;

/**
 * Created by Gue-PC on 3/23/2017.
 */

public class Model_Customer {

    String alamat;
    String lat,lon;

    public void Model_Customer(){

    }

    public void Model_Customer(String alamat,String lat, String lon){
        this.alamat = alamat;
        this.lat = lat;
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }
}
