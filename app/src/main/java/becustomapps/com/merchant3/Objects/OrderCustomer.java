package becustomapps.com.merchant3.Objects;

/**
 * Created by Jay on 10/16/2017.
 */

public class OrderCustomer {
    private long id;
    private String acct;
    private String days;
    private String dates;
    private String edlp;
    private String start;
    private String end;
    private String orderdate;
    private int transmitted;

    public OrderCustomer(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDates() {
        return dates;
    }

    public void setDates(String dates) {
        this.dates = dates;
    }

    public String getEdlp() {
        return edlp;
    }

    public void setEdlp(String edlp) {
        this.edlp = edlp;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public int getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }

    public String toString(){
        return acct + "," + start + "," + end + "," + orderdate;
    }
}
