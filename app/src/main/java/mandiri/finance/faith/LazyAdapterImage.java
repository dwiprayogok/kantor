package mandiri.finance.faith;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class LazyAdapterImage extends BaseAdapter {
    
	private Activity activity;
    private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    
    public LazyAdapterImage(Activity a, String[] d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    static class ViewHolder{
        ImageView imageHolder;        
    }
    
    @Override    
    public View getView(int position, View convertView, ViewGroup parent) {
    	ViewHolder holder;
    	View vi=convertView;
        
    	if (convertView == null) {
    		vi = inflater.inflate(R.layout.survey_image, null);

            holder = new ViewHolder();
            holder.imageHolder = (ImageView)vi.findViewById(R.id.surveyImg);
               
            vi.setTag(holder);
        } else {
        	holder = (ViewHolder) vi.getTag();
        }
    	
    	imageLoader.DisplayImage(data[position], holder.imageHolder, "image");
    	
    	vi.setClickable(false);    	        
        return vi;
    }
	
		
}