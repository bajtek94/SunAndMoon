package com.politechnika.app.astroweather.fragments;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.politechnika.app.astroweather.R;
import com.politechnika.app.astroweather.service.ProductListAdapter;
import com.politechnika.app.astroweather.service.SharedPreference;

public class ProductListFragment  extends Fragment implements
        OnItemClickListener, OnItemLongClickListener {

    public static final String ARG_ITEM_ID = "product_list";

    Activity activity;
    ListView productListView;
    List<String> products;
    ProductListAdapter productListAdapter;
    SharedPreference sharedPreference;

    EditText cityNameEditText;
    ImageButton addCityButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        sharedPreference = new SharedPreference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container,
                false);
        findViewsById(view);

        cityNameEditText = (EditText) view.findViewById(R.id.cityNameEditText);
        addCityButton = (ImageButton) view.findViewById(R.id.addCityButton);

        addCityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(cityNameEditText.getText().toString())  || cityNameEditText.getText().toString().length() != 0){
                    String cityName = cityNameEditText.getText().toString();
                    if (!products.contains(cityName)){
                        products.add(cityName);
                        productListAdapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(activity, "City already added.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        setProducts();

        productListAdapter = new ProductListAdapter(activity, products);
        productListView.setAdapter(productListAdapter);
        productListView.setOnItemClickListener(this);
        productListView.setOnItemLongClickListener(this);
        return view;
    }

    private void setProducts() {
        products = new ArrayList<String>();
        List<String> listOfFavourities = sharedPreference.getFavorites(getContext());
        if (listOfFavourities != null && listOfFavourities.size() > 0){
            for(String favourite : listOfFavourities){
                products.add(favourite);
            }
        }
    }

    private void findViewsById(View view) {
        productListView = (ListView) view.findViewById(R.id.list_product);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        String product = (String) parent.getItemAtPosition(position);
        Toast.makeText(activity, product.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                   int position, long arg3) {
        ImageView button = (ImageView) view.findViewById(R.id.imgbtn_favorite);

        String tag = button.getTag().toString();
        if (tag.equalsIgnoreCase("grey")) {
            sharedPreference.addFavorite(activity, products.get(position));
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.add_favr),
                    Toast.LENGTH_SHORT).show();

            button.setTag("red");
            button.setImageResource(R.drawable.fav_add);
        } else {
            sharedPreference.removeFavorite(activity, products.get(position));
            button.setTag("grey");
            button.setImageResource(R.drawable.fav_remove);
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.remove_favr),
                    Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    @Override
    public void onResume() {
        getActivity().setTitle(R.string.app_name);
        //getActivity().getActionBar().setTitle(R.string.app_name);
        super.onResume();
    }
}