package healthliteracy.models;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class QuestionModelXML {

	private List<Question> listOfQuestions;

	public void setQuestion(List<Question> listOfQuestions) {
		this.listOfQuestions = listOfQuestions;
	}

	public List<Question> getQuestion() {
		return listOfQuestions;
	}

	private Set<Integer> getParticipants() {
		Set<Integer> participantNos = new TreeSet<Integer>();
		for (Question que : listOfQuestions) {
			int i = Integer.parseInt(que.getParticipantNo().trim());
			participantNos.add(i);
		}
		return participantNos;
	}

	private Map<Integer, Question> getParticipantQuestions(String participantNo) {
		Map<Integer, Question> questions = new TreeMap<Integer, Question>();
		for (Question que : listOfQuestions) {
			if (que.getParticipantNo().trim().equals(participantNo)) {
				int key = Integer.parseInt(que.getQuestionNo().trim());
				questions.put(key, que);
			}
		}
		return questions;
	}

	public Map<Integer, Map<Integer, Question>> sortQuestions() {
		Map<Integer, Map<Integer, Question>> sortedQuestions = new TreeMap<Integer, Map<Integer, Question>>();
		Map<Integer, Question> participantQuestions;
		Set<Integer> participantNos = getParticipants();
		for (Integer participantNo : participantNos) {
			participantQuestions = getParticipantQuestions(participantNo.toString());
			sortedQuestions.put(participantNo, participantQuestions);
		}
		return sortedQuestions;
	}
}
