package mandiri.finance.faith;

import java.util.ArrayList;
import mandiri.finance.faith.ListSurveyActivity.SearchResults;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter implements Filterable {
    
    private Activity activity;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    private ItemFilter mFilter = new ItemFilter();
    private String custType = "";
    private ArrayList<SearchResults> items = null;
	private ArrayList<SearchResults> filteredData = null;
	private DataSource datasource;
	private SQLiteDatabase db;

    public LazyAdapter(Activity a, ArrayList<SearchResults> results) {
    	activity = a;
        this.items = results;
        this.filteredData = results ;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
		datasource = new DataSource(a);
		db = datasource.getWritableDatabase();
    }

    public int getCount() {
        return filteredData.size();
    }

    public Object getItem(int position) {
    	return filteredData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    static class ViewHolder{
        TextView headerTextHolder;
        TextView detailTextHolder;
        TextView prospectDateHolder;
        ImageView thumbImageHolder;
        TextView customerIDHolder;
    }
    
    @Override    
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	View vi=convertView;
    	String strProspectDate;
        
    	if (convertView == null) {


    		vi = inflater.inflate(R.layout.survey_item, null);

            holder = new ViewHolder();
            holder.headerTextHolder = (TextView)vi.findViewById(R.id.textHeader);
            holder.detailTextHolder = (TextView)vi.findViewById(R.id.textDetail);
            holder.prospectDateHolder = (TextView)vi.findViewById(R.id.textDate);
            holder.thumbImageHolder = (ImageView)vi.findViewById(R.id.image);
            holder.customerIDHolder = (TextView)vi.findViewById(R.id.textCustomerID);
                        
            vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag();
        }

//        try{
//			Cursor cs1 = db.rawQuery("SELECT infotext FROM infodetail ", null);
//
//			try {
//				if (cs1.moveToFirst()) {
//					custType = cs1.getString(0);
//					Log.d("custType", "getView: "+ custType);
//				}
//			} finally {
//				cs1.close();
//			}
//
//			if (custType.equals("New")){
//				holder.thumbImageHolder.setVisibility(View.GONE);
//				holder.headerTextHolder.setTextColor(Color.GREEN);
//			} else {
//				holder.thumbImageHolder.setVisibility(View.VISIBLE);
//				holder.headerTextHolder.setTextColor(Color.BLUE);
//			}
//
//
//
//		}catch (Exception e){
//			e.printStackTrace();
//		}

    	strProspectDate = filteredData.get(position).getProspectDate().substring(6, 8) + "/" + 
			filteredData.get(position).getProspectDate().substring(4, 6) + "/" +
			filteredData.get(position).getProspectDate().substring(0, 4) ;
    	
    	holder.headerTextHolder.setText(filteredData.get(position).getHeader());
    	holder.detailTextHolder.setText(filteredData.get(position).getDetail());
    	holder.prospectDateHolder.setText("Prospect Date : " + strProspectDate);
    	imageLoader.DisplayImage(filteredData.get(position).getURL(), holder.thumbImageHolder, null);
    	holder.customerIDHolder.setText(filteredData.get(position).getCustomerID());

    	vi.setClickable(true);
    	vi.setLongClickable(true);
    	
    	vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				RelativeLayout rl = (RelativeLayout) v;
				
				TextView tvCustName = (TextView) rl.getChildAt(1);
				TextView tvCustAddress = (TextView) rl.getChildAt(2);
				TextView tvCustID = (TextView) rl.getChildAt(4);
				
				String custName = tvCustName.getText().toString();
				String custAddress = tvCustAddress.getText().toString();
				String custID = tvCustID.getText().toString();

						
				LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
				
		        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
		        	buildLocationAlert();
		            return;
		        }
		        				
		        Intent myintent = new Intent(activity,SurveyActivity.class);
      	   		myintent.putExtra("custID", custID);
          	   	myintent.putExtra("custName", custName);
          	 	myintent.putExtra("custAddress", custAddress);
          	   	activity.startActivity(myintent);
          	   	return;
            }

        });

    	        
        return vi;
    }
    
    private void buildLocationAlert() {
	    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    builder.setIcon(android.R.drawable.ic_dialog_info);
	    builder.setTitle("Confirmation");
	    builder.setMessage("Location service not active, activated now ?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	            	   Intent myintent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	            	   activity.startActivity(myintent);	            	   
	            	   return;
	               }
	           })
	           .setNegativeButton("No", new DialogInterface.OnClickListener() {
	               public void onClick(final DialogInterface dialog, final int id) {
	                   dialog.cancel();
	                   return;
	               }	
	           });
	    final AlertDialog alert = builder.create();
	    alert.show();
	}
    
    
	public Filter getFilter() {
		return mFilter;
		}

		private class ItemFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {

			constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if(constraint != null && constraint.toString().length() > 0)
            {
                ArrayList<SearchResults> filt = new ArrayList<SearchResults>();
                ArrayList<SearchResults> lItems = new ArrayList<SearchResults>();
                synchronized (this)
                {
                    lItems.addAll(items);
                }
                for(int i = 0, l = lItems.size(); i < l; i++)
                {
                	SearchResults m = lItems.get(i);
                    if(m.getHeader().toLowerCase().contains(constraint))
                        filt.add(m);
                }
                result.count = filt.size();
                result.values = filt;
                
            }
            else
            {
                synchronized(this)
                {                	
                    result.values = items;
                    result.count = items.size();
                }
            }

			return result;
		}

		 @SuppressWarnings("unchecked")
	        @Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	            filteredData = (ArrayList<SearchResults>)results.values;
	            notifyDataSetChanged();
	        }

	}
		
}