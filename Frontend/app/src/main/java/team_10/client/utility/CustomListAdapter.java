package team_10.client.utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.time.LocalDate;
import java.util.List;

import team_10.client.R;
import team_10.client.constant.TYPE;
import team_10.client.object.account.Account;

public class CustomListAdapter extends ArrayAdapter {

    private int resourceLayout;
    private Context mContext;

    public CustomListAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Account a = (Account) getItem(position);

        if (a != null) {
            String s = a.getClass().getSimpleName();

            TYPE t = TYPE.firstMatch(s);
            if (t != null) {
                s = t.toString();
            }

            TextView tt1 = (TextView) v.findViewById(R.id.account_title);
//            TextView tt2 = (TextView) v.findViewById(R.id.categoryId);
//            TextView tt3 = (TextView) v.findViewById(R.id.description);

            if (tt1 != null) {
                double value = a.getValue(LocalDate.now().plusMonths(18));
                tt1.setText("Label: " + a.getLabel() +
                        ", Type: " + s + ", Today's Value: " +
                        value);
            }

//            if (tt2 != null) {
//                tt2.setText(p.getCategory().getId());
//            }
//
//            if (tt3 != null) {
//                tt3.setText(p.getDescription());
//            }
        }

        return v;
    }
}
