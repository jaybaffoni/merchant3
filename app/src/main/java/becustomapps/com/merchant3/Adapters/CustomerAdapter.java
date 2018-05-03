package becustomapps.com.merchant3.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;

/**
 * Created by Jay on 4/10/2018.
 */

public class CustomerAdapter extends ArrayAdapter<Customer> {
    Context context;
    List<Customer> customers;
    DataSource datasource;
    String serviceType;

    public CustomerAdapter(Context context, List<Customer> customers, String serviceType) {
        super(context, 0);
        this.context = context;
        this.customers = customers;
        this.serviceType = serviceType;
        datasource = new DataSource(context);
        datasource.open();
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    private class ViewHolder {
        TextView cust_no;
        TextView cust_name;
        TextView cust_address;
        ImageView completed;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Customer customer = customers.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.customer_item, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.cust_no = (TextView)convertView.findViewById(R.id.cust_no);
        holder.cust_name = (TextView)convertView.findViewById(R.id.cust_name);
        holder.cust_address = (TextView)convertView.findViewById(R.id.cust_address);
        holder.completed = (ImageView)convertView.findViewById(R.id.image_check);

        holder.cust_no.setText(customer.getCust_no());
        holder.cust_name.setText(customer.getCust_name());
        holder.cust_address.setText(customer.getCust_address());

        if(datasource.punchExists(customer.getCust_no(), serviceType)){
            holder.completed.setVisibility(View.VISIBLE);
        } else {
            holder.completed.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

}