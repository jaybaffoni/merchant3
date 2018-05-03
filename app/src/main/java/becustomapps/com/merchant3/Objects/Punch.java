package becustomapps.com.merchant3.Objects;

import android.support.annotation.NonNull;

/**
 * Created by Jay on 4/10/2018.
 */

public class Punch implements Comparable<Punch>{

    private long id;
    private String punch_id;
    private String date_of_service;
    private String clock_in_time;
    private String clock_in_date;
    private String clock_out_time;
    private String clock_out_date;
    private String type;
    private String odometer;
    private String cust_name;
    private String cust_no;
    private int completed;
    private int transmitted;
    private int left_off;
    private String entry_mode;

    public Punch() {

    }

    public Punch(String punch_id, String date_of_service, String clock_in_time, String clock_in_date, String clock_out_time, String clock_out_date, String type, String odometer, String cust_name, String cust_no, int completed, int transmitted, int left_off, String entry_mode) {
        this.punch_id = punch_id;
        this.date_of_service = date_of_service;
        this.clock_in_time = clock_in_time;
        this.clock_in_date = clock_in_date;
        this.clock_out_time = clock_out_time;
        this.clock_out_date = clock_out_date;
        this.type = type;
        this.odometer = odometer;
        this.cust_name = cust_name;
        this.cust_no = cust_no;
        this.completed = completed;
        this.transmitted = transmitted;
        this.left_off = left_off;
        this.entry_mode = entry_mode;
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

    public String getDate_of_service() {
        return date_of_service;
    }

    public void setDate_of_service(String date_of_service) {
        this.date_of_service = date_of_service;
    }

    public String getClock_in_time() {
        return clock_in_time;
    }

    public void setClock_in_time(String clock_in_time) {
        this.clock_in_time = clock_in_time;
    }

    public String getClock_in_date() {
        return clock_in_date;
    }

    public void setClock_in_date(String clock_in_date) {
        this.clock_in_date = clock_in_date;
    }

    public String getClock_out_time() {
        return clock_out_time;
    }

    public void setClock_out_time(String clock_out_time) {
        this.clock_out_time = clock_out_time;
    }

    public String getClock_out_date() {
        return clock_out_date;
    }

    public void setClock_out_date(String clock_out_date) {
        this.clock_out_date = clock_out_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOdometer() {
        return odometer;
    }

    public void setOdometer(String odometer) {
        this.odometer = odometer;
    }

    public String getCust_name() {
        return cust_name;
    }

    public void setCust_name(String cust_name) {
        this.cust_name = cust_name;
    }

    public String getCust_no() {
        return cust_no;
    }

    public void setCust_no(String cust_no) {
        this.cust_no = cust_no;
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

    public int getLeft_off() {
        return left_off;
    }

    public void setLeft_off(int left_off) {
        this.left_off = left_off;
    }

    public String getEntry_mode() {
        return entry_mode;
    }

    public void setEntry_mode(String entry_mode) {
        this.entry_mode = entry_mode;
    }

    public String toString(){
        String toReturn = "";

        toReturn += clock_in_date + "," + clock_in_time + "," + clock_out_date + "," + clock_out_time + "," + odometer;

        return toReturn;
    }

    public String toStringForTransmit() {
        if(type.equals("SHIFT")){
            return toString();
        }
        else{
            if(type.equals("MORNING")){
                return cust_no + "," + "MORN" + ",\"" + cust_name + "\"," + toString() + "," + date_of_service + ",";
            }
            else{
                return cust_no + "," + "PULL" + ",\"" + cust_name + "\"," + toString() + "," + date_of_service + ",";
            }

        }

    }

    @Override
    public int compareTo(@NonNull Punch o) {
        return (o.clock_in_date + o.clock_in_time).compareTo(clock_in_date + clock_in_time);
    }
}
