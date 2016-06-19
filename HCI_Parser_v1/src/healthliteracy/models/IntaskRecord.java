package healthliteracy.models;

import java.util.Calendar;

public class IntaskRecord {

	private Calendar date;
	private String URL;
	private String domain;
	private long duration;
	private String scrollType;
	private String eventType;

	public IntaskRecord() {
		this.date = null;
		this.URL = null;
		this.scrollType = null;
		this.domain = null;
		this.duration = 0l;
	}

	public IntaskRecord(Calendar date, String URL, String domain, String scrollType, String eventType) {
		this.date = date;
		this.URL = URL;
		this.domain = domain;
		this.scrollType = scrollType;
		this.eventType = eventType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getScrollType() {
		return scrollType;
	}

	public void setScrollType(String scrollType) {
		this.scrollType = scrollType;
	}

	public Calendar getDate() {
		return date;
	}

	public void setDate(Calendar date) {
		this.date = date;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
}
