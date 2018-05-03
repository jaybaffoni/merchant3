package becustomapps.com.merchant3.Objects;

/**
 * Created by Jay on 4/4/2018.
 */

public class Product {

    private long id;
    private String prod_name;
    private String prod_no;
    private String isAvail;
    private String availability;
    private String upc_prefix;
    private String upc_10;
    private String upc_check;
    private String image_address;

    public Product() {

    }

    public Product(String prod_name, String prod_no, String isAvail, String availability, String upc_prefix, String upc_10, String upc_check, String image_address) {
        this.prod_name = prod_name;
        this.prod_no = prod_no;
        this.isAvail = isAvail;
        this.availability = availability;
        this.upc_prefix = upc_prefix;
        this.upc_10 = upc_10;
        this.upc_check = upc_check;
        this.image_address = image_address;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }

    public String getProd_no() {
        return prod_no;
    }

    public void setProd_no(String prod_no) {
        this.prod_no = prod_no;
    }

    public String getIsAvail() {
        return isAvail;
    }

    public void setIsAvail(String isAvail) {
        this.isAvail = isAvail;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getUpc_prefix() {
        return upc_prefix;
    }

    public void setUpc_prefix(String upc_prefix) {
        this.upc_prefix = upc_prefix;
    }

    public String getUpc_10() {
        return upc_10;
    }

    public void setUpc_10(String upc_10) {
        this.upc_10 = upc_10;
    }

    public String getUpc_check() {
        return upc_check;
    }

    public void setUpc_check(String upc_check) {
        this.upc_check = upc_check;
    }

    public String getImage_address() {
        return image_address;
    }

    public void setImage_address(String image_address) {
        this.image_address = image_address;
    }
}
