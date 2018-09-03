package mandiri.finance.faith;

import android.util.Log;

import mandiri.finance.faith.EditBox;
import mandiri.finance.faith.PickOne;

public class FormField {
	String initialText;
	boolean isInitialText;
	String name;
	String label;
	String type;
	boolean required;
	String optionsName;
	String optionsID;
	String hint;
	String errmsg;
	String maxlength;
	boolean issearchable;
	Object obj;			
		
	// getters & setters
	
	public boolean getIsSearchable() {
		return issearchable;
	}
	public void setIsSearchable(Boolean issearchable) {
		this.issearchable = issearchable;
	}
	public String getMaxLength() {
		return maxlength;
	}
	public void setMaxLength(String maxlength) {
		this.maxlength = maxlength;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public String getHint() {
		return hint;
	}
	public void setHint(String hint) {
		this.hint = hint;
	}	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	
	public String getOptionsName() {
		return optionsName;
	}
	public void setOptionsName(String optionsName) {
		this.optionsName = optionsName;
	}
	
	public String getOptionsID() {
		return optionsID;
	}
	public void setOptionsID(String optionsID) {
		this.optionsID = optionsID;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Field Name : " + this.name + "\n");
		sb.append("Field Label : " + this.label + "\n");
		sb.append("Field Type : " + this.type + "\n");
		sb.append("Required? : " + this.required + "\n");
		sb.append("Hint : " + this.hint + "\n");
		sb.append("ErrMsg : " + this.errmsg + "\n");
		sb.append("Options : " + this.optionsName + "\n");
		sb.append("Value : " + (String) this.getData() + "\n");
		
		return sb.toString();
	}
	
	public String getFormattedResult()
	{
		return this.name + "= [" + (String) this.getData() + "]";

	}
	
	public Object getData()
	{
		if (type.equals("text") || type.equals("number") || type.equals("multiline"))
		{
			if (obj != null) {
				EditBox b = (EditBox) obj;
				Log.d("cek", "getData: text / number / multiline "+obj);
				return b.getValue();
			}
		}else if (type.equals("decimal"))
		{
			if (obj != null) {
				EditBox b = (EditBox) obj;
				Log.d("cek", "getData: decimal"+obj);
				return b.getValue().replace(",", "");
			}
		}else if (type.equals("pickone")) {
			if (obj != null) {
				PickOne po = (PickOne) obj;
				return po.getValue();
			}
		}else if (type.equals("pickonechoice")) {
			if (obj != null) {
				PickOneChoice poc = (PickOneChoice) obj;
				return poc.getValue();
			}
		}else if (type.equals("radiobutton")) {
			if (obj != null) {
				RadioBtn rb = (RadioBtn) obj;
				return rb.getValueradiobtton();
			}
		}
		 else if (type.equals("radiobutton")) {
					if (obj != null) {
						RadioBtn rb = (RadioBtn) obj;
						return rb.getValueradiobtton();
					}
				}
		else if (type.equals("date")) {
			if (obj != null) {
				PickDate d = (PickDate) obj;
				return d.getValue();
			}
		}

		else if (type.equals("PickTempatLahir")) {
			if (obj != null) {
				PickTempatLahir d = (PickTempatLahir) obj;
				return d.getValue();
			}
		}

		else if (type.equals("PickTahun")) {
			if (obj != null) {
				Pick_tahun d = (Pick_tahun) obj;
				return d.getValue();
			}
		}

		else if (type.equals("Pick_Ekonomi")) {
			if (obj != null) {
				Pick_Ekonomi d = (Pick_Ekonomi) obj;
				return d.getValueEko1();

			}
            if (obj != null) {
                Pick_Ekonomi d = (Pick_Ekonomi) obj;
                return d.getValueEko2();

            }
		}

		else{
			if (obj != null) {
				PickOneSupplier pos = (PickOneSupplier) obj;
				return pos.getValue();
			}
		}


				
		return null;
	}
	
	public Object getDataLabel()
	{
		if (type.equals("text") || type.equals("number") || type.equals("multiline") || type.equals("decimal"))
		{
			if (obj != null) {
				EditBox b = (EditBox) obj;
				Log.d("cek", "getDataLabel: text / number / multiline / decimal "+obj);
				return b.getLabel();
			}
		}
		if (type.equals("pickone")) {
			if (obj != null) {
				PickOne po = (PickOne) obj;

				return po.getLabel();
			}
		}
		
		if (type.equals("pickonechoice")) {
			if (obj != null) {
				PickOneChoice poc = (PickOneChoice) obj;
				return poc.getLabel();
			}
		}
		
		if (type.equals("radiobutton")) {
			if (obj != null) {
				RadioBtn rb = (RadioBtn) obj;
				return rb.getLabel();
			}
		}
		
		if (type.equals("date")) {
			if (obj != null) {
				PickDate d = (PickDate) obj;
				return d.getLabel();
			}
		}

		if (type.equals("pickonesupplier")) {
			if (obj != null) {
				PickOneSupplier pos = (PickOneSupplier) obj;
				return pos.getLabel();
			}
		}

		if (type.equals("PickTempatLahir")) {
			if (obj != null) {
				PickTempatLahir pos = (PickTempatLahir) obj;
				return pos.getLabel();
			}
		}

		if (type.equals("PickTahun")) {
			if (obj != null) {
				Pick_tahun pos = (Pick_tahun) obj;
				return pos.getLabel();
			}
		}

		if (type.equals("Pick_Ekonomi")) {
			if (obj != null) {
				Pick_Ekonomi pos = (Pick_Ekonomi) obj;
				return pos.getLabel();
			}
		}

		return null;
	}
	
	public void setData(String v)
	{
		if (type.equals("text") || type.equals("number") || type.equals("multiline") || type.equals("decimal"))
		{
			if (obj != null) {
				EditBox b = (EditBox) obj;
				Log.d("cek", "setData: text / number / multiline / decimal "+obj);
				b.setErrorMsg(v);
			}
		}
		if (type.equals("pickone"))
		{
			if (obj != null) {
				PickOne po = (PickOne) obj;
				po.setErrorMsg(v);
			}
		}
		if (type.equals("pickonechoice"))
		{
			if (obj != null) {
				PickOneChoice poc = (PickOneChoice) obj;
				poc.setErrorMsg(v);
			}
		}
		if (type.equals("date"))
		{
			if (obj != null) {
				PickDate d = (PickDate) obj;
				d.setErrorMsg(v);
			}
		}
		if (type.equals("pickonesupplier"))
		{
			if (obj != null) {
				PickOneSupplier pos = (PickOneSupplier) obj;
				pos.setErrorMsg(v);
			}
		}

		if (type.equals("PickTempatLahir"))
		{
			if (obj != null) {
				PickTempatLahir pos = (PickTempatLahir) obj;
				pos.setErrorMsg(v);
			}
		}

		if (type.equals("PickTahun"))
		{
			if (obj != null) {
				Pick_tahun pos = (Pick_tahun) obj;
				pos.setErrorMsg(v);
			}
		}

		if (type.equals("Pick_Ekonomi"))
		{
			if (obj != null) {
				Pick_Ekonomi pos = (Pick_Ekonomi) obj;
				pos.setErrorMsg(v);
			}
		}



	}



	
}

