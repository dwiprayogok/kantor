package mandiri.finance.faith.Model;

/**
 * Created by dwi.prayogo on 10/31/2017.
 */

public class Supplier {
    String Supplierid,SupplierEmployeeID,SupplierEmployeeName;
    String SupplierEmployeePosition;
    public Supplier(){}
    public Supplier(String Supplierid,String SupplierEmployeeID,String SupplierEmployeeName, String SupplierEmployeePosition){
        this.Supplierid = Supplierid;
        this.SupplierEmployeeID = SupplierEmployeeID;
        this.SupplierEmployeeName = SupplierEmployeeName;
        this.SupplierEmployeePosition = SupplierEmployeePosition;


    }

    public void setSupplierid(String supplierid) {
        Supplierid = supplierid;
    }

    public void setSupplierEmployeeID(String supplierEmployeeID) {
        SupplierEmployeeID = supplierEmployeeID;
    }

    public void setSupplierEmployeeName(String supplierEmployeeName) {
        SupplierEmployeeName = supplierEmployeeName;
    }

    public void setSupplierEmployeePosition(String supplierEmployeePosition) {
        SupplierEmployeePosition = supplierEmployeePosition;
    }

    public String getSupplierid() {
        return Supplierid;
    }

    public String getSupplierEmployeeID() {
        return SupplierEmployeeID;
    }

    public String getSupplierEmployeeName() {
        return SupplierEmployeeName;
    }

    public String getSupplierEmployeePosition() {
        return SupplierEmployeePosition;
    }
}
