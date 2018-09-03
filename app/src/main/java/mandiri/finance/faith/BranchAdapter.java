package mandiri.finance.faith;

import mandiri.finance.faith.LoginActivity.SearchResults;
import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BranchAdapter extends BaseAdapter{
    
    private Context context;
    private LayoutInflater inflater = null;
	private ArrayList<SearchResults> filteredData = null;
	
    public BranchAdapter(Context c, ArrayList<SearchResults> results) {
    	context = c;
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
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
    
    public View getCustomView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	View vi=convertView;
        
    	if (convertView == null) {
    		vi = inflater.inflate(R.layout.branch, null);

            holder = new ViewHolder();
            holder.nameHolder = (TextView)vi.findViewById(R.id.tvBranchName);
            holder.idHolder = (TextView)vi.findViewById(R.id.tvBranchID);
                        
            vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag();
        }
    		    	
    	holder.nameHolder.setText(filteredData.get(position).getName());
    	holder.idHolder.setText(filteredData.get(position).getID());
    	
    	return vi;
    }
}


