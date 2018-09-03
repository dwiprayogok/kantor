package mandiri.finance.faith.Model;

/**
 * Created by dwi.prayogo on 7/19/2017.
 */

public class Identitas {
    public String idNumber,CustomerID,CustomerGrade;
    public Identitas(){}
    public Identitas(String idNumber, String CustomerID, String CustomerGrade){
        this.idNumber = idNumber;
        this.CustomerGrade = CustomerGrade;
        this.CustomerID = CustomerID;

    }
    public void setCustomerGrade(String customerGrade) {
        CustomerGrade = customerGrade;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }


    public String getCustomerGrade() {
        return CustomerGrade;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getIdNumber() {
        return idNumber;
    }
}
