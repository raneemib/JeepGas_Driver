package com.raneem.omer.jeepgas_driver;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Omer on 1/14/2017.
 */

public class OrderCustomAdapter extends CursorAdapter {

    LayoutInflater inflater;

    public OrderCustomAdapter(Context context, Cursor c) {
        super(context, c);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.order_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView client_nameTV = (TextView) view.findViewById(R.id.client_name);
        TextView client_areaTV = (TextView) view.findViewById(R.id.client_area);
        TextView serviceTV = (TextView) view.findViewById(R.id.service);

        int nameIndex = cursor.getColumnIndex("Name");
        int areaIndex = cursor.getColumnIndex("Area");
        int serviceIndex = cursor.getColumnIndex("Service");

        String name = cursor.getString(nameIndex);
        String area = cursor.getString(areaIndex);
        String service = cursor.getString(serviceIndex);

        client_nameTV.setText(name);
        client_areaTV.setText(area);
        serviceTV.setText(service);

    }
}
