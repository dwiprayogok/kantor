package mandiri.finance.faith.Model;

/**
 * Created by dwi.prayogo on 7/11/2017.
 */

public class Guide {

    String pertanyaan,isi_pertanyaan,closing_pertanyaan;
    public Guide(){}
    public Guide(String pertanyaan,String isi_pertanyaan,String closing_pertanyaan){
        this.pertanyaan = pertanyaan;
        this.isi_pertanyaan = isi_pertanyaan;
        this.closing_pertanyaan = closing_pertanyaan;
    }

    public void setClosing_pertanyaan(String closing_pertanyaan) {
        this.closing_pertanyaan = closing_pertanyaan;
    }

    public void setIsi_pertanyaan(String isi_pertanyaan) {
        this.isi_pertanyaan = isi_pertanyaan;
    }

    public void setPertanyaan(String pertanyaan) {
        this.pertanyaan = pertanyaan;
    }

    public String getClosing_pertanyaan() {
        return closing_pertanyaan;
    }

    public String getIsi_pertanyaan() {
        return isi_pertanyaan;
    }

    public String getPertanyaan() {
        return pertanyaan;
    }
}
