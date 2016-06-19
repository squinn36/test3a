package healthliteracy.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.validator.routines.DomainValidator;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.NameValuePair;

import healthliteracy.const_values.HealthLiteracyConst;
import healthliteracy.models.Answer;
import healthliteracy.models.IntaskRecord;
import healthliteracy.models.DistinctWebPage;
import healthliteracy.models.Question;
import healthliteracy.models.QuestionModelXML;

public class ProcessHCIFile implements HealthLiteracyConst {

	private List<Question> listOfQuestions = new ArrayList<Question>();
	private Question question = null;
	private List<Answer> inTaskAnswers = null;
	private List<IntaskRecord> inTaskRecords = null;
	private List<DistinctWebPage> distinctPageVisits = null;
	private List<DistinctWebPage> distinctPageVisitsXML = null;
	private List<String> queryStr = null;
	private List<String> searchTermsList = null;
	private Set<String> searchTermsSet = null;
	private QuestionModelXML questionModelXML = null;
	private Map<Integer, Map<Integer, Question>> sortedQuestions = null;
	private OutputXMLFile oxml = null;
	private OutputExcelFile oe = null;

	private UrlValidator urlValidator = new UrlValidator();

	private Calendar firstRecordTimestamp;
	private Calendar lastRecordTimestamp;
	private Calendar answerTimestamp;
	private long questionDuration;

	private boolean containsTerm(String[] searchArray, String searchTerm) {
		boolean found = false;
		Arrays.sort(searchArray);
		int pos = Arrays.binarySearch(searchArray, searchTerm);
		if (pos >= 0) {
			found = true;
		}
		return found;
	}

	private boolean isTask(String str, String task) {
		return str.contains(task);
	}

	private boolean isTask(String str) {
		boolean found = false;
		for (String task : EVENTS) {
			if (isTask(str, task)) {
				found = true;
			}
		}
		return found;
	}

	private int matchToken(String[] tokens, String searchStr) {
		int foundpos = -1, element = 0;
		boolean found = false;
		while (element < tokens.length && !found) {
			if (tokens[element].equals(searchStr)) {
				foundpos = element;
				found = true;
			}
			element++;
		}
		return foundpos;
	}

	private long computeDuration(Calendar cal1, Calendar cal2) {
		long durationSeconds = 0;
		Calendar currentDate = Calendar.getInstance();
		Calendar nextDate = Calendar.getInstance();
		currentDate = cal1;
		nextDate = cal2;
		long durationMilliseconds = nextDate.getTimeInMillis() - currentDate.getTimeInMillis();
		durationSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMilliseconds);
		return durationSeconds;
	}

	private int[] getTemporalComponent(String token, Pattern matchPattern, String splitStr, boolean changeMonth) {
		int[] resultInt = null;
		Matcher m = matchPattern.matcher(token);
		if (m.find()) {
			resultInt = new int[3];
			String[] resultStr = m.group().split(splitStr);
			for (int element = 0; element < resultStr.length; element++) {
				resultInt[element] = Integer.parseInt(resultStr[element]);
			}
			if (changeMonth) {
				// correct month
				resultInt[1]--;
			}
		}
		return resultInt;
	}

	private int getPrevURLPos(String url) {
		int pos = -1;
		//boolean found = false;
		for (int element = 0; element < distinctPageVisits.size(); element++) {
			if (distinctPageVisits.get(element).getURL().equals(url)) {
				pos = element;
			}
		}
		return pos;
	}

	private void setDistinctWebsites() {
		if (inTaskRecords.size() != 0) {
			DistinctWebPage webPage = new DistinctWebPage();
			webPage.setURL(inTaskRecords.get(0).getURL());
			webPage.setDomain(inTaskRecords.get(0).getDomain());
			webPage.setDuration(inTaskRecords.get(0).getDuration());
			webPage.setScrollType(inTaskRecords.get(0).getScrollType());
			webPage.setEventType(inTaskRecords.get(0).getEventType());
			distinctPageVisits.add(webPage);

			int element = 0;
			for (int i = 1; i < inTaskRecords.size(); i++) {
				String urlStr = inTaskRecords.get(i).getURL();
				int foundPos = getPrevURLPos(urlStr);

				if (foundPos != -1) {
					long duration = distinctPageVisits.get(foundPos).getDuration();
					duration = duration + inTaskRecords.get(i).getDuration();
					distinctPageVisits.get(foundPos).setDuration(duration);
					if (inTaskRecords.get(i).getScrollType().equals(SCROLL)) {
						distinctPageVisits.get(foundPos).setScrollType(SCROLL);
					}
				} else {
					webPage = new DistinctWebPage();
					webPage.setURL(inTaskRecords.get(i).getURL());
					webPage.setDomain(inTaskRecords.get(i).getDomain());
					webPage.setDuration(inTaskRecords.get(i).getDuration());
					webPage.setScrollType(inTaskRecords.get(i).getScrollType());
					webPage.setEventType(inTaskRecords.get(i).getEventType());
					distinctPageVisits.add(webPage);
					element++;
				}
			}
		}
	}

	private void setDistinctWebsitesXML() {
		distinctPageVisitsXML = new ArrayList<DistinctWebPage>();
		for (DistinctWebPage webpage : distinctPageVisits) {
			if (webpage.getEventType().equals(IS_SEARCH_RESULTS) || webpage.getEventType().equals(IS_INFO_PAGE)) {
				distinctPageVisitsXML.add(webpage);
			}
		}
	}

	private void setDistinctSearchTerms() {
		for (String str : queryStr) {
			if (!urlValidator.isValid(str) && !containsTerm(INVALID_PARAMETER_VALUES, str)) {
				searchTermsList.add(str);
				searchTermsSet.add(str);
			}
		}
	}

	private void setPageDuration() {
		for (int index = 0; index < inTaskRecords.size() - 1; index++) {
			long durationSeconds = computeDuration(inTaskRecords.get(index).getDate(),
					inTaskRecords.get(index + 1).getDate());
			inTaskRecords.get(index).setDuration(durationSeconds);
		}
	}
	
	public void sort() {
		sortedQuestions = questionModelXML.sortQuestions();
	}

	private int storePrePostTaskAnswer(String str) {
		String[] tokens = tokeniseLine(str);
		int foundpos = matchToken(tokens, Q1);
		int answer = 0;
		if (foundpos != -1) {
			answer = Integer.parseInt(tokens[foundpos + 1].trim());
		}
		return answer;
	}

	private String[] tokeniseLine(String str) {
		String[] tokens = str.split(REGEX_TAB);
		return tokens;
	}

	private void processLine(String str, TaskTypes taskType) {
		try {
			int[] timeInt = null;
			int[] dateInt = null;
			boolean timeFound = false, dateFound = false, urlFound = false, dateSet = false;
			String eventType = IS_INFO_PAGE;
			if (isTask(str, GOOGLE_HOME)) {
				eventType = IS_HOME_PAGE;
			} else {
				if (isTask(str, GOOGLE_LINK) || isTask(str, GOOGLE_COM_LINK)) {
					eventType = IS_LINK;
				} else {
					if (isTask(str, GOOGLE_SEARCH) || isTask(str, GOOGLE_COM_SEARCH)) {
						eventType = IS_SEARCH_RESULTS;
					}
				}
			}

			Calendar cal = null;
			String url = null, domain = null;
			DomainValidator domainValidator = DomainValidator.getInstance();
			String[] lineTokens = tokeniseLine(str);
			for (String token : lineTokens) {
				if (!dateFound) {
					dateInt = getTemporalComponent(token, DATE_PATTERN, DASH, true);
					if (dateInt != null) {
						dateFound = true;
					}
				}
				if (!timeFound) {
					timeInt = getTemporalComponent(token, TIME_PATTERN, COLON, false);
					if (timeInt != null) {
						timeFound = true;
					}
				}
				if (dateFound && timeFound && !dateSet) {
					cal = Calendar.getInstance();
					cal.set(dateInt[2], dateInt[1], dateInt[0], timeInt[0], timeInt[1], timeInt[2]);
					dateSet = true;
					if (taskType == TaskTypes.SUBMITTED_ANSWER) {
						answerTimestamp = cal;
						long durationSeconds;
						if (inTaskRecords.size() != 0) {
							durationSeconds = computeDuration(inTaskRecords.get(inTaskRecords.size() - 1).getDate(),
									cal);
							inTaskRecords.get(inTaskRecords.size() - 1).setDuration(durationSeconds);
						} else {
							durationSeconds = 0l;
						}
					} else {
						if (taskType == TaskTypes.TASK_DESCRIPTION) {
							firstRecordTimestamp = cal;
						} else {
							if (taskType == TaskTypes.POST_QUESTION) {
								lastRecordTimestamp = cal;
							}
						}
					}
				}

				if (taskType != TaskTypes.TASK_DESCRIPTION && taskType != TaskTypes.POST_QUESTION) {
					if (!urlFound) {
						if (token.contains(GOOGLE_HOME)) {
							url = GOOGLE_HOME;
							urlFound = true;
							domain = GOOGLE_HOME;
						} else {
							if (urlValidator.isValid(token)) {
								url = token;
								urlFound = true;
								URL urlObj = null;
								try {
									urlObj = new URL(token);
								} catch (MalformedURLException e) {
									System.out.println(e);
								}
								String host = urlObj.getHost();
								String query = urlObj.getQuery();
								String ref = urlObj.getRef();
								if (ref != null) {
									String omitAnchorStr = token.substring(0, token.indexOf("#"));
									url = omitAnchorStr;
								}
								if (host != null) {
									if (domainValidator.isValid(host)) {
										domain = host;
									}
								}
								if (eventType.equals(IS_SEARCH_RESULTS)) {
									if (query != null) {
										List<NameValuePair> params = URLEncodedUtils.parse(new URI(url), "UTF-8");
										for (NameValuePair param : params) {
											if (!containsTerm(PARAMETER_NAMES, param.getName())) {
												queryStr.add(param.getValue());
											}
										}
									}
								}
							}
						}
					}
				}
			}

			if (urlFound) {
				if (taskType == TaskTypes.INTASK_REC) {
					String scrollType = NO_SCROLL;
					if (str.contains(SCROLL)) {
						scrollType = SCROLL;
					}
					inTaskRecords.add(new IntaskRecord(cal, url, domain, scrollType, eventType));
				}
				if (taskType == TaskTypes.SUBMITTED_ANSWER) {
					int foundpos = matchToken(lineTokens, SUBMITTED_ANSWER_TEXT);
					String answerText = null;
					if (foundpos != -1) {
						answerText = lineTokens[foundpos + 1].trim();
					}
					Answer ans = new Answer();
					ans.setAnswerText(answerText);
					ans.setAnswerURL(url);
					ans.setAnswerDomain(domain);
					inTaskAnswers.add(ans);
				}
			}
		} catch (URISyntaxException e) {
			System.out.println(e);
		}
	}

	public void readHCIFile(String inputFile) {
		try {
			System.out.println("Processing..." + inputFile + ".txt\t");
			question = new Question();

			String[] details = inputFile.split(UNDERSCORE);
			String participantNo = details[0].substring(1);
			String questionNo = details[1].substring(1);
			question.setParticipantNo(participantNo);
			question.setQuestionNo(questionNo);

			inTaskAnswers = new ArrayList<Answer>();
			inTaskRecords = new ArrayList<IntaskRecord>();
			queryStr = new ArrayList<String>();
			searchTermsList = new ArrayList<String>();
			searchTermsSet = new LinkedHashSet<String>();
			distinctPageVisits = new ArrayList<DistinctWebPage>();

			File f1 = new File(inputFile + ".txt");
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);

			String str;
			while ((str = br.readLine()) != null) {
				if (isTask(str, PRE_TASK)) {
					if (isTask(str, Q1)) {
						int preTaskAnswer = storePrePostTaskAnswer(str);
						question.setPreTaskAnswer(preTaskAnswer);
					} else {
						if (isTask(str, TASK_DESC)) {
							processLine(str, TaskTypes.TASK_DESCRIPTION);
						}
					}
				} else {
					if (isTask(str, POST_TASK)) {
						if (isTask(str, Q1)) {
							int postTaskAnswer = storePrePostTaskAnswer(str);
							question.setPostTaskAnswer(postTaskAnswer);
							processLine(str, TaskTypes.POST_QUESTION);
						}
					} else {
						if (isTask(str, IN_TASK)) {
							if (isTask(str, SUBMITTED_ANSWER_TEXT)) {
								processLine(str, TaskTypes.SUBMITTED_ANSWER);
							} else {
								if (isTask(str)) {
									processLine(str, TaskTypes.INTASK_REC);
								}
							}
						}
					}
				}
			}

			setPageDuration();
			setDistinctWebsites();
			setDistinctWebsitesXML();
			setDistinctSearchTerms();
			question.setWebsites(distinctPageVisitsXML);
			question.setAnswer(inTaskAnswers);
			question.setSearchTerms(searchTermsSet);
			questionDuration = computeDuration(firstRecordTimestamp, lastRecordTimestamp);
			question.setQuestionDuration(questionDuration);
			listOfQuestions.add(question);
			questionModelXML = new QuestionModelXML();
			questionModelXML.setQuestion(listOfQuestions);
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void outputXML() {
		oxml = new OutputXMLFile();
		oxml.outputXML(questionModelXML);
	}
	
	public void outputExcel() {
		oe = new OutputExcelFile();
		oe.outputExcelWorkbook(sortedQuestions);
	}
}
