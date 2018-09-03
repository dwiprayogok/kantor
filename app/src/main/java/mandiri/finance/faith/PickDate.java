package mandiri.finance.faith;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PickDate extends LinearLayout {
	private TextView label;
	private LinearLayout ll;
	private  String val;
	private static int yearVal;
	private static int monthVal;
	private static int dayVal;
	private EditText ambiltanggal;
    private  Button bt;
	private SimpleDateFormat dateFormatter;
	Context mcontext;
	private DatePickerDialog fromDatePickerDialog,datePickerDialog;
	String date2 ;
    public PickDate(final Context context, String labelText) {			
    	super(context);
		mcontext = context;
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
		dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
		setDateTimeField();
		//val = null;
		ambiltanggal = new EditText(context);
		ambiltanggal.setHint("Input Tanggal Lahir");
		ambiltanggal.setClickable(true);
		ambiltanggal.setFocusable(false);
		ambiltanggal.setInputType(InputType.TYPE_NULL);
		ambiltanggal.requestFocus();
		ambiltanggal.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1));
		ambiltanggal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				datepicker();
			}
		});
//		bt = new Button(context, null, android.R.attr.buttonStyleSmall);
//		bt.setGravity(Gravity.LEFT|Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
//		//bt.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//		bt.setText("Select Date");
//
//        bt.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//				datepicker();
////            	DialogFragment newFragment = new DatePickerFragment();
////
////            	FragmentManager fm = ((Activity)context).getFragmentManager();
////            	newFragment.show(fm, "datePicker");
//            }
//
//        });
		
        ll.addView(label);
		ll.addView(ambiltanggal);
		//ll.addView(bt);
		
		this.addView(ll);
	}


//    public static class DatePickerFragment extends DialogFragment
//    implements DatePickerDialog.OnDateSetListener {
//
//	    public Dialog onCreateDialog(Bundle savedInstanceState) {
//		    // Use the current date as the default date in the picker
//		    final Calendar c = Calendar.getInstance();
//		    int year = c.get(Calendar.YEAR);
//		    int month = c.get(Calendar.MONTH);
//		    int day = c.get(Calendar.DAY_OF_MONTH);
//
//		    if(val == null){
//		    	return new DatePickerDialog(getActivity(), this, year, month, day);
//		    }else{
//		    	return new DatePickerDialog(getActivity(), this, yearVal, monthVal, dayVal);
//		    }
//
//	    }
//
//	    public void onDateSet(DatePicker view, int year, int month, int day) {
//	    	String strDay = String.valueOf(day);
//	    	String strMonth = String.valueOf(month + 1);
//			//String monthname=(String)android.text.format.DateFormat.format("MMMM", new Date());
//			//Log.d("cek", "onDateSet: "+monthname);
//
//			if(strDay.length()==1)
//	    		strDay = "0" + strDay;
//
//	    	if(strMonth.length()==1)
//	    		strMonth = "0" + strMonth;
//
//	        val = String.valueOf(year) + strMonth + strDay;
//			//val = String.valueOf(year) + monthname + strDay;
//	        bt.setText(strDay + "/" + strMonth + "/" + String.valueOf(year));
//			//bt.setText(strDay + "/" + monthname + "/" + String.valueOf(year));
//
//	        yearVal = year;
//	        monthVal = month;
//	        dayVal = day;
//	    }
//    }






	private void setDateTimeField() {
		Calendar newCalendar = Calendar.getInstance();
		fromDatePickerDialog = new DatePickerDialog(mcontext, new DatePickerDialog.OnDateSetListener() {

			public void onDateSet(DatePicker view, int dayOfMonth, int monthOfYear, int year) {
				Calendar newDate = Calendar.getInstance();
				newDate.set(dayOfMonth, monthOfYear, year);
				ambiltanggal.setText(dateFormatter.format(newDate.getTime()));
			}

		},newCalendar.get(Calendar.DAY_OF_MONTH), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.YEAR));


	}

	private void datepicker(){

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR); // current year
		int mMonth = c.get(Calendar.MONTH); // current month
		int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
		datePickerDialog = new DatePickerDialog(mcontext,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						ambiltanggal.setText(dayOfMonth + "/"
								+ (monthOfYear + 1) + "/" + year);
						date2= ambiltanggal.getText().toString();
						Log.d("cek", "onDateSet: " + date2);

					}
				}, mYear, mMonth, mDay);
		//val = String.valueOf(mDay) + mMonth + mYear;
		//val= ambiltanggal.getText().toString();
		datePickerDialog.show();

	}



    public void setErrorMsg(String v)
	{
		ambiltanggal.setError(v);
	}
	
	
    public String getLabel()
	{
    	return label.getText().toString();
	}
	
	public String getValue()
	{
		//return (String) val;
		return (String ) date2;
	}
	
}