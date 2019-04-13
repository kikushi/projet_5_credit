package proj.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import proj.myapplication.PINConfig;
import proj.myapplication.R;

public class CustomAdapter extends ArrayAdapter<PINConfig> {

    Context context;
    private ArrayList<PINConfig> pin_info;

    //View lookup
    private static class ViewHolder {
        TextView name;
        TextView informatiion;
    }

    //Constructeur
    public CustomAdapter(Context _context, ArrayList<PINConfig> values) {
        super(_context, R.layout.row_item, values);
        context = _context;
        pin_info = values;
    }


    @Override
    public View getView(int position, View rowView, ViewGroup parent) {
        PINConfig pinConfig = getItem(position);
        ViewHolder viewHolder = new ViewHolder();
        final View result;

        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            rowView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.name = (TextView) rowView.findViewById(R.id.name);
            viewHolder.informatiion = (TextView) rowView.findViewById(R.id.information);

            result = rowView;
            rowView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) rowView.getTag();
            result=rowView;
        }

        viewHolder.name.setText(pinConfig.getText());
        viewHolder.informatiion.setText(pinConfig.getSubText());
        return rowView;
    }

    public PINConfig get_element(int index){
        if (index != -1) {
            if (index < pin_info.size()) {
                return pin_info.get(index);
            }
            else
                return null;
        }
        else
            return null;
    }
}


