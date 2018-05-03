package becustomapps.com.merchant3.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import becustomapps.com.merchant3.Objects.Customer;
import becustomapps.com.merchant3.Objects.Punch;
import becustomapps.com.merchant3.R;
import becustomapps.com.merchant3.Utilities.DataSource;

/**
 * Created by Jay on 4/10/2018.
 */

public class PunchAdapter extends ArrayAdapter<Punch> {
    Context context;
    List<Punch> punches;
    DataSource datasource;

    public PunchAdapter(Context context, List<Punch> punches) {
        super(context, 0);
        this.context = context;
        this.punches = punches;
        datasource = new DataSource(context);
        datasource.open();
        Log.e("ADAPTER-COUNT", this.punches.size() + "");
    }

    @Override
    public int getCount() {
        return punches.size();
    }

    private class ViewHolder {
        TextView cust_no;
        TextView cust_name;
        TextView cust_address;
        TextView clock_in;
        TextView status;
        TextView type;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Punch punch = punches.get(position);
        Log.e("POSITION", position + "");
        Customer customer = datasource.getCustomerById(punch.getCust_no());
        if(customer == null){
            customer = new Customer("unknown name", "unknown address", "", punch.getCust_no(), "");
        }
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.punch_item, parent, false);
            holder = new ViewHolder();

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.cust_no = (TextView)convertView.findViewById(R.id.customerId);
        holder.cust_name = (TextView)convertView.findViewById(R.id.customerName);
        holder.cust_address = (TextView)convertView.findViewById(R.id.customerAddress);
        holder.clock_in = (TextView)convertView.findViewById(R.id.clockIn);
        holder.status = (TextView)convertView.findViewById(R.id.status);
        holder.type = (TextView)convertView.findViewById(R.id.punchType);

        holder.cust_no.setText(punch.getCust_no());
        holder.cust_name.setText(customer.getCust_name());
        holder.cust_address.setText(customer.getCust_address());
        holder.clock_in.setText(punch.getClock_in_date() + punch.getClock_in_time());
        holder.type.setText(punch.getType().replace("_", "-"));
        if(punch.getCompleted() == 1){
            holder.status.setText("Completed");
            holder.status.setTextColor(Color.GREEN);
        } else {
            holder.status.setText("In Progress");
            holder.status.setTextColor(Color.RED);
        }

        return convertView;
    }

}