package healthliteracy.models;

public class DistinctWebPage {

	private String URL;
	private String scrollType;
	private String domain;
	private long duration;
	private String eventType;

	public String getScrollType() {
		return scrollType;
	}

	public void setScrollType(String scrollType) {
		this.scrollType = scrollType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
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
