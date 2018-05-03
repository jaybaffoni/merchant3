package becustomapps.com.merchant3.Objects;

/**
 * Created by Jay on 4/4/2018.
 */

public class Authcode {

    private long id;
    private String punch_id;
    private String prod_name;
    private String prod_no;
    private String cust_no;
    private String onhand;
    private String markdown;
    private String notsold;
    private String load;
    private String mdretail;
    private int completed;
    private int transmitted;
    private String charge;
    private String short_;
    private String damaged;
    private String cripple;
    private String transfer;
    private String recall;

    public Authcode() {

    }

    public Authcode(String punch_id, String prod_name, String prod_no, String cust_no, String onhand, String markdown, String notsold, String load, String mdretail, int completed, int transmitted, String charge, String short_, String damaged, String cripple, String transfer, String recall) {
        this.punch_id = punch_id;
        this.prod_name = prod_name;
        this.prod_no = prod_no;
        this.cust_no = cust_no;
        this.onhand = onhand;
        this.markdown = markdown;
        this.notsold = notsold;
        this.load = load;
        this.mdretail = mdretail;
        this.completed = completed;
        this.transmitted = transmitted;
        this.charge = charge;
        this.short_ = short_;
        this.damaged = damaged;
        this.cripple = cripple;
        this.transfer = transfer;
        this.recall = recall;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPunch_id() {
        return punch_id;
    }

    public void setPunch_id(String punch_id) {
        this.punch_id = punch_id;
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

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
    }

    public String getOnhand() {
        return onhand;
    }

    public void setOnhand(String onhand) {
        this.onhand = onhand;
    }

    public String getMarkdown() {
        return markdown;
    }

    public void setMarkdown(String markdown) {
        this.markdown = markdown;
    }

    public String getNotsold() {
        return notsold;
    }

    public void setNotsold(String notsold) {
        this.notsold = notsold;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public String getMdretail() {
        return mdretail;
    }

    public void setMdretail(String mdretail) {
        this.mdretail = mdretail;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getShort_() {
        return short_;
    }

    public void setShort_(String short_) {
        this.short_ = short_;
    }

    public String getDamaged() {
        return damaged;
    }

    public void setDamaged(String damaged) {
        this.damaged = damaged;
    }

    public String getCripple() {
        return cripple;
    }

    public void setCripple(String cripple) {
        this.cripple = cripple;
    }

    public String getTransfer() {
        return transfer;
    }

    public void setTransfer(String transfer) {
        this.transfer = transfer;
    }

    public String getRecall() {
        return recall;
    }

    public void setRecall(String recall) {
        this.recall = recall;
    }

    public int getAdjustmentCount(){
        int total = 0;
        if(!charge.equals(""))
            total += Integer.parseInt(charge);
        if(!short_.equals(""))
            total += Integer.parseInt(short_);
        if(!damaged.equals(""))
            total += Integer.parseInt(damaged);
        if(!cripple.equals(""))
            total += Integer.parseInt(cripple);
        if(!transfer.equals(""))
            total += Integer.parseInt(transfer);
        if(!recall.equals(""))
            total += Integer.parseInt(recall);

        return total;
    }

    public String toString(){

        return cust_no + "," + prod_no + "," + onhand + "," + markdown + "," + mdretail + "," + notsold + "," + adjustmentString();
    }

    public String adjustmentString(){
        int index = 0;
        String toReturn = "";
        if(charge.equals("")){
            index++;
        }
        else{
            toReturn += charge;
            toReturn += ",1,";
        }
        if(short_.equals("")){
            index++;
        }
        else{
            toReturn += short_;
            toReturn += ",3,";
        }
        if(damaged.equals("")){
            index++;
        }
        else{
            toReturn += damaged;
            toReturn += ",4,";
        }
        if(cripple.equals("")){
            index++;
        }
        else{
            toReturn += cripple;
            toReturn += ",5,";
        }
        if(transfer.equals("")){
            index++;
        }
        else{
            toReturn += transfer;
            toReturn += ",6,";
        }
        if(recall.equals("")){
            index++;
        }
        else{
            toReturn += recall;
            toReturn += ",9,";
        }

        for(int x = 0; x < index; x++){
            toReturn += ",,";
        }

        int size = toReturn.length();
        toReturn = toReturn.substring(0,size-1);
        return toReturn;

    }
}
