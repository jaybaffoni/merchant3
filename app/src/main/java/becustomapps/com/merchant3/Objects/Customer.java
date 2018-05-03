package becustomapps.com.merchant3.Objects;

/**
 * Created by Jay on 4/4/2018.
 */

public class Customer implements Comparable<Customer>{

    private long id;
    private String cust_name;
    private String cust_address;
    private String cust_state_zip;
    private String cust_no;
    private String cust_type;

    public Customer() {

    }

    public Customer(String cust_name, String cust_address, String customer_state_zip, String cust_no, String cust_type) {
        this.cust_name = cust_name;
        this.cust_address = cust_address;
        this.cust_state_zip = customer_state_zip;
        this.cust_no = cust_no;
        this.cust_type = cust_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_address() {
        return cust_address;
    }

    public void setCust_address(String cust_address) {
        this.cust_address = cust_address;
    }

    public String getCust_state_zip() {
        return cust_state_zip;
    }

    public void setCust_state_zip(String cust_state_zip) {
        this.cust_state_zip = cust_state_zip;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getCust_type() {
        return cust_type;
    }

    public void setCust_type(String cust_type) {
        this.cust_type = cust_type;
    }

    public String toString(){
        return cust_no + "\t" + cust_name + "\n" + cust_address;
    }

    @Override
    public int compareTo(Customer another) {
        return this.cust_no.compareTo(another.getCust_no());
    }

}
