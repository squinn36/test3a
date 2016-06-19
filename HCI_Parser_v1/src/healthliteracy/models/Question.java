package healthliteracy.models;

import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElementWrapper;

public class Question {

	private List<DistinctWebPage> website;
	private List<Answer> suggestedAnswer;
	private Set<String> terms;
	private int preTask;
	private int postTask;
	private String partNo;
	private String questNo;
	private long questionDuration;

	@XmlElementWrapper(name = "distinctWebsites")
	public List<DistinctWebPage> getWebsites() {
		return website;
	}

	public void setWebsites(List<DistinctWebPage> website) {
		this.website = website;
	}

	@XmlElementWrapper(name = "distinctAnswers")
	public List<Answer> getAnswer() {
		return suggestedAnswer;
	}

	public void setAnswer(List<Answer> suggestedAnswer) {
		this.suggestedAnswer = suggestedAnswer;
	}

	@XmlElementWrapper(name = "distinctSearchTerms")
	public Set<String> getSearchTerms() {
		return terms;
	}

	public void setSearchTerms(Set<String> terms) {
		this.terms = terms;
	}

	public int getPreTaskAnswer() {
		return preTask;
	}

	public void setPreTaskAnswer(int preTask) {
		this.preTask = preTask;
	}

	public int getPostTaskAnswer() {
		return postTask;
	}

	public void setPostTaskAnswer(int postTask) {
		this.postTask = postTask;
	}

	public String getParticipantNo() {
		return partNo;
	}

	public void setParticipantNo(String partNo) {
		this.partNo = partNo;
	}

	public String getQuestionNo() {
		return questNo;
	}

	public void setQuestionNo(String questNo) {
		this.questNo = questNo;
	}

	public long getQuestionDuration() {
		return questionDuration;
	}

	public void setQuestionDuration(long questionDuration) {
		this.questionDuration = questionDuration;
	}
}
