package proj.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import proj.myapplication.PINConfig;
import proj.myapplication.R;

public class CustomAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> pin_info;



    public CustomAdapter(Context _context, ArrayList<String> values) {
        //super(_context, -1);
        super(_context, -1, values);
        context = _context;
        pin_info = values;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.name);
        //TextView textView2 = (TextView) rowView.findViewById(R.id.name_information);

        textView.setText(pin_info.get(position));
        //textView2.setText(pin_info.get(position)[1]);

        return rowView;
    }

    public String get_pin_info_name(int index){
        return pin_info.get(index);

    }
    public String get_pin_info_name_info(int index){
        return pin_info.get(index);
    }
}


