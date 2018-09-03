package mandiri.finance.faith;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mandiri.finance.faith.Model.ZipCode;

public class PickOneSupplier extends LinearLayout {
	private TextView label;
	private PickOneAdapter aa;
	private LinearLayout ll;
	private String title;
	private Button btnShow;
	private Dialog dialog;
	private ListView lv;
	private EditText searchText;
	private String val;
	private DataSource datasource;
    private SQLiteDatabase db;
    private ArrayList<SearchResults> results = new ArrayList<SearchResults>();
	ZipCode zipCode = new ZipCode();
	public PickOneSupplier(Context context,String labelText, String tableName, String fieldName, String fieldID) {
		super(context);
		
		datasource = new DataSource(context);
        db = datasource.getWritableDatabase();
		
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		    LayoutParams.MATCH_PARENT,
		    LayoutParams.MATCH_PARENT);
	 	 
	 	ll.setLayoutParams(params);
	 	ll.setPadding(4, 10, 4, 0);
	 		 	
		label = new TextView(context);
		label.setText(labelText);
		label.setTextColor(getResources().getColor(R.color.label_color));
		
		btnShow = new Button(context, null, android.R.attr.buttonStyleSmall);
		dialog = new Dialog(context);
		
		val = null;
		btnShow.setText("Select One");
		btnShow.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		btnShow.setOnClickListener(showClickListener);
		title = label.getText().toString();
		
		LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = li.inflate(R.layout.pick_one_list, null, false);
        
        dialog.setContentView(v);
		dialog.setTitle(title);
	    dialog.setCancelable(true);
        
		searchText = (EditText) dialog.findViewById(R.id.searchText);
	    lv = (ListView) dialog.findViewById(R.id.lvPickOne);
	    
	    String sqlqueryName = "select " + fieldName + " from " + tableName + " order by " + fieldName;
	    String sqlqueryID = "select " + fieldID + " from " + tableName + " order by " + fieldName;
	    
    	List<String> lableName = datasource.getAllLabels(db, sqlqueryName);
    	List<String> lableID = datasource.getAllLabels(db, sqlqueryID);
	    	    
	    String[]optsName = lableName.toArray(new String[lableName.size()]);
    	String[]optsID = lableID.toArray(new String[lableID.size()]);
	    
	    for (int i = 0; i < optsName.length; i++) {
	    	SearchResults sr1 = new SearchResults();
	        sr1.setName(optsName[i]);
	        sr1.setID(optsID[i]);
	        results.add(sr1);
	    }
	    
	    if (aa != null){ 	    	
    		aa.notifyDataSetInvalidated();
    		aa = null;
    	}
	    
	    aa = new PickOneAdapter(context, results);
		lv.setAdapter(aa);
		lv.setTextFilterEnabled(true);
		lv.setCacheColorHint(0);
		lv.setFocusable(false);
		
		if (db != null){
    		if (db.isOpen()){
        		db.close();	
        	}
    	}
				
		searchText.addTextChangedListener(new TextWatcher() {
			 
		    @Override
		    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
		        aa.getFilter().filter(cs);
		    }
		 
		    @Override
		    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
		            int arg3) {			    	
		    }

			@Override
			public void afterTextChanged(Editable arg0) {					
			}
		});
		
		ll.addView(label);
		ll.addView(btnShow);
	    
		this.addView(ll);
		
	}




	
	private class SearchResults {
	   	 private String name = "";
	   	 private String id = "";
	   	 
	   	 public void setName(String name) {
	   		 this.name = name;
	   	 }
	
	   	 public String getName() {
	   		 return name;
	   	 }
	
	   	 public void setID(String id) {
	   		 this.id = id;
	   	 }
	
	   	 public String getID() {
	   		 return id;
	   	 }
	   	 
	}
	
	private OnClickListener showClickListener = new OnClickListener() {
        public void onClick(View v) {
        	searchText.setText("");
        	dialog.show();        	
        }
    };
    
    public String getLabel()
	{
		return label.getText().toString();
	}
	
	public String getValue()
	{
		return (String) val;
	}
	
	public void setErrorMsg(String v)
	{
		btnShow.setError(v);		
	}	
	
	private class PickOneAdapter extends BaseAdapter implements Filterable {
	    
	    private Context context;
	    private LayoutInflater inflater = null;
	    private ItemFilter mFilter = new ItemFilter();
	    private ArrayList<SearchResults> items = null;
		private ArrayList<SearchResults> filteredData = null;
		
		private PickOneAdapter(Context c, ArrayList<SearchResults> results) {
	    	context = c;
	        this.items = results;
	        this.filteredData = results;
	    	inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
	    
	    private class ViewHolder{
	        TextView nameHolder;
	        TextView idHolder;
	    }
	    
	    @Override    
	    public View getView(int position, View convertView, ViewGroup parent) {
	    	ViewHolder holder;
	    	View vi=convertView;
	        
	    	if (convertView == null) {
	    		vi = inflater.inflate(R.layout.pick_one, null);

	            holder = new ViewHolder();
	            holder.nameHolder = (TextView)vi.findViewById(R.id.tvName);
	            holder.idHolder = (TextView)vi.findViewById(R.id.tvID);
	                        
	            vi.setTag(holder);
	        } else {
	        	holder = (ViewHolder) vi.getTag();
	        }
	    		    	
	    	holder.nameHolder.setText(filteredData.get(position).getName());
	    	holder.idHolder.setText(filteredData.get(position).getID());

	    	vi.setClickable(true);
	    	    	
	    	vi.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	            	LinearLayout ll = (LinearLayout) v;
					
					TextView tvName = (TextView) ll.getChildAt(0);
					TextView tvID = (TextView) ll.getChildAt(1);
					
					String name = tvName.getText().toString();
						String id = tvID.getText().toString();
					
					btnShow.setError(null);
					btnShow.setText(name);
					
					val = id;
					//val = name;
					dialog.dismiss();					
	            }

	        });
	    	        
	        return vi;
	    }

		@Override
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
	                    if(m.getName().toLowerCase().contains(constraint))
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


	
}