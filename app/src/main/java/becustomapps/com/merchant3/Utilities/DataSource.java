package becustomapps.com.merchant3.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import becustomapps.com.merchant3.Objects.Authcode;
import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.OrderCustomer;
import becustomapps.com.merchant3.Objects.OrderProduct;
import becustomapps.com.merchant3.Objects.Product;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.Objects.Shipment;

/**
 * Created by Jay on 4/5/2018.
 */

public class DataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    private String[] customerColumns = { DatabaseHelper.COLUMN_CUST_ID,
            DatabaseHelper.COLUMN_CUST_NAME, DatabaseHelper.COLUMN_CUST_ADDRESS,
            DatabaseHelper.COLUMN_CUST_STATE_ZIP, DatabaseHelper.COLUMN_CUST_NO,
            DatabaseHelper.COLUMN_CUST_TYPE};

    private String[] productColumns = { DatabaseHelper.COLUMN_PROD_ID,
            DatabaseHelper.COLUMN_PROD_NAME, DatabaseHelper.COLUMN_PROD_NO,
            DatabaseHelper.COLUMN_PROD_IS_AVAIL, DatabaseHelper.COLUMN_PROD_AVAILABILITY,
            DatabaseHelper.COLUMN_UPC_PREFIX, DatabaseHelper.COLUMN_UPC_10,
            DatabaseHelper.COLUMN_UPC_CHECK, DatabaseHelper.COLUMN_PROD_IMAGE_ADDRESS};

    private String[] authcodeColumns = { DatabaseHelper.COLUMN_AUTH_ID,
            DatabaseHelper.COLUMN_AUTH_PUNCH_ID, DatabaseHelper.COLUMN_AUTH_PROD_NAME,
            DatabaseHelper.COLUMN_AUTH_PROD_NO, DatabaseHelper.COLUMN_AUTH_CUST_NO,
            DatabaseHelper.COLUMN_ONHAND, DatabaseHelper.COLUMN_MARKDOWN,
            DatabaseHelper.COLUMN_NOTSOLD, DatabaseHelper.COLUMN_LOAD,
            DatabaseHelper.COLUMN_MDRETAIL, DatabaseHelper.COLUMN_CHARGE,
            DatabaseHelper.COLUMN_SHORT, DatabaseHelper.COLUMN_DAMAGED,
            DatabaseHelper.COLUMN_CRIPPLE, DatabaseHelper.COLUMN_TRANSFER,
            DatabaseHelper.COLUMN_RECALL, DatabaseHelper.COLUMN_AUTH_COMPLETED,
            DatabaseHelper.COLUMNN_AUTH_TRANSMITTED};

    private String[] shipmentColumns = { DatabaseHelper.COLUMN_SHIP_ID,
            DatabaseHelper.COLUMN_SHIP_CUST_NO, DatabaseHelper.COLUMN_SHIP_PROD_NO,
            DatabaseHelper.COLUMN_SHIP_QTY, DatabaseHelper.COLUMN_SHIP_DATE};

    private String[] orderCustomerColumns = { DatabaseHelper.COLUMN_ORDERCUSTOMERID, DatabaseHelper.COLUMN_ORDERCUSTOMERACCT,
            DatabaseHelper.COLUMN_ORDERCUSTOMERDAYS, DatabaseHelper.COLUMN_ORDERCUSTOMERDATES,
            DatabaseHelper.COLUMN_ORDERCUSTOMEREDLP, DatabaseHelper.COLUMN_ORDERCUSTOMERSTART,
            DatabaseHelper.COLUMN_ORDERCUSTOMEREND, DatabaseHelper.COLUMN_ORDERCUSTOMERORDERDATE,
            DatabaseHelper.COLUMN_ORDERCUSTOMERTRANSMITTED};

    private String[] orderProductColumns = { DatabaseHelper.COLUMN_ORDERPRODUCTID, DatabaseHelper.COLUMN_ORDERPRODUCTCUSTNO,
            DatabaseHelper.COLUMN_ORDERPRODUCTPRODNO, DatabaseHelper.COLUMN_ORDERPRODUCTONHAND,
            DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLE, DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLETYPE,
            DatabaseHelper.COLUMN_ORDERPRODUCTFACING, DatabaseHelper.COLUMN_ORDERPRODUCTPREVORDERADJ,
            DatabaseHelper.COLUMN_ORDERPRODUCTSHIPADJ, DatabaseHelper.COLUMN_ORDERPRODUCTLOADADJTOTAL,
            DatabaseHelper.COLUMN_ORDERPRODUCTAVGSOLD, DatabaseHelper.COLUMN_ORDERPRODUCTPREVWEEKSOLD,
            DatabaseHelper.COLUMN_ORDERPRODUCTCURRENTORDER, DatabaseHelper.COLUMN_ORDERPRODUCTDAILYPRODUCTAVAIL,
            DatabaseHelper.COLUMN_ORDERPRODUCTAVAILTOORDER, DatabaseHelper.COLUMN_ORDERPRODUCTLOCKED,
            DatabaseHelper.COLUMN_ORDERPRODUCTSUGGESTEDORDER, DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER,
            DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER2, DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER3,
            DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER4, DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER5,
            DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER6, DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER7,
            DatabaseHelper.COLUMN_ORDERPRODUCTTOUCHED, DatabaseHelper.COLUMN_ORDERPRODUCTTRANSMITTED};

    private String[] punchColumns = { DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_PUNCH_ID, DatabaseHelper.COLUMN_DATE_OF_SERVICE,
            DatabaseHelper.COLUMN_CLOCK_IN_TIME,
            DatabaseHelper.COLUMN_CLOCK_IN_DATE, DatabaseHelper.COLUMN_CLOCK_OUT_TIME,
            DatabaseHelper.COLUMN_CLOCK_OUT_DATE, DatabaseHelper.COLUMN_PUNCH_TYPE,
            DatabaseHelper.COLUMN_ODOMETER, DatabaseHelper.COLUMN_PUNCH_CUST_NAME,
            DatabaseHelper.COLUMN_PUNCH_CUST_NO, DatabaseHelper.COLUMN_PUNCH_COMPLETED,
            DatabaseHelper.COLUMN_PUNCH_TRANSMITTED, DatabaseHelper.COLUMN_PUNCH_LEFT_OFF,
            DatabaseHelper.COLUMN_ENTRY_MODE};

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createCustomer(Customer c) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUST_NAME, c.getCust_name());
        values.put(DatabaseHelper.COLUMN_CUST_ADDRESS, c.getCust_address());
        values.put(DatabaseHelper.COLUMN_CUST_STATE_ZIP, c.getCust_state_zip());
        values.put(DatabaseHelper.COLUMN_CUST_NO, c.getCust_no());
        values.put(DatabaseHelper.COLUMN_CUST_TYPE, c.getCust_type());

        long insertId = database.insert(DatabaseHelper.TABLE_CUSTOMERS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_CUSTOMERS,
                customerColumns, DatabaseHelper.COLUMN_CUST_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updateCustomer(Customer c) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CUST_NAME, c.getCust_name());
        values.put(DatabaseHelper.COLUMN_CUST_ADDRESS, c.getCust_address());
        values.put(DatabaseHelper.COLUMN_CUST_STATE_ZIP, c.getCust_state_zip());
        values.put(DatabaseHelper.COLUMN_CUST_NO, c.getCust_no());
        values.put(DatabaseHelper.COLUMN_CUST_TYPE, c.getCust_type());

        // updating row
        return database.update(DatabaseHelper.TABLE_CUSTOMERS, values, DatabaseHelper.COLUMN_CUST_ID + " = ?",
                new String[] { String.valueOf(c.getId()) });
    }

    public void deleteCustomer(Customer customer) {
        long id = customer.getId();
        System.out.println("Customer deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_CUSTOMERS, DatabaseHelper.COLUMN_CUST_ID
                + " = " + id, null);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<Customer>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_CUSTOMERS,
                customerColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Customer customer = cursorToCustomer(cursor);
            customers.add(customer);
            cursor.moveToNext();
        }

        cursor.close();
        return customers;
    }

    private Customer cursorToCustomer(Cursor cursor) {
        Customer c = new Customer();
        c.setId(cursor.getLong(0));
        c.setCust_name(cursor.getString(1));
        c.setCust_address(cursor.getString(2));
        c.setCust_state_zip(cursor.getString(3));
        c.setCust_no(cursor.getString(4));
        c.setCust_type(cursor.getString(5));
        return c;
    }

    public boolean customerExists(String cust_no){
        String Query = "Select * from " + DatabaseHelper.TABLE_CUSTOMERS + " where " + DatabaseHelper.COLUMN_CUST_NO + " = '" + cust_no + "'";
        Cursor cursor = database.rawQuery(Query, null);
        //Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Customer getCustomerById(String cust_no){

        String whereClause = DatabaseHelper.COLUMN_CUST_NO + " = ?";
        String[] whereArgs = new String[] {
                cust_no
        };
        //Log.e("CLAUSE", whereClause);
        Cursor cursor = database.query(DatabaseHelper.TABLE_CUSTOMERS, customerColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        //Log.e("COUNT", cursor.getCount() + "");
        return cursorToCustomer(cursor);
    }

    public void createProduct(Product p) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PROD_NAME, p.getProd_name());
        values.put(DatabaseHelper.COLUMN_PROD_NO, p.getProd_no());
        values.put(DatabaseHelper.COLUMN_PROD_IS_AVAIL, p.getIsAvail());
        values.put(DatabaseHelper.COLUMN_PROD_AVAILABILITY, p.getAvailability());
        values.put(DatabaseHelper.COLUMN_UPC_PREFIX, p.getUpc_prefix());
        values.put(DatabaseHelper.COLUMN_UPC_10, p.getUpc_10());
        values.put(DatabaseHelper.COLUMN_UPC_CHECK, p.getUpc_check());
        values.put(DatabaseHelper.COLUMN_PROD_IMAGE_ADDRESS, p.getImage_address());

        long insertId = database.insert(DatabaseHelper.TABLE_PRODUCTS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS,
                productColumns, DatabaseHelper.COLUMN_PROD_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updateProduct(Product p) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PROD_NAME, p.getProd_name());
        values.put(DatabaseHelper.COLUMN_PROD_NO, p.getProd_no());
        values.put(DatabaseHelper.COLUMN_PROD_IS_AVAIL, p.getIsAvail());
        values.put(DatabaseHelper.COLUMN_PROD_AVAILABILITY, p.getAvailability());
        values.put(DatabaseHelper.COLUMN_UPC_PREFIX, p.getUpc_prefix());
        values.put(DatabaseHelper.COLUMN_UPC_10, p.getUpc_10());
        values.put(DatabaseHelper.COLUMN_UPC_CHECK, p.getUpc_check());
        values.put(DatabaseHelper.COLUMN_PROD_IMAGE_ADDRESS, p.getImage_address());

        // updating row
        return database.update(DatabaseHelper.TABLE_PRODUCTS, values, DatabaseHelper.COLUMN_PROD_ID + " = ?",
                new String[] { String.valueOf(p.getId()) });
    }

    public void deleteProduct(Product product) {
        long id = product.getId();
        System.out.println("Product deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_PRODUCTS, DatabaseHelper.COLUMN_PROD_ID
                + " = " + id, null);
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<Product>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_PRODUCTS,
                productColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = cursorToProduct(cursor);
            products.add(product);
            cursor.moveToNext();
        }

        cursor.close();
        return products;
    }

    private Product cursorToProduct(Cursor cursor) {
        Product c = new Product();
        c.setId(cursor.getLong(0));
        c.setProd_name(cursor.getString(1));
        c.setProd_no(cursor.getString(2));
        c.setIsAvail(cursor.getString(3));
        c.setAvailability(cursor.getString(4));
        c.setUpc_prefix(cursor.getString(5));
        c.setUpc_10(cursor.getString(6));
        c.setUpc_check(cursor.getString(7));
        c.setImage_address(cursor.getString(8));
        return c;
    }

    public boolean productExists(String prod_no){
        String Query = "Select * from " + DatabaseHelper.TABLE_PRODUCTS + " where " + DatabaseHelper.COLUMN_PROD_NO + " = '" + prod_no + "'";
        Cursor cursor = database.rawQuery(Query, null);
        //Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void createAuthcode(Authcode a) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AUTH_PUNCH_ID, a.getPunch_id());
        values.put(DatabaseHelper.COLUMN_AUTH_PROD_NAME, a.getProd_name());
        values.put(DatabaseHelper.COLUMN_AUTH_PROD_NO, a.getProd_no());
        values.put(DatabaseHelper.COLUMN_AUTH_CUST_NO, a.getCust_no());
        values.put(DatabaseHelper.COLUMN_ONHAND, a.getOnhand());
        values.put(DatabaseHelper.COLUMN_MARKDOWN, a.getMarkdown());
        values.put(DatabaseHelper.COLUMN_NOTSOLD, a.getNotsold());
        values.put(DatabaseHelper.COLUMN_LOAD, a.getLoad());
        values.put(DatabaseHelper.COLUMN_MDRETAIL, a.getMdretail());
        values.put(DatabaseHelper.COLUMN_CHARGE, a.getCharge());
        values.put(DatabaseHelper.COLUMN_SHORT, a.getShort_());
        values.put(DatabaseHelper.COLUMN_DAMAGED, a.getDamaged());
        values.put(DatabaseHelper.COLUMN_CRIPPLE, a.getCripple());
        values.put(DatabaseHelper.COLUMN_TRANSFER, a.getTransfer());
        values.put(DatabaseHelper.COLUMN_RECALL, a.getRecall());
        values.put(DatabaseHelper.COLUMN_AUTH_COMPLETED, a.getCompleted());
        values.put(DatabaseHelper.COLUMNN_AUTH_TRANSMITTED, a.getTransmitted());

        long insertId = database.insert(DatabaseHelper.TABLE_AUTHCODES, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_AUTHCODES,
                authcodeColumns, DatabaseHelper.COLUMN_AUTH_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updateAuthcode(Authcode a) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_AUTH_PUNCH_ID, a.getPunch_id());
        values.put(DatabaseHelper.COLUMN_AUTH_PROD_NAME, a.getProd_name());
        values.put(DatabaseHelper.COLUMN_AUTH_PROD_NO, a.getProd_no());
        values.put(DatabaseHelper.COLUMN_AUTH_CUST_NO, a.getCust_no());
        values.put(DatabaseHelper.COLUMN_ONHAND, a.getOnhand());
        values.put(DatabaseHelper.COLUMN_MARKDOWN, a.getMarkdown());
        values.put(DatabaseHelper.COLUMN_NOTSOLD, a.getNotsold());
        values.put(DatabaseHelper.COLUMN_LOAD, a.getLoad());
        values.put(DatabaseHelper.COLUMN_MDRETAIL, a.getMdretail());
        values.put(DatabaseHelper.COLUMN_CHARGE, a.getCharge());
        values.put(DatabaseHelper.COLUMN_SHORT, a.getShort_());
        values.put(DatabaseHelper.COLUMN_DAMAGED, a.getDamaged());
        values.put(DatabaseHelper.COLUMN_CRIPPLE, a.getCripple());
        values.put(DatabaseHelper.COLUMN_TRANSFER, a.getTransfer());
        values.put(DatabaseHelper.COLUMN_RECALL, a.getRecall());
        values.put(DatabaseHelper.COLUMN_AUTH_COMPLETED, a.getCompleted());
        values.put(DatabaseHelper.COLUMNN_AUTH_TRANSMITTED, a.getTransmitted());

        // updating row
        return database.update(DatabaseHelper.TABLE_AUTHCODES, values, DatabaseHelper.COLUMN_AUTH_ID + " = ?",
                new String[] { String.valueOf(a.getId()) });
    }

    public void deleteAuthcode(Authcode authcode) {
        long id = authcode.getId();
        System.out.println("Authcode deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_AUTHCODES, DatabaseHelper.COLUMN_AUTH_ID
                + " = " + id, null);
    }

    public List<Authcode> getAllAuthcodes() {
        List<Authcode> authcodes = new ArrayList<Authcode>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_AUTHCODES,
                authcodeColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Authcode authcode = cursorToAuthcode(cursor);
            authcodes.add(authcode);
            cursor.moveToNext();
        }

        cursor.close();
        return authcodes;
    }

    public List<Authcode> getAuthcodesWithPID(String p_id){
        List<Authcode> authcodes = new ArrayList<Authcode>();
        String Query = "Select * from " + DatabaseHelper.TABLE_AUTHCODES + " where " + DatabaseHelper.COLUMN_AUTH_PUNCH_ID + " = '" + p_id + "'";
        Cursor cursor = database.rawQuery(Query, null);
        Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Authcode authcode = cursorToAuthcode(cursor);
                authcodes.add(authcode);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return authcodes;
    }

    public List<Authcode> getAllTemplates(String acct7) {
        List<Authcode> authcodes = new ArrayList<Authcode>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_AUTHCODES,
                authcodeColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Authcode authcode = cursorToAuthcode(cursor);
            if(authcode.getCust_no().startsWith(acct7) && authcode.getPunch_id().equals("template")){
                authcodes.add(authcode);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return authcodes;
    }

    private Authcode cursorToAuthcode(Cursor cursor) {
        Authcode c = new Authcode();
        c.setId(cursor.getLong(0));
        c.setPunch_id(cursor.getString(1));
        c.setProd_name(cursor.getString(2));
        c.setProd_no(cursor.getString(3));
        c.setCust_no(cursor.getString(4));
        c.setOnhand(cursor.getString(5));
        c.setMarkdown(cursor.getString(6));
        c.setNotsold(cursor.getString(7));
        c.setLoad(cursor.getString(8));
        c.setMdretail(cursor.getString(9));
        c.setCharge(cursor.getString(10));
        c.setShort_(cursor.getString(11));
        c.setDamaged(cursor.getString(12));
        c.setCripple(cursor.getString(13));
        c.setTransfer(cursor.getString(14));
        c.setRecall(cursor.getString(15));
        c.setCompleted(cursor.getInt(16));
        c.setTransmitted(cursor.getInt(17));
        return c;
    }

    public boolean authcodeExists(String p_id, String cust_no, String prod_no){
        String Query = "Select * from " + DatabaseHelper.TABLE_AUTHCODES + " where " + DatabaseHelper.COLUMN_AUTH_PUNCH_ID + " = '" + p_id + "' AND " + DatabaseHelper.COLUMN_AUTH_CUST_NO + " = '" + cust_no + "' AND " + DatabaseHelper.COLUMN_AUTH_PROD_NO + " = '" + prod_no + "'";
        Cursor cursor = database.rawQuery(Query, null);
        //Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void createShipment(String cust_no, String prod_no, int qty, String date) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SHIP_CUST_NO, cust_no);
        values.put(DatabaseHelper.COLUMN_SHIP_PROD_NO, prod_no);
        values.put(DatabaseHelper.COLUMN_SHIP_QTY, qty);
        values.put(DatabaseHelper.COLUMN_SHIP_DATE, date);

        long insertId = database.insert(DatabaseHelper.TABLE_SHIPMENTS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_SHIPMENTS,
                shipmentColumns, DatabaseHelper.COLUMN_SHIP_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updateShipment(Shipment s) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_SHIP_CUST_NO, s.getCust_no());
        values.put(DatabaseHelper.COLUMN_SHIP_PROD_NO, s.getProd_no());
        values.put(DatabaseHelper.COLUMN_SHIP_QTY, s.getQty());
        values.put(DatabaseHelper.COLUMN_SHIP_DATE, s.getDate());

        // updating row
        return database.update(DatabaseHelper.TABLE_SHIPMENTS, values, DatabaseHelper.COLUMN_SHIP_ID + " = ?",
                new String[] { String.valueOf(s.getId()) });
    }

    public void deleteShipment(Shipment s) {
        long id = s.getId();
        System.out.println("Shipment deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_SHIPMENTS, DatabaseHelper.COLUMN_SHIP_ID
                + " = " + id, null);
    }

    public List<Shipment> getAllShipments() {
        List<Shipment> shipments = new ArrayList<Shipment>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_SHIPMENTS,
                shipmentColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Shipment customer = cursorToShipment(cursor);
            shipments.add(customer);
            cursor.moveToNext();
        }

        cursor.close();
        return shipments;
    }

    public List<Shipment> getShipmentsByDate(String cust_no, String ship_date){
        List<Shipment> shipments = new ArrayList<Shipment>();
        String Query = "Select * from " + DatabaseHelper.TABLE_SHIPMENTS + " where " + DatabaseHelper.COLUMN_SHIP_CUST_NO + " = '" + cust_no + "' AND " + DatabaseHelper.COLUMN_SHIP_DATE + " = '" + ship_date + "'";
        Cursor cursor = database.rawQuery(Query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Shipment customer = cursorToShipment(cursor);
            shipments.add(customer);
            cursor.moveToNext();
        }

        cursor.close();
        return shipments;
    }

    private Shipment cursorToShipment(Cursor cursor) {
        Shipment c = new Shipment();
        c.setId(cursor.getLong(0));
        c.setCust_no(cursor.getString(1));
        c.setProd_no(cursor.getString(2));
        c.setQty(cursor.getInt(3));
        c.setDate(cursor.getString(4));
        return c;
    }

    public void createOrderCustomer(String acct, String days, String dates, String edlp, String orderdate) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERACCT, acct);
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERDAYS, days);
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERDATES, dates);
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMEREDLP, edlp);
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERSTART, "");
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMEREND, "");
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERORDERDATE, orderdate);
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERTRANSMITTED, 0);

        long insertId = database.insert(DatabaseHelper.TABLE_ORDERCUSTOMERS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERCUSTOMERS,
                orderCustomerColumns, DatabaseHelper.COLUMN_ORDERCUSTOMERID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        //Log.e("OrderCustomerCreated", acct);
        return;
    }

    public void createOrderProduct(String custno, String prodno, String onhand, String gamble,
                                           String gambletype, String facing, String prevOrderAdj, String shipAdj,
                                           String loadAdjTotal, String avgSold, String prevWeekSold, String currentOrder,
                                           String dailyProductAvailable, String availableToOrder, String locked,
                                           String suggestedOrder, String finalOrder, String finalOrder2, String finalOrder3,
                                           String finalOrder4, String finalOrder5, String finalOrder6, String finalOrder7,
                                           int touched, int transmitted) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTCUSTNO, custno);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPRODNO, prodno);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTONHAND, onhand);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLE, gamble);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLETYPE, gambletype);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFACING, facing);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPREVORDERADJ, prevOrderAdj);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTSHIPADJ, shipAdj);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTLOADADJTOTAL, loadAdjTotal);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTAVGSOLD, avgSold);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPREVWEEKSOLD, prevWeekSold);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTCURRENTORDER, currentOrder);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTDAILYPRODUCTAVAIL, dailyProductAvailable);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTAVAILTOORDER, availableToOrder);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTLOCKED, locked);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTSUGGESTEDORDER, suggestedOrder);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER, finalOrder);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER2, finalOrder2);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER3, finalOrder3);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER4, finalOrder4);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER5, finalOrder5);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER6, finalOrder6);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER7, finalOrder7);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTTOUCHED, touched);
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTTRANSMITTED, transmitted);

        long insertId = database.insert(DatabaseHelper.TABLE_ORDERPRODUCTS, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERPRODUCTS,
                orderProductColumns, DatabaseHelper.COLUMN_ORDERPRODUCTID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updateOrderCustomer(OrderCustomer s) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERACCT, s.getAcct());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERDAYS, s.getDays());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERDATES, s.getDates());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMEREDLP, s.getEdlp());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERSTART, s.getStart());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMEREND, s.getEnd());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERORDERDATE, s.getOrderdate());
        values.put(DatabaseHelper.COLUMN_ORDERCUSTOMERTRANSMITTED, s.getTransmitted());

        // updating row
        return database.update(DatabaseHelper.TABLE_ORDERCUSTOMERS, values, DatabaseHelper.COLUMN_ORDERCUSTOMERID + " = ?",
                new String[] { String.valueOf(s.getId()) });
    }

    public int updateOrderProduct(OrderProduct s) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTCUSTNO, s.getCustno());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPRODNO, s.getProdno());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTONHAND, s.getOnhand());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLE, s.getGamble());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTGAMBLETYPE, s.getGambletype());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFACING, s.getFacing());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPREVORDERADJ, s.getPrevOrderAdj());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTSHIPADJ, s.getShipAdj());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTLOADADJTOTAL, s.getLoadAdjTotal());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTAVGSOLD, s.getAvgSold());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTPREVWEEKSOLD, s.getPrevWeekSold());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTCURRENTORDER, s.getCurrentOrder());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTDAILYPRODUCTAVAIL, s.getDailyProductAvailable());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTAVAILTOORDER, s.getAvailableToOrder());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTLOCKED, s.getLocked());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTSUGGESTEDORDER, s.getSuggestedOrder());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER, s.getFinalOrder());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER2, s.getFinalOrder2());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER3, s.getFinalOrder3());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER4, s.getFinalOrder4());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER5, s.getFinalOrder5());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER6, s.getFinalOrder6());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTFINALORDER7, s.getFinalOrder7());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTTOUCHED, s.getTouched());
        values.put(DatabaseHelper.COLUMN_ORDERPRODUCTTRANSMITTED, s.getTransmitted());

        // updating row
        return database.update(DatabaseHelper.TABLE_ORDERPRODUCTS, values, DatabaseHelper.COLUMN_ORDERPRODUCTID + " = ?",
                new String[] { String.valueOf(s.getId()) });
    }

    public void deleteOrderCustomer(OrderCustomer s) {
        long id = s.getId();
        System.out.println("OrderCustomer deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_ORDERCUSTOMERS, DatabaseHelper.COLUMN_ORDERCUSTOMERID
                + " = " + id, null);
    }

    public void deleteOrderProduct(OrderProduct s) {
        long id = s.getId();
        System.out.println("OrderProduct deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_ORDERPRODUCTS, DatabaseHelper.COLUMN_ORDERPRODUCTID
                + " = " + id, null);
    }

    public List<OrderCustomer> getAllOrderCustomers() {
        List<OrderCustomer> sd = new ArrayList<OrderCustomer>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERCUSTOMERS,
                orderCustomerColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderCustomer s = cursorToOrderCustomer(cursor);
            sd.add(s);
            cursor.moveToNext();
        }

        cursor.close();
        return sd;
    }

    public List<OrderProduct> getAllOrderProducts() {
        List<OrderProduct> sd = new ArrayList<OrderProduct>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERPRODUCTS,
                orderProductColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            OrderProduct s = cursorToOrderProduct(cursor);
            sd.add(s);
            cursor.moveToNext();
        }

        cursor.close();
        return sd;
    }

    private OrderCustomer cursorToOrderCustomer(Cursor cursor) {
        OrderCustomer s = new OrderCustomer();
        s.setId(cursor.getLong(0));
        s.setAcct(cursor.getString(1));
        s.setDays(cursor.getString(2));
        s.setDates(cursor.getString(3));
        s.setEdlp(cursor.getString(4));
        s.setStart(cursor.getString(5));
        s.setEnd(cursor.getString(6));
        s.setOrderdate(cursor.getString(7));
        s.setTransmitted(cursor.getInt(8));
        return s;
    }

    private OrderProduct cursorToOrderProduct(Cursor cursor) {
        OrderProduct s = new OrderProduct();
        s.setId(cursor.getLong(0));
        s.setCustno(cursor.getString(1));
        s.setProdno(cursor.getString(2));
        s.setOnhand(cursor.getString(3));
        s.setGamble(cursor.getString(4));
        s.setGambletype(cursor.getString(5));
        s.setFacing(cursor.getString(6));
        s.setPrevOrderAdj(cursor.getString(7));
        s.setShipAdj(cursor.getString(8));
        s.setLoadAdjTotal(cursor.getString(9));
        s.setAvgSold(cursor.getString(10));
        s.setPrevWeekSold(cursor.getString(11));
        s.setCurrentOrder(cursor.getString(12));
        s.setDailyProductAvailable(cursor.getString(13));
        s.setAvailableToOrder(cursor.getString(14));
        s.setLocked(cursor.getString(15));
        s.setSuggestedOrder(cursor.getString(16));
        s.setFinalOrder(cursor.getString(17));
        s.setFinalOrder2(cursor.getString(18));
        s.setFinalOrder3(cursor.getString(19));
        s.setFinalOrder4(cursor.getString(20));
        s.setFinalOrder5(cursor.getString(21));
        s.setFinalOrder6(cursor.getString(22));
        s.setFinalOrder7(cursor.getString(23));
        s.setTouched(cursor.getInt(24));
        s.setTransmitted(cursor.getInt(25));
        return s;
    }

    public boolean orderCustomerExists(String cust_no){
        String Query = "Select * from " + DatabaseHelper.TABLE_ORDERCUSTOMERS + " where " + DatabaseHelper.COLUMN_ORDERCUSTOMERACCT + " = '" + cust_no + "'";
        Cursor cursor = database.rawQuery(Query, null);
        //Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public OrderCustomer getOrderCustomerById(String cust_no){

        String whereClause = DatabaseHelper.COLUMN_ORDERCUSTOMERACCT + " = ?";
        String[] whereArgs = new String[] {
                cust_no
        };
        //Log.e("CLAUSE", whereClause);
        Cursor cursor = database.query(DatabaseHelper.TABLE_ORDERCUSTOMERS, orderCustomerColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        //Log.e("COUNT", cursor.getCount() + "");
        return cursorToOrderCustomer(cursor);
    }

    public boolean orderProductExists(String cust_no, String prod_no){
        String Query = "Select * from " + DatabaseHelper.TABLE_ORDERPRODUCTS + " where " + DatabaseHelper.COLUMN_ORDERPRODUCTCUSTNO + " = '" + cust_no + "' AND " + DatabaseHelper.COLUMN_ORDERPRODUCTPRODNO + " = '" + prod_no + "'";
        Cursor cursor = database.rawQuery(Query, null);
        //Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public void createPunch(Punch p) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PUNCH_ID, p.getPunch_id());
        values.put(DatabaseHelper.COLUMN_DATE_OF_SERVICE, p.getDate_of_service());
        values.put(DatabaseHelper.COLUMN_CLOCK_IN_TIME, p.getClock_in_time());
        values.put(DatabaseHelper.COLUMN_CLOCK_IN_DATE, p.getClock_in_date());
        values.put(DatabaseHelper.COLUMN_CLOCK_OUT_TIME, p.getClock_out_time());
        values.put(DatabaseHelper.COLUMN_CLOCK_OUT_DATE, p.getClock_out_date());
        values.put(DatabaseHelper.COLUMN_PUNCH_TYPE, p.getType());
        values.put(DatabaseHelper.COLUMN_ODOMETER, p.getOdometer());
        values.put(DatabaseHelper.COLUMN_PUNCH_CUST_NAME, p.getCust_name());
        values.put(DatabaseHelper.COLUMN_PUNCH_CUST_NO, p.getCust_no());
        values.put(DatabaseHelper.COLUMN_PUNCH_COMPLETED, p.getCompleted());
        values.put(DatabaseHelper.COLUMN_PUNCH_TRANSMITTED, p.getTransmitted());
        values.put(DatabaseHelper.COLUMN_PUNCH_LEFT_OFF, p.getLeft_off());
        values.put(DatabaseHelper.COLUMN_ENTRY_MODE, p.getEntry_mode());

        long insertId = database.insert(DatabaseHelper.TABLE_PUNCHES, null,
                values);
        Cursor cursor = database.query(DatabaseHelper.TABLE_PUNCHES,
                punchColumns, DatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        cursor.close();
        return;
    }

    public int updatePunch(Punch p) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_PUNCH_ID, p.getPunch_id());
        values.put(DatabaseHelper.COLUMN_DATE_OF_SERVICE, p.getDate_of_service());
        values.put(DatabaseHelper.COLUMN_CLOCK_IN_TIME, p.getClock_in_time());
        values.put(DatabaseHelper.COLUMN_CLOCK_IN_DATE, p.getClock_in_date());
        values.put(DatabaseHelper.COLUMN_CLOCK_OUT_TIME, p.getClock_out_time());
        values.put(DatabaseHelper.COLUMN_CLOCK_OUT_DATE, p.getClock_out_date());
        values.put(DatabaseHelper.COLUMN_PUNCH_TYPE, p.getType());
        values.put(DatabaseHelper.COLUMN_ODOMETER, p.getOdometer());
        values.put(DatabaseHelper.COLUMN_PUNCH_CUST_NAME, p.getCust_name());
        values.put(DatabaseHelper.COLUMN_PUNCH_CUST_NO, p.getCust_no());
        values.put(DatabaseHelper.COLUMN_PUNCH_COMPLETED, p.getCompleted());
        values.put(DatabaseHelper.COLUMN_PUNCH_TRANSMITTED, p.getTransmitted());
        values.put(DatabaseHelper.COLUMN_PUNCH_LEFT_OFF, p.getLeft_off());
        values.put(DatabaseHelper.COLUMN_ENTRY_MODE, p.getEntry_mode());

        // updating row
        return database.update(DatabaseHelper.TABLE_PUNCHES, values, DatabaseHelper.COLUMN_ID + " = ?",
                new String[] { String.valueOf(p.getId()) });
    }

    public void deletePunch(Punch punch) {
        long id = punch.getId();
        System.out.println("Punch deleted with id: " + id);
        database.delete(DatabaseHelper.TABLE_PUNCHES, DatabaseHelper.COLUMN_ID
                + " = " + id, null);
    }

    public List<Punch> getAllPunches() {
        List<Punch> punches = new ArrayList<Punch>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_PUNCHES,
                punchColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Punch punch = cursorToPunch(cursor);
            punches.add(punch);
            cursor.moveToNext();
        }

        cursor.close();
        return punches;
    }

    public List<Punch> getAllServicePunches() {
        List<Punch> punches = new ArrayList<Punch>();

        Cursor cursor = database.query(DatabaseHelper.TABLE_PUNCHES,
                punchColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Punch punch = cursorToPunch(cursor);
            if(punch.getType().equals("MORNING") || punch.getType().equals("PULL_UP")){
                punches.add(punch);
            }
            cursor.moveToNext();
        }

        cursor.close();
        return punches;
    }

    private Punch cursorToPunch(Cursor cursor) {
        Punch c = new Punch();
        c.setId(cursor.getLong(0));
        c.setPunch_id(cursor.getString(1));
        c.setDate_of_service(cursor.getString(2));
        c.setClock_in_time(cursor.getString(3));
        c.setClock_in_date(cursor.getString(4));
        c.setClock_out_time(cursor.getString(5));
        c.setClock_out_date(cursor.getString(6));
        c.setType(cursor.getString(7));
        c.setOdometer(cursor.getString(8));
        c.setCust_name(cursor.getString(9));
        c.setCust_no(cursor.getString(10));
        c.setCompleted(cursor.getInt(11));
        c.setTransmitted(cursor.getInt(12));
        c.setLeft_off(cursor.getInt(13));
        c.setEntry_mode(cursor.getString(14));
        return c;
    }

    public boolean punchExists(String cust_no, String type){
        String Query = "Select * from " + DatabaseHelper.TABLE_PUNCHES + " where " + DatabaseHelper.COLUMN_PUNCH_CUST_NO + " = '" + cust_no + "' AND " + DatabaseHelper.COLUMN_PUNCH_TYPE + " = '" + type + "'";
        Cursor cursor = database.rawQuery(Query, null);
        Log.e("CURSOR", cursor.getCount() + "");
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public Punch getPunchById(String punch_id){

        String whereClause = DatabaseHelper.COLUMN_PUNCH_ID + " = ?";
        String[] whereArgs = new String[] {
                punch_id
        };
        //Log.e("CLAUSE", whereClause);
        Cursor cursor = database.query(DatabaseHelper.TABLE_PUNCHES, punchColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        //Log.e("COUNT", cursor.getCount() + "");
        return cursorToPunch(cursor);
    }

    public void clearAll(){
        database.execSQL("delete from "+ DatabaseHelper.TABLE_PUNCHES);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_PRODUCTS);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_AUTHCODES);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_CUSTOMERS);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_SHIPMENTS);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_ORDERPRODUCTS);
        database.execSQL("delete from "+ DatabaseHelper.TABLE_ORDERCUSTOMERS);
    }

}
