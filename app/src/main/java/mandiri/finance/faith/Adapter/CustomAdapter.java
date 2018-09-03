package mandiri.finance.faith.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mandiri.finance.faith.DataSource;
import mandiri.finance.faith.PickOneChoice;
import mandiri.finance.faith.PickOneSupplier;
import mandiri.finance.faith.R;

/**
 * Created by dwi.prayogo on 11/9/2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] countryNames;
    LayoutInflater inflter;
    private ArrayList<String> items = null;
    private ArrayList<String> filteredData = null;
    private DataSource datasource;
    private SQLiteDatabase db;
    //private ItemFilter mFilter = new ItemFilter();
    public CustomAdapter(Context applicationContext,ArrayList<String> results) {
        this.context = applicationContext;
        this.countryNames = countryNames;
        this.items = results;
        this.filteredData = results;
    }

    @Override
    public int getCount() {
        //return countryNames.length;
        return filteredData.size();
    }

    @Override
    public Object getItem(int i) {
        //return null;
        return filteredData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
        //return 0;
    }


    public static class ViewHolder {
        public int position;
        public TextView textView2;
        public EditText edt_prod;



    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        datasource = new DataSource(context);
        db = datasource.getWritableDatabase();
        if (view == null) {
            inflter = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflter.inflate(R.layout.custom_spinner, null);
            viewHolder = new ViewHolder();
            viewHolder.textView2 = (TextView) view.findViewById(R.id.textView);
            viewHolder.edt_prod = (EditText) view.findViewById(R.id.edt_prod);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        

        viewHolder.textView2.setText(filteredData.toString());

        viewHolder.edt_prod.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                //aa.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });


//        ArrayAdapter<String> adapter;
//        adapter=new ArrayAdapter<String>(context, R.layout.spinner, filteredData);
//        viewHolder.textView2.setAdapter(adapter);


        return view;
    }

//    @Override
//    public Filter getFilter() {
//        return mFilter;
//    }
//
//    private class ItemFilter extends Filter {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//
//            constraint = constraint.toString().toLowerCase();
//            FilterResults result = new FilterResults();
//            if(constraint != null && constraint.toString().length() > 0)
//            {
//                ArrayList<SearchResults> filt = new ArrayList<SearchResults>();
//                ArrayList<SearchResults> lItems = new ArrayList<SearchResults>();
//                synchronized (this)
//                {
//                    lItems.addAll(items);
//                }
//                for(int i = 0, l = lItems.size(); i < l; i++)
//                {
//                    SearchResults m = lItems.get(i);
//                    if(m.getName().toLowerCase().contains(constraint))
//                        filt.add(m);
//                }
//                result.count = filt.size();
//                result.values = filt;
//
//            }
//            else
//            {
//                synchronized(this)
//                {
//                    result.values = items;
//                    result.count = items.size();
//                }
//            }
//
//            return result;
//        }
//
//        @SuppressWarnings("unchecked")
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            filteredData = (ArrayList<SearchResults>)results.values;
//            notifyDataSetChanged();
//        }
//
//    }
//    private class SearchResults {
//        private String name = "";
//        private String id = "";
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setID(String id) {
//            this.id = id;
//        }
//
//        public String getID() {
//            return id;
//        }
//
//    }
}




