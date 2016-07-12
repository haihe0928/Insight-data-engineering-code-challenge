package venmo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

public class Payment implements Comparable<Payment>{
	final static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	final static DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
	public List<String> names;
	public long timestamp;
	public Payment(String actor, String target, String timeString) {
		if (actor.compareTo(target) <= 0) {
			names = Arrays.asList(actor, target);
		} else {
			names = Arrays.asList(target, actor);
		}
		try {
			timestamp = formatter.parse(timeString).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	public Payment(JSONObject object) {
		String actor = (String) object.get("actor");
		String target = (String) object.get("target");
		if (actor.compareTo(target) <= 0) {
			names = Arrays.asList(actor, target);
		} else {
			names = Arrays.asList(target, actor);
		}
		try {
			timestamp = formatter.parse((String) object.get("created_time")).getTime() / 1000;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public int compareTo(Payment another) {
		if (this.timestamp == another.timestamp && this.names.get(0).equals(another.names.get(0))) {
			return this.names.get(1).compareTo(another.names.get(1));
		} else if (this.timestamp == another.timestamp) {
			return this.names.get(0).compareTo(another.names.get(0));
		}
		
		return this.timestamp < another.timestamp ? -1 : 1;
	}
}
