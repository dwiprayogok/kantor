package mandiri.finance.faith;

import java.util.ArrayList;
import java.util.List;

import mandiri.finance.faith.ListCollectionActivity.SearchResults;
import mandiri.finance.faith.Model.Model_Customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LazyAdapterCollection extends BaseAdapter implements Filterable {
    
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ItemFilter mFilter = new ItemFilter();
    private ArrayList<SearchResults> items = null;
	private ArrayList<SearchResults> filteredData = null;
	int posisinya;

	
    public LazyAdapterCollection(Activity a, ArrayList<SearchResults> results) {
    	activity = a;
        this.items = results;
        this.filteredData = results ;
    	inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        TextView dueDateHolder;
        TextView contractNoHolder;        
        TextView taskDateHolder;
        TextView insNoHolder;
        TextView dueDateInfoHolder;
        TextView insAmountHolder;
		TextView latitude12;
		TextView longitude12;
		TextView overdue23;
		TextView Amount;
    }
    
    @Override    
    public View getView(final int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	View vi=convertView;
    	String strDueDate;
		String Amount ;
		String Amount2 ;
		String overdue;

    	
    	if (convertView == null) {
    		vi = inflater.inflate(R.layout.collection_item, null);

            holder = new ViewHolder();
            holder.headerTextHolder = (TextView)vi.findViewById(R.id.textHeader);
            holder.contractNoHolder = (TextView)vi.findViewById(R.id.textcontractNo);
            holder.detailTextHolder = (TextView)vi.findViewById(R.id.textDetail);
            holder.dueDateInfoHolder = (TextView)vi.findViewById(R.id.textDate);
            holder.taskDateHolder = (TextView)vi.findViewById(R.id.textTaskDate);
            holder.insNoHolder = (TextView)vi.findViewById(R.id.textInsNo);
            holder.dueDateHolder = (TextView)vi.findViewById(R.id.textDueDate);
            holder.insAmountHolder = (TextView)vi.findViewById(R.id.textInsAmount);
			//tambahan
			holder.overdue23 = (TextView)vi.findViewById(R.id.textOverdue);
			holder.Amount = (TextView)vi.findViewById(R.id.textAmount);


                                    
            vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag();
        }

    	strDueDate = filteredData.get(position).getDueDate().substring(6, 8) + "/" + 
    		filteredData.get(position).getDueDate().substring(4, 6) + "/" +
    		filteredData.get(position).getDueDate().substring(0, 4) ;
		Amount = filteredData.get(position).getInstallmentAmount();
		Amount2 = filteredData.get(position).getAmount();
		overdue = filteredData.get(position).getOverdue2();

    	holder.headerTextHolder.setText(filteredData.get(position).getHeader());
    	holder.contractNoHolder.setText(filteredData.get(position).getcontractNo());
    	holder.detailTextHolder.setText(filteredData.get(position).getDetail());
    	holder.dueDateInfoHolder.setText("Due Date : " + strDueDate);
    	holder.taskDateHolder.setText(filteredData.get(position).getTaskDate());
    	holder.insNoHolder.setText(filteredData.get(position).getInstallmentNo());
    	holder.dueDateHolder.setText(filteredData.get(position).getDueDate());
    	//holder.insAmountHolder.setText("Amount : "+" Rp. " +  filteredData.get(position).getInstallmentAmount());
		holder.insAmountHolder.setText("Amount : "+" Rp. " +  Amount);
		//holder.overdue23.setText("OverDue : " + filteredData.get(position).getOverdue2() + " hari ");
		holder.overdue23.setText("OverDue : " + overdue + " hari ");
		holder.Amount.setText("Amount : "+" Rp. " +  Amount2);

		//holder.latitude12.setText(filteredData.get(position).getLatitude());
		//holder.longitude12.setText(filteredData.get(position).getLongitude());
		//holder.latitude12.setText(lati.toString());
		//holder.longitude12.setText(longi.toString());

		Log.d("lihat", "getView: "+"\n"
				+"header : "+ filteredData.get(position).getHeader()+"\n"
				+"due_datenya : "+ filteredData.get(position).getDueDate()+"\n"
				+"amountnya : "+ filteredData.get(position).getInstallmentAmount()+"\n"
				+"amountnya : "+ filteredData.get(position).getAmount()+"\n"
				+"overduenya :"+ filteredData.get(position).getOverdue2());

		Log.d("lihatlatlong", "getView: "+filteredData.get(position).getLatitude() + filteredData.get(position).getLongitude());

    	
    	vi.setLongClickable(true);
    	vi.setClickable(true);
    	
    	vi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
				posisinya = position;
				RelativeLayout rl = (RelativeLayout) v;
				TextView tvCustName = (TextView) rl.getChildAt(0);
				TextView tvcontractNo = (TextView) rl.getChildAt(1);				
				TextView tvCustAddress = (TextView) rl.getChildAt(2);
				TextView tvTaskDate = (TextView) rl.getChildAt(4);
				TextView tvInsNo = (TextView) rl.getChildAt(5);
				TextView tvDueDate = (TextView) rl.getChildAt(6);
				TextView tvInsAmount = (TextView) rl.getChildAt(7);
				TextView tvoverdue = (TextView) rl.getChildAt(8);
				//TextView tvLat = (TextView) rl.getChildAt(8);
				//TextView tvLng = (TextView) rl.getChildAt(9);

				String newamount = filteredData.get(position).getInstallmentAmount();

				String custName = tvCustName.getText().toString();
				String contractNo = tvcontractNo.getText().toString();
				String custAddress = tvCustAddress.getText().toString();
				String dueDate = tvDueDate.getText().toString();
				String taskDate = tvTaskDate.getText().toString();
				String insNo = tvInsNo.getText().toString();
				String insAmount = tvInsAmount.getText().toString();
				String inoverdue = tvoverdue.getText().toString();

				String lati2 = filteredData.get(posisinya).getLatitude().toString();
				String longi2 = filteredData.get(posisinya).getLongitude().toString();
				Log.d("lihat lat Lngnya ", "onClick: "+"latnya: "+ lati2 +"longinya : " + longi2);

				//String lati2 = tvLat.getText().toString();
				//String longi2 = tvLng.getText().toString();

						
				LocationManager locationManager = (LocationManager) activity.getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
				
		        if ( !locationManager.isProviderEnabled( LocationManager.NETWORK_PROVIDER ) ) {
		        	buildLocationAlert();
		            return;
		        }

		        Intent myintent = new Intent(activity,CollectionActivity.class);
      	   		myintent.putExtra("contractNo", contractNo);
          	   	myintent.putExtra("custName", custName);
          	 	myintent.putExtra("custAddress", custAddress);
          	 	myintent.putExtra("dueDate", dueDate);
          	 	myintent.putExtra("taskDate", taskDate);
          	 	myintent.putExtra("insNo", insNo);
          	 	//myintent.putExtra("insAmount", insAmount);
				myintent.putExtra("insAmount", newamount);
				myintent.putExtra("latitudenya", lati2);
				myintent.putExtra("longitudenya", longi2);
				myintent.putExtra("OD", inoverdue);

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