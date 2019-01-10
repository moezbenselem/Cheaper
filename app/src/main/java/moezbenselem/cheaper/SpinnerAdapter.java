package moezbenselem.cheaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Moez on 19/06/2018.
 */

public class SpinnerAdapter extends ArrayAdapter<String> {

    private Context ctx;
    private ArrayList<String> contentArray;
    private ArrayList<Integer> imageArray;

    public SpinnerAdapter(Context context, int resource, ArrayList<String> objects,
                          ArrayList<Integer> imageArray) {
        super(context,  R.layout.spinner_layout, R.id.spinner_name, objects);
        this.ctx = context;
        this.contentArray = objects;
        this.imageArray = imageArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_layout, parent, false);

        TextView textView = (TextView) row.findViewById(R.id.spinner_name);
        textView.setText(contentArray.get(position));

        ImageView imageView = (ImageView) row.findViewById(R.id.spinner_img);
        imageView.setImageResource(imageArray.get(position));

        return row;
    }
}