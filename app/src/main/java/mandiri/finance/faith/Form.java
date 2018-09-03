package mandiri.finance.faith;

import java.util.Vector;
import java.util.ListIterator;

public class Form {

	private String formNumber;
	private String formName;
	private String submitTo;
	public Vector<FormField> fields;
		
	public Form()
	{
		this.fields = new Vector<FormField>();
		formNumber = "";
		formName = "";
		submitTo = "loopback"; // ie, do nothing but display the results
	}
	// getters & setters
	public String getFormNumber() {
		return formNumber;
	}

	public void setFormNumber(String formNumber) {
		this.formNumber = formNumber;
	}


	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public String getSubmitTo() {
		return submitTo;
	}

	public void setSubmitTo(String submitTo) {
		this.submitTo = submitTo;
	}

	public Vector<FormField> getFields() {
		return fields;
	}

	public void setFields(Vector<FormField> fields) {
		this.fields = fields;
	}

	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Form:\n");
		sb.append("Form Number: " + this.formNumber + "\n");
		sb.append("Form Name: " + this.formName + "\n");
		sb.append("Submit To: " + this.submitTo + "\n");
		if (this.fields == null) return sb.toString();
		ListIterator<FormField> li = this.fields.listIterator();
		while (li.hasNext()) {
			sb.append(li.next().toString());
		}
		
		return sb.toString();
	}
	
	public String getFormattedResults()
	{
		StringBuilder sb = new StringBuilder();
		if (this.fields == null) return sb.toString();
		ListIterator<FormField> li = this.fields.listIterator();
		while (li.hasNext()) {
			//count = li.nextIndex();
			sb.append(li.next().getFormattedResult() + ", ");
		}
		
		return sb.toString();
	}
	
	public String[] getEncodedData()
	{
		String[] data = null;
		data = new String[2];
		
		StringBuilder sbName = new StringBuilder();
		StringBuilder sbValue = new StringBuilder();
		ListIterator<FormField> li = this.fields.listIterator();
		
		while (li.hasNext()) {			
			FormField thisField = li.next();
			
			sbName.append(thisField.name + ";");
			sbValue.append("'" + thisField.getData() + "';");
		}		
		data[0] = new String(sbName.toString());			 			
		data[1] = new String(sbValue.toString().replace(",", "").replace(" ' ",""));
		return data;
	}	
	
}

