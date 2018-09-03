package mandiri.finance.faith;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.EditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.widget.Toast;

public class EditBox extends LinearLayout {
	TextView label;
	EditText txtBox;
	LinearLayout ll;
	Integer maxlength;
	private String blocksimbol = "'*^`~+|/<>";

	public EditBox(Context context,String labelText,String initialText,String hintText,String maxLength) {
		super(context);
		
		maxlength = Integer.valueOf(maxLength);
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


		txtBox = new EditText(context);	
		txtBox.setHint(hintText);
		txtBox.setText(initialText);
		txtBox.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
				
		ll.addView(label);
		ll.addView(txtBox);
		
		this.addView(ll);
	}

	public EditBox(Context context, AttributeSet attrs) {
		super(context, attrs);		
	}
	
	public void makeChar()
	{
		txtBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		InputFilter filter = new InputFilter() { 
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { 
            for (int i = start; i < end; i++) {
				Log.d("cek", "filter: "+start);
				if ((!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i)))
						|| dend >= maxlength) {
                	return "";
                }
            } 
            	return null; 
            }
		};

		txtBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().contains("'")){
					txtBox.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		txtBox.setFilters(new InputFilter[]{filter});
		txtBox.setFilters(new InputFilter[] {
				new InputFilter.LengthFilter(500)
		});


		InputFilter filter1 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source != null && blocksimbol.contains((""+source))){
					return "";
				}
				return null;
			}
		};
		txtBox.setFilters(new InputFilter[]{filter1});
		
		txtBox.addTextChangedListener(new TextWatcher(){
	        private String current = "";
	        
	        @Override
	        public void afterTextChanged(Editable s) {
	        	String userInput=s.toString();
	        	
	        	if(!userInput.toString().equals(current)){
	        		txtBox.removeTextChangedListener(this);

	        		String cleanString = userInput;

	        		if(cleanString.length()>0){		        		
		        		current = cleanString;
		        		txtBox.setText(cleanString.toUpperCase());
		        		txtBox.setSelection(cleanString.length());
	        		}else{
	        			txtBox.setText(cleanString);
		        		txtBox.setSelection(cleanString.length());
	        		}
	        		
	        		txtBox.addTextChangedListener(this);
        	    }
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start,
	                int count, int after) {
	        }
	        @Override
	        public void onTextChanged(CharSequence s, int start,
	                int before, int count) {
	        }
	    });
	}

	public void makeChar2()
	{

		txtBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					Log.d("cek", "filter: "+start);
					if ((!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i)))
							|| dend >= maxlength) {
						return "";
					}
				}
				return null;
			}
		};

		txtBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() < 3){
					txtBox.setError("Kolom input nama Harus diisi lebih dari 3 karakter");
				}
				if (s.toString().contains("'")){
					txtBox.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		txtBox.setFilters(new InputFilter[]{filter});
		txtBox.setFilters(new InputFilter[] {
				new InputFilter.LengthFilter(500)
		});


		InputFilter filter1 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source != null && blocksimbol.contains((""+source))){
					return "";
				}
				return null;
			}
		};
		txtBox.setFilters(new InputFilter[]{filter1});

		txtBox.addTextChangedListener(new TextWatcher(){
			private String current = "";

			@Override
			public void afterTextChanged(Editable s) {
				String userInput=s.toString();

				if(!userInput.toString().equals(current)){
					txtBox.removeTextChangedListener(this);

					String cleanString = userInput;

					if(cleanString.length()>0){
						current = cleanString;
						txtBox.setText(cleanString.toUpperCase());
						txtBox.setSelection(cleanString.length());
					}else{
						txtBox.setText(cleanString);
						txtBox.setSelection(cleanString.length());
					}

					txtBox.addTextChangedListener(this);
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
			}
		});
	}


	public void makeEmail()
	{
		txtBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
				| InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				for (int i = start; i < end; i++) {
					if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
						if((source.charAt(i) != '.' && source.charAt(i) != ',' && source.charAt(i) != '-' &&
								source.charAt(i) != '/' && source.charAt(i) != '(' && source.charAt(i) != ')'
								&& source.charAt(i) != ':' && source.charAt(i) != '@' ) && source.charAt(i) != '_'
								|| dend >= maxlength)
						{
							return "";
						}
					}
				}
				return null;
			}
		};
		txtBox.setFilters(new InputFilter[]{filter});


		String email = txtBox.getText().toString();

			if (email.equals("")){
				txtBox.clearFocus();
			}else{
				if(isValidEmail(email)){
					Toast.makeText(getContext(), "Email is valid", Toast.LENGTH_SHORT).show();
					txtBox.clearFocus();
				}else{
					//Toast.makeText(mContext, "Email is invalid", Toast.LENGTH_SHORT).show();
					txtBox.setError("Email is invalid");
					txtBox.requestFocus();
				}
			}


		txtBox.addTextChangedListener(new TextWatcher()
		{
			private String current = "";

			@Override
			public void afterTextChanged(Editable s) {
				String userInput=s.toString();

				if(!userInput.toString().equals(current)){
					txtBox.removeTextChangedListener(this);

					String cleanString = userInput;

					if(cleanString.length()>0){
						current = cleanString;
						txtBox.setText(cleanString.toUpperCase());
						txtBox.setSelection(cleanString.length());
					}else{
						txtBox.setText(cleanString);
						txtBox.setSelection(cleanString.length());
					}

					txtBox.addTextChangedListener(this);
				}


				if (userInput.equals("")){
					txtBox.clearFocus();
				}else{
					if(isValidEmail(userInput)){
						Toast.makeText(getContext(), "Email is valid", Toast.LENGTH_SHORT).show();
						txtBox.clearFocus();
					}else{
						//Toast.makeText(mContext, "Email is invalid", Toast.LENGTH_SHORT).show();
						txtBox.setError("Email is invalid");
						txtBox.requestFocus();
					}
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start,
										  int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start,
									  int before, int count) {
			}
		});
	}
	public static boolean isValidEmail(String email)
	{
		String expression = "^[\\w\\.]+@([\\w]+\\.)+[A-Z]{2,7}$";

		CharSequence inputString = email;
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputString);
		if (matcher.matches())
		{
			return true;
		}
		else{
			return false;
		}
	}

	public void makeNumber()
	{
		txtBox.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);
		
		InputFilter filter = new InputFilter() { 
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        	            	
            for (int i = start; i < end; i++) {	            	
            	if (!Character.isDigit(source.charAt(i)) || dend >= maxlength) {
                	return ""; 
                }
            }	            
        	return null; 
            }
		};
		txtBox.setFilters(new InputFilter[]{filter});
	}

	public void makeNumber2()
	{
		txtBox.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);

		InputFilter filter = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

				for (int i = start; i < end; i++) {
					if (!Character.isDigit(source.charAt(i)) || dend >= maxlength) {
						return "";
					}
				}
				return null;
			}
		};

		txtBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.length() < 16){
					txtBox.setError("Kolom input Identitas tidak boleh kurang dari 16 digit");
				}

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});


		txtBox.setFilters(new InputFilter[]{filter});
	}


	public void makeDecimal()
	{
		txtBox.setRawInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		DigitsKeyListener dkl = new DigitsKeyListener(true,true);
		txtBox.setKeyListener(dkl);
		
		InputFilter filter = new InputFilter() { 
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        	            	
            for (int i = start; i < end; i++) {	            	
                if (dend >= maxlength) {
                	return ""; 
                }
            }	            
        	return null; 
            }
		};
		txtBox.setFilters(new InputFilter[]{filter});
		
		txtBox.addTextChangedListener(new TextWatcher(){
	        private String current = "";
	        
	        @Override
	        public void afterTextChanged(Editable s) {
	        	String userInput=s.toString();
	        	
	        	if(!userInput.toString().equals(current)){
	        		txtBox.removeTextChangedListener(this);

	        		String cleanString = userInput.replaceAll("[,]", "");

	        		if(cleanString.length()>0){
	        			double parsed = Double.parseDouble(cleanString);
		        		String formated = DecimalFormat.getNumberInstance(Locale.ENGLISH).format(parsed);
		        		
		        		current = formated;
		        		txtBox.setText(formated);
		        		txtBox.setSelection(formated.length());
	        		}else{
	        			txtBox.setText(cleanString);
		        		txtBox.setSelection(cleanString.length());
	        		}
	        		
	        		txtBox.addTextChangedListener(this);
        	    }
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start,
	                int count, int after) {
	        }
	        @Override
	        public void onTextChanged(CharSequence s, int start,
	                int before, int count) {
	        }
	    });
		
		
	}
	
	public void makeMultiLine()
	{
		txtBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
		
		txtBox.setLines(3);
		txtBox.setMaxLines(3);
		txtBox.setGravity(Gravity.TOP);

		
		InputFilter filter = new InputFilter() { 
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) { 
            for (int i = start; i < end; i++) {
                if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.isSpaceChar(source.charAt(i))) {
            		if((source.charAt(i) != '.' && source.charAt(i) != ',' && source.charAt(i) != '-' && 
            			source.charAt(i) != '/' && source.charAt(i) != '(' && source.charAt(i) != ')' && source.charAt(i) != ':') || dend >= maxlength)
            		{
            			return ""; 
            		}
                }
            } 
            	return null; 
            }
		};
		txtBox.setFilters(new InputFilter[]{filter});
		txtBox.setFilters(new InputFilter[] {
				new InputFilter.LengthFilter(500)
		});


		txtBox.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().contains("'")){
					txtBox.setError("Harap Tidak Menggunakan Tanda Petik satu (')");
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		InputFilter filter1 = new InputFilter() {
			@Override
			public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
				if (source != null && blocksimbol.contains((""+source))){
					return "";
				}
				return null;
			}
		};
		txtBox.setFilters(new InputFilter[]{filter1});


		txtBox.addTextChangedListener(new TextWatcher(){
	        private String current = "";
	        
	        @Override
	        public void afterTextChanged(Editable s) {
	        	String userInput=s.toString();
	        	
	        	if(!userInput.toString().equals(current)){
	        		txtBox.removeTextChangedListener(this);

	        		String cleanString = userInput;

	        		if(cleanString.length()>0){		        		
		        		current = cleanString;
		        		txtBox.setText(cleanString.toUpperCase());
		        		txtBox.setSelection(cleanString.length());
	        		}else{
	        			txtBox.setText(cleanString);
		        		txtBox.setSelection(cleanString.length());
	        		}
	        		
	        		txtBox.addTextChangedListener(this);
        	    }
	        }
	        @Override
	        public void beforeTextChanged(CharSequence s, int start,
	                int count, int after) {
	        }
	        @Override
	        public void onTextChanged(CharSequence s, int start,
	                int before, int count) {
	        }
	    });
	}
	
	public String getLabel()
	{
		return label.getText().toString();
	}
	
	public String getValue()
	{
		return txtBox.getText().toString();
	}
	
	public void setValue(String v)
	{
		txtBox.setText(v);
	}
	
	public void setErrorMsg(String v)
	{
		txtBox.setError(v);
	}

}