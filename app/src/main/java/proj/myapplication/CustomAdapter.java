package proj.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] pin_info = new String[40];


    public CustomAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.pin_info = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        TextView textView2 = (TextView) rowView.findViewById(R.id.name_information);

        textView.setText(pin_info[position]);
        textView2.setText(pin_info[position]);

        return rowView;
    }
}


