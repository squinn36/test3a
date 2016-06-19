package healthliteracy.models;

public class Answer {

	private String answerText;
	private String answerURL;
	private String answerDomain;

	public String getAnswerURL() {
		return answerURL;
	}

	public void setAnswerURL(String answerURL) {
		this.answerURL = answerURL;
	}

	public String getAnswerDomain() {
		return answerDomain;
	}

	public void setAnswerDomain(String answerDomain) {
		this.answerDomain = answerDomain;
	}

	public String getAnswerText() {
		return answerText;
	}

	public void setAnswerText(String answerText) {
		this.answerText = answerText;
	}
}
