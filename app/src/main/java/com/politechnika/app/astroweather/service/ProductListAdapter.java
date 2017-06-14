package com.politechnika.app.astroweather.service;

import java.util.List;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.politechnika.app.astroweather.R;

/**
 * Created by Jacek on 2017-06-11.
 */

public class ProductListAdapter extends ArrayAdapter<String> {

    private Context context;
    List<String> products;
    SharedPreference sharedPreference;

    public ProductListAdapter(Context context, List<String> products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
        this.products = products;
        sharedPreference = new SharedPreference();
    }

    private class ViewHolder {
        TextView productNameTxt;
        ImageView favoriteImg;
    }

    @Override
    public int getCount() {
        return products.size();
    }

    @Override
    public String getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.product_list_item, null);
            holder = new ViewHolder();
            holder.productNameTxt = (TextView) convertView
                    .findViewById(R.id.txt_pdt_name);
            holder.favoriteImg = (ImageView) convertView
                    .findViewById(R.id.imgbtn_favorite);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String product = (String) getItem(position);
        holder.productNameTxt.setText(product);

		/*If a product exists in shared preferences then set heart_red drawable
		 * and set a tag*/
        if (checkFavoriteItem(product)) {
            holder.favoriteImg.setImageResource(R.drawable.fav_add);
            holder.favoriteImg.setTag("red");
        } else {
            holder.favoriteImg.setImageResource(R.drawable.fav_remove);
            holder.favoriteImg.setTag("grey");
        }

        return convertView;
    }

    /*Checks whether a particular product exists in SharedPreferences*/
    public boolean checkFavoriteItem(String checkProduct) {
        boolean check = false;
        List<String> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (String product : favorites) {
                if (product.equals(checkProduct)) {
                    check = true;
                    break;
                }
            }
        }
        return check;
    }

    @Override
    public void add(String product) {
        super.add(product);
        products.add(product);
        notifyDataSetChanged();
    }

    @Override
    public void remove(String product) {
        super.remove(product);
        products.remove(product);
        notifyDataSetChanged();
    }
}