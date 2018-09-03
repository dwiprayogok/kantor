package mandiri.finance.faith;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioButton;
import android.widget.TextView;

public class RadioBtn extends LinearLayout {
	String tag = RadioBtn.class.getName();
	TextView label;
	RadioButton[] rb;
	RadioGroup radiogroup;
	LinearLayout ll;
	String title;
	int satu =1;
	public RadioBtn(Context context,String labelText,String optionsName, String optionsID) {
		super(context);
		
		ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
		    LayoutParams.MATCH_PARENT,
		    LayoutParams.WRAP_CONTENT);
	 	 
	 	ll.setLayoutParams(params);
	 	ll.setPadding(4, 10, 4, 0);
	 	
		label = new TextView(context);
		label.setText(labelText);
		label.setTextColor(getResources().getColor(R.color.label_color));
		
		radiogroup = new RadioGroup(context);
		radiogroup.setOrientation(RadioGroup.HORIZONTAL);
		
		String[]optsName = optionsName.split("\\|");
    	String[]optsID = optionsID.split("\\|");
		Log.d("cek optionsID", "RadioBtn: "+optionsID);
		Log.d("cek optionsName", "RadioBtn: "+optionsName);
		
		List<String> list = Arrays.asList(optionsName.split("\\|"));
		String[]opts = new String[list.size()];

		list.toArray(opts);
		
		rb = new RadioButton[list.size()];
		
		for (int i = 0; i < list.size(); i++) {
			rb[i] = new RadioButton(context);
			//rb[i].setText(optsName[i] + "               ");
			rb[i].setText(optsName[i]);
			Log.d("cek", "RadioBtn: "+optsName[i].toString());
			rb[i].setId(Integer.valueOf(optsID[i]));
			rb[i].setTextColor(getResources().getColor(R.color.label_color));
			radiogroup.addView(rb[i]);
		}
						
		title = label.getText().toString();


		if (optsName.equals("Tidak")){
			//radiogroup.check(1);
			radiogroup.check(0);
		}
		else {
			//radiogroup.check(((RadioButton)radiogroup.getChildAt(0)).getId());
			radiogroup.check(((RadioButton)radiogroup.getChildAt(1)).getId());
		}



						
		ll.addView(label);
		ll.addView(radiogroup);
		
		this.addView(ll);		
	}
	
	public String getLabel()
	{
		return label.getText().toString();
	}

	public String getValueradiobtton()	{
		String selectedValue = "";
		for(int i=0; i<radiogroup.getChildCount(); i++) {
            RadioButton rbt = (RadioButton) radiogroup.getChildAt(i);
            if(rbt.isChecked()) {
            	selectedValue = (String) String.valueOf(rbt.getId());
				break;
            }
		}
		return selectedValue.trim();
		
	}
	
}