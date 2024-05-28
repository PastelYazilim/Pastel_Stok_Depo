package com.pastelyazilim.depo.ListViewAdapter;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pastelyazilim.depo.R;
import com.pastelyazilim.depo.Veritabani.Depo;

import java.util.List;

/**
 * guven_bulut tarafından 5.11.2018 tarihinde oluşturulmuştur.
 * pastel_yazilim projesi için tasarlanmaştır.
 */
public class DepolarAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<Depo> dopalar;
    private final int mResource;

    public DepolarAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, 0, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        dopalar = objects;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView depo_adi = (TextView) view.findViewById(R.id.depo_adi);

        Depo depoData = dopalar.get(position);

        depo_adi.setText(depoData.getAdi());

        return view;
    }
}
