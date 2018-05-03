package becustomapps.com.merchant3.Objects;

import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jay on 4/5/2018.
 */

public class Shipment {

    private long id;
    private String cust_no;
    private String prod_no;
    private int qty;
    private String date;

    public Shipment() {

    }

    public Shipment(String cust_no, String prod_no, int qty, String date) {
        this.cust_no = cust_no;
        this.prod_no = prod_no;
        this.qty = qty;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getProd_no() {
        return prod_no;
    }

    public void setProd_no(String prod_no) {
        this.prod_no = prod_no;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString(){
        String newDate = "";
        try {
            Date date1=new SimpleDateFormat("MMddyyyy").parse(date);
            newDate = (String) DateFormat.format("MM/dd/yyyy", date1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "Ship Date: " + date + " - Qty: " + qty;
        }
        return "Ship Date: " + newDate + " - Qty: " + qty;
    }
}
