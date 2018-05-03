package becustomapps.com.merchant3.Utilities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jay on 4/5/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "merchant3db.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PUNCHES = "punches";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PUNCH_ID = "punch_id";
    public static final String COLUMN_DATE_OF_SERVICE = "date_of_service";
    public static final String COLUMN_CLOCK_IN_TIME = "clock_in_time";
    public static final String COLUMN_CLOCK_IN_DATE = "clock_in_date";
    public static final String COLUMN_CLOCK_OUT_TIME = "clock_out_time";
    public static final String COLUMN_CLOCK_OUT_DATE = "clock_out_date";
    public static final String COLUMN_PUNCH_TYPE = "punch_type";
    public static final String COLUMN_ODOMETER = "odometer";
    public static final String COLUMN_PUNCH_CUST_NAME = "punch_cust_name";
    public static final String COLUMN_PUNCH_CUST_NO = "punch_cust_no";
    public static final String COLUMN_PUNCH_COMPLETED = "punch_completed";
    public static final String COLUMN_PUNCH_TRANSMITTED = "punch_transmitted";
    public static final String COLUMN_PUNCH_LEFT_OFF = "punch_left_off";
    public static final String COLUMN_ENTRY_MODE = "entry_mode";

    public static final String TABLE_CUSTOMERS = "customers";
    public static final String COLUMN_CUST_ID = "cust_id";
    public static final String COLUMN_CUST_NAME = "cust_name";
    public static final String COLUMN_CUST_ADDRESS = "cust_address";
    public static final String COLUMN_CUST_STATE_ZIP = "cust_state_zip";
    public static final String COLUMN_CUST_NO = "cust_no";
    public static final String COLUMN_CUST_TYPE = "cust_type";

    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_PROD_ID = "prod_id";
    public static final String COLUMN_PROD_NAME = "prod_name";
    public static final String COLUMN_PROD_NO = "prod_no";
    public static final String COLUMN_PROD_IS_AVAIL = "prod_is_avail";
    public static final String COLUMN_PROD_AVAILABILITY = "prod_availability";
    public static final String COLUMN_UPC_PREFIX = "upc_prefix";
    public static final String COLUMN_UPC_10 = "upc_10";
    public static final String COLUMN_UPC_CHECK = "upc_check";
    public static final String COLUMN_PROD_IMAGE_ADDRESS = "prod_image_address";

    public static final String TABLE_AUTHCODES = "authcodes";
    public static final String COLUMN_AUTH_ID = "auth_id";
    public static final String COLUMN_AUTH_PUNCH_ID = "auth_punch_id";
    public static final String COLUMN_AUTH_PROD_NAME = "auth_prod_name";
    public static final String COLUMN_AUTH_PROD_NO = "auth_prod_no";
    public static final String COLUMN_AUTH_CUST_NO = "auth_cust_no";
    public static final String COLUMN_ONHAND = "onhand";
    public static final String COLUMN_MARKDOWN = "markdown";
    public static final String COLUMN_NOTSOLD = "notsold";
    public static final String COLUMN_LOAD = "load";
    public static final String COLUMN_MDRETAIL = "mdretail";
    public static final String COLUMN_CHARGE = "charge";
    public static final String COLUMN_SHORT = "short_";
    public static final String COLUMN_DAMAGED = "damaged";
    public static final String COLUMN_CRIPPLE = "cripple";
    public static final String COLUMN_TRANSFER = "transfer";
    public static final String COLUMN_RECALL = "recall";
    public static final String COLUMN_AUTH_COMPLETED = "auth_completed";
    public static final String COLUMNN_AUTH_TRANSMITTED = "auth_transmitted";

    public static final String TABLE_LOGS = "logs";
    public static final String TABLE_FILES = "files";

    public static final String TABLE_SHIPMENTS = "shipments";
    public static final String COLUMN_SHIP_ID = "ship_id";
    public static final String COLUMN_SHIP_CUST_NO = "ship_cust_no";
    public static final String COLUMN_SHIP_PROD_NO = "ship_prod_no";
    public static final String COLUMN_SHIP_QTY = "ship_qty";
    public static final String COLUMN_SHIP_DATE = "ship_date";

    public static final String TABLE_ORDERCUSTOMERS = "ordercustomers";
    public static final String COLUMN_ORDERCUSTOMERID = "ordercustomerid";
    public static final String COLUMN_ORDERCUSTOMERACCT = "ordercustomeracct";
    public static final String COLUMN_ORDERCUSTOMERDAYS = "ordercustomerdays";
    public static final String COLUMN_ORDERCUSTOMERDATES = "ordercustomerdates";
    public static final String COLUMN_ORDERCUSTOMEREDLP = "ordercustomeredlp";
    public static final String COLUMN_ORDERCUSTOMERSTART = "ordercustomerstart";
    public static final String COLUMN_ORDERCUSTOMEREND = "ordercustomerend";
    public static final String COLUMN_ORDERCUSTOMERORDERDATE = "ordercustomerorderdate";
    public static final String COLUMN_ORDERCUSTOMERTRANSMITTED = "ordercustomertransmitted";

    public static final String TABLE_ORDERPRODUCTS = "orderproducts";
    public static final String COLUMN_ORDERPRODUCTID = "orderproductid";
    public static final String COLUMN_ORDERPRODUCTCUSTNO = "orderproductcustno";
    public static final String COLUMN_ORDERPRODUCTPRODNO = "orderproductprodno";
    public static final String COLUMN_ORDERPRODUCTONHAND = "orderproductonhand";
    public static final String COLUMN_ORDERPRODUCTGAMBLE = "orderproductgamble";
    public static final String COLUMN_ORDERPRODUCTGAMBLETYPE = "orderproductgambletype";
    public static final String COLUMN_ORDERPRODUCTFACING = "orderproductfacing";
    public static final String COLUMN_ORDERPRODUCTPREVORDERADJ = "orderproductprevorderadj";
    public static final String COLUMN_ORDERPRODUCTSHIPADJ = "orderproductshipadj";
    public static final String COLUMN_ORDERPRODUCTLOADADJTOTAL = "orderproductloadadjtotal";
    public static final String COLUMN_ORDERPRODUCTAVGSOLD = "orderproductavgsold";
    public static final String COLUMN_ORDERPRODUCTPREVWEEKSOLD = "orderproductprevweeksold";
    public static final String COLUMN_ORDERPRODUCTCURRENTORDER = "orderproductcurrentorder";
    public static final String COLUMN_ORDERPRODUCTDAILYPRODUCTAVAIL = "orderproductdailyproductavail";
    public static final String COLUMN_ORDERPRODUCTAVAILTOORDER = "orderproductavailtoorder";
    public static final String COLUMN_ORDERPRODUCTLOCKED = "orderproductlocked";
    public static final String COLUMN_ORDERPRODUCTSUGGESTEDORDER = "orderproductsuggestedorder";
    public static final String COLUMN_ORDERPRODUCTFINALORDER = "orderproductfinalorder";
    public static final String COLUMN_ORDERPRODUCTFINALORDER2 = "orderproductfinalorder2";
    public static final String COLUMN_ORDERPRODUCTFINALORDER3 = "orderproductfinalorder3";
    public static final String COLUMN_ORDERPRODUCTFINALORDER4 = "orderproductfinalorder4";
    public static final String COLUMN_ORDERPRODUCTFINALORDER5 = "orderproductfinalorder5";
    public static final String COLUMN_ORDERPRODUCTFINALORDER6 = "orderproductfinalorder6";
    public static final String COLUMN_ORDERPRODUCTFINALORDER7 = "orderproductfinalorder7";
    public static final String COLUMN_ORDERPRODUCTTOUCHED = "orderproducttouched";
    public static final String COLUMN_ORDERPRODUCTTRANSMITTED = "orderproducttransmitted";

    private static final String DATABASE_PUNCHES_CREATE = "create table " + TABLE_PUNCHES + "(" +
            COLUMN_ID + " integer primary key autoincrement, " +
            COLUMN_PUNCH_ID + " text not null, " +
            COLUMN_DATE_OF_SERVICE + " text not null, " +
            COLUMN_CLOCK_IN_TIME + " text not null, " +
            COLUMN_CLOCK_IN_DATE + " text not null, " +
            COLUMN_CLOCK_OUT_TIME + " text not null, " +
            COLUMN_CLOCK_OUT_DATE + " text not null, " +
            COLUMN_PUNCH_TYPE + " text not null, " +
            COLUMN_ODOMETER + " text not null, " +
            COLUMN_PUNCH_CUST_NAME + " text not null, " +
            COLUMN_PUNCH_CUST_NO + " text not null, " +
            COLUMN_PUNCH_COMPLETED + " text not null, " +
            COLUMN_PUNCH_TRANSMITTED + " text not null, " +
            COLUMN_PUNCH_LEFT_OFF + " text not null, " +
            COLUMN_ENTRY_MODE +" text not null);";

    private static final String DATABASE_CUSTOMERS_CREATE = "create table " + TABLE_CUSTOMERS + "(" +
            COLUMN_CUST_ID + " integer primary key autoincrement, " +
            COLUMN_CUST_NAME + " text not null, " +
            COLUMN_CUST_ADDRESS + " text not null, " +
            COLUMN_CUST_STATE_ZIP + " text not null, " +
            COLUMN_CUST_NO + " text not null, " +
            COLUMN_CUST_TYPE +" text not null);";

    private static final String DATABASE_PRODUCTS_CREATE = "create table " + TABLE_PRODUCTS + "(" +
            COLUMN_PROD_ID + " integer primary key autoincrement, " +
            COLUMN_PROD_NAME+ " text not null, " +
            COLUMN_PROD_NO + " text not null, " +
            COLUMN_PROD_IS_AVAIL + " text not null, " +
            COLUMN_PROD_AVAILABILITY + " text not null, " +
            COLUMN_UPC_PREFIX + " text not null, " +
            COLUMN_UPC_10 + " text not null, " +
            COLUMN_UPC_CHECK + " text not null, " +
            COLUMN_PROD_IMAGE_ADDRESS +" text not null);";

    private static final String DATABASE_AUTHCODES_CREATE = "create table " + TABLE_AUTHCODES + "(" +
            COLUMN_AUTH_ID + " integer primary key autoincrement, " +
            COLUMN_AUTH_PUNCH_ID + " text not null, " +
            COLUMN_AUTH_PROD_NAME + " text not null, " +
            COLUMN_AUTH_PROD_NO + " text not null, " +
            COLUMN_AUTH_CUST_NO + " text not null, " +
            COLUMN_ONHAND + " text not null, " +
            COLUMN_MARKDOWN + " text not null, " +
            COLUMN_NOTSOLD + " text not null, " +
            COLUMN_LOAD + " text not null, " +
            COLUMN_MDRETAIL + " text not null, " +
            COLUMN_CHARGE + " text not null, " +
            COLUMN_SHORT + " text not null, " +
            COLUMN_DAMAGED + " text not null, " +
            COLUMN_CRIPPLE + " text not null, " +
            COLUMN_TRANSFER + " text not null, " +
            COLUMN_RECALL + " text not null, " +
            COLUMN_AUTH_COMPLETED + " text not null, " +
            COLUMNN_AUTH_TRANSMITTED +" text not null);";

    private static final String DATABASE_SHIPMENTS_CREATE = "create table " + TABLE_SHIPMENTS + "(" +
            COLUMN_SHIP_ID + " integer primary key autoincrement, " +
            COLUMN_SHIP_CUST_NO + " text not null, " +
            COLUMN_SHIP_PROD_NO + " text not null, " +
            COLUMN_SHIP_QTY + " text not null, " +
            COLUMN_SHIP_DATE + " text not null);";

    private static final String DATABASE_ORDERCUSTOMER_CREATE = "create table " + TABLE_ORDERCUSTOMERS + "(" +
            COLUMN_ORDERCUSTOMERID + " integer primary key autoincrement, " +
            COLUMN_ORDERCUSTOMERACCT + " text not null, " +
            COLUMN_ORDERCUSTOMERDAYS + " text not null, " +
            COLUMN_ORDERCUSTOMERDATES  + " text not null, " +
            COLUMN_ORDERCUSTOMEREDLP +  " text not null, " +
            COLUMN_ORDERCUSTOMERSTART + " text not null, " +
            COLUMN_ORDERCUSTOMEREND  + " text not null, " +
            COLUMN_ORDERCUSTOMERORDERDATE + " text not null, " +
            COLUMN_ORDERCUSTOMERTRANSMITTED + " text not null);";

    private static final String DATABASE_ORDERPRODUCT_CREATE = "create table " + TABLE_ORDERPRODUCTS + "(" +
            COLUMN_ORDERPRODUCTID + " integer primary key autoincrement, " +
            COLUMN_ORDERPRODUCTCUSTNO + " text not null, " +
            COLUMN_ORDERPRODUCTPRODNO + " text not null, " +
            COLUMN_ORDERPRODUCTONHAND + " text not null, " +
            COLUMN_ORDERPRODUCTGAMBLE + " text not null, " +
            COLUMN_ORDERPRODUCTGAMBLETYPE + " text not null, " +
            COLUMN_ORDERPRODUCTFACING + " text not null, " +
            COLUMN_ORDERPRODUCTPREVORDERADJ + " text not null, " +
            COLUMN_ORDERPRODUCTSHIPADJ + " text not null, " +
            COLUMN_ORDERPRODUCTLOADADJTOTAL + " text not null, " +
            COLUMN_ORDERPRODUCTAVGSOLD + " text not null, " +
            COLUMN_ORDERPRODUCTPREVWEEKSOLD + " text not null, " +
            COLUMN_ORDERPRODUCTCURRENTORDER + " text not null, " +
            COLUMN_ORDERPRODUCTDAILYPRODUCTAVAIL + " text not null, " +
            COLUMN_ORDERPRODUCTAVAILTOORDER + " text not null, " +
            COLUMN_ORDERPRODUCTLOCKED + " text not null, " +
            COLUMN_ORDERPRODUCTSUGGESTEDORDER + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER2 + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER3 + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER4 + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER5 + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER6 + " text not null, " +
            COLUMN_ORDERPRODUCTFINALORDER7 + " text not null, " +
            COLUMN_ORDERPRODUCTTOUCHED + " text not null, " +
            COLUMN_ORDERPRODUCTTRANSMITTED +" text not null);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_PUNCHES_CREATE);
        sqLiteDatabase.execSQL(DATABASE_CUSTOMERS_CREATE);
        sqLiteDatabase.execSQL(DATABASE_PRODUCTS_CREATE);
        sqLiteDatabase.execSQL(DATABASE_AUTHCODES_CREATE);
        sqLiteDatabase.execSQL(DATABASE_SHIPMENTS_CREATE);
        sqLiteDatabase.execSQL(DATABASE_ORDERCUSTOMER_CREATE);
        sqLiteDatabase.execSQL(DATABASE_ORDERPRODUCT_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
