package becustomapps.com.merchant3.Objects;

/**
 * Created by Jay on 10/18/2017.
 */

public class OrderProduct {
    private long id;
    private String custno;
    private String prodno;
    private String onhand;
    private String gamble;
    private String gambletype;
    private String facing;
    private String prevOrderAdj;
    private String shipAdj;
    private String loadAdjTotal;
    private String avgSold;
    private String prevWeekSold;
    private String currentOrder;
    private String dailyProductAvailable;
    private String availableToOrder;
    private String locked;
    private String suggestedOrder;
    private String finalOrder;
    private String finalOrder2;
    private String finalOrder3;
    private String finalOrder4;
    private String finalOrder5;
    private String finalOrder6;
    private String finalOrder7;
    private int touched;
    private int transmitted;

    public OrderProduct() {

    }

    public OrderProduct(long id, String custno, String prodno, String onhand, String gamble, String gambletype, String facing, String prevOrderAdj, String shipAdj, String loadAdjTotal, String avgSold, String prevWeekSold, String currentOrder, String dailyProductAvailable, String availableToOrder, String locked, String suggestedOrder, String finalOrder, int touched, int transmitted) {
        this.id = id;
        this.custno = custno;
        this.prodno = prodno;
        this.onhand = onhand;
        this.gamble = gamble;
        this.gambletype = gambletype;
        this.facing = facing;
        this.prevOrderAdj = prevOrderAdj;
        this.shipAdj = shipAdj;
        this.loadAdjTotal = loadAdjTotal;
        this.avgSold = avgSold;
        this.prevWeekSold = prevWeekSold;
        this.currentOrder = currentOrder;
        this.dailyProductAvailable = dailyProductAvailable;
        this.availableToOrder = availableToOrder;
        this.locked = locked;
        this.suggestedOrder = suggestedOrder;
        this.finalOrder = finalOrder;
        this.touched = touched;
        this.transmitted = transmitted;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }

    public String getProdno() {
        return prodno;
    }

    public void setProdno(String prodno) {
        this.prodno = prodno;
    }

    public String getOnhand() {
        return onhand;
    }

    public void setOnhand(String onhand) {
        this.onhand = onhand;
    }

    public String getGamble() {
        return gamble;
    }

    public void setGamble(String gamble) {
        this.gamble = gamble;
    }

    public String getGambletype() {
        return gambletype;
    }

    public void setGambletype(String gambletype) {
        this.gambletype = gambletype;
    }

    public String getFacing() {
        return facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    public String getPrevOrderAdj() {
        return prevOrderAdj;
    }

    public void setPrevOrderAdj(String prevOrderAdj) {
        this.prevOrderAdj = prevOrderAdj;
    }

    public String getShipAdj() {
        return shipAdj;
    }

    public void setShipAdj(String shipAdj) {
        this.shipAdj = shipAdj;
    }

    public String getLoadAdjTotal() {
        return loadAdjTotal;
    }

    public void setLoadAdjTotal(String loadAdjTotal) {
        this.loadAdjTotal = loadAdjTotal;
    }

    public String getAvgSold() {
        return avgSold;
    }

    public void setAvgSold(String avgSold) {
        this.avgSold = avgSold;
    }

    public String getPrevWeekSold() {
        return prevWeekSold;
    }

    public void setPrevWeekSold(String prevWeekSold) {
        this.prevWeekSold = prevWeekSold;
    }

    public String getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(String currentOrder) {
        this.currentOrder = currentOrder;
    }

    public String getDailyProductAvailable() {
        return dailyProductAvailable;
    }

    public void setDailyProductAvailable(String dailyProductAvailable) {
        this.dailyProductAvailable = dailyProductAvailable;
    }

    public String getAvailableToOrder() {
        return availableToOrder;
    }

    public void setAvailableToOrder(String availableToOrder) {
        this.availableToOrder = availableToOrder;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getSuggestedOrder() {
        return suggestedOrder;
    }

    public void setSuggestedOrder(String suggestedOrder) {
        this.suggestedOrder = suggestedOrder;
    }

    public String getFinalOrder() {
        return finalOrder;
    }

    public void setFinalOrder(String finalOrder) {
        this.finalOrder = finalOrder;
    }

    public int getTouched() {
        return touched;
    }

    public void setTouched(int touched) {
        this.touched = touched;
    }

    public int getTransmitted() {
        return transmitted;
    }

    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }

    public String getFinalOrder2() {
        return finalOrder2;
    }

    public void setFinalOrder2(String finalOrder2) {
        this.finalOrder2 = finalOrder2;
    }

    public String getFinalOrder3() {
        return finalOrder3;
    }

    public void setFinalOrder3(String finalOrder3) {
        this.finalOrder3 = finalOrder3;
    }

    public String getFinalOrder4() {
        return finalOrder4;
    }

    public void setFinalOrder4(String finalOrder4) {
        this.finalOrder4 = finalOrder4;
    }

    public String getFinalOrder5() {
        return finalOrder5;
    }

    public void setFinalOrder5(String finalOrder5) {
        this.finalOrder5 = finalOrder5;
    }

    public String getFinalOrder6() {
        return finalOrder6;
    }

    public void setFinalOrder6(String finalOrder6) {
        this.finalOrder6 = finalOrder6;
    }

    public String getFinalOrder7() {
        return finalOrder7;
    }

    public void setFinalOrder7(String finalOrder7) {
        this.finalOrder7 = finalOrder7;
    }

    public String getFinalOrderByNumber(int number){
        if(number == 0){
            return finalOrder;
        } else if(number == 1){
            return finalOrder2;
        } else if(number == 2){
            return finalOrder3;
        } else if(number == 3){
            return finalOrder4;
        } else if(number == 4){
            return finalOrder5;
        } else if(number == 5){
            return finalOrder6;
        } else {
            return finalOrder7;
        }
    }

    public boolean isEmpty(){
        if(!finalOrder.equals("")){
            return false;
        }
        if(!finalOrder2.equals("")){
            return false;
        }
        if(!finalOrder3.equals("")){
            return false;
        }
        if(!finalOrder4.equals("")){
            return false;
        }
        if(!finalOrder5.equals("")){
            return false;
        }
        if(!finalOrder6.equals("")){
            return false;
        }
        if(!finalOrder7.equals("")){
            return false;
        }
        return true;
    }

    public String toString(OrderCustomer customer){
        String dateString = customer.getDates() + " ";
        return prodno + "," +
                dateString.substring(0,6) + "," + finalOrder + "," +
                dateString.substring(6,12) + "," + finalOrder2 + "," +
                dateString.substring(12,18) + "," + finalOrder3 + "," +
                dateString.substring(18,24) + "," + finalOrder4 + "," +
                dateString.substring(24,30) + "," + finalOrder5 + "," +
                dateString.substring(30,36) + "," + finalOrder6 + "," +
                dateString.substring(36,42) + "," + finalOrder7;
    }
}
