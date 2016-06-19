package healthliteracy.const_values;

import java.util.regex.Pattern;

public interface HealthLiteracyConst {

	String HCI_BROWSER_INPUT_FILE = "HCI_InputFile.txt";
	String EXCEL_OUTPUT_FILE = "HCI_ExcelOutputFile.xls";
	String XML_OUTPUT_FILE = "HCI_XmlOutputFile.xml";
	String POST_TASK = "posttask";
	String PRE_TASK = "pretask";
	String IN_TASK = "intask";
	String SUBMITTED_ANSWER_TEXT = "submittedAnswerText";
	String TASK_DESC = "TaskDescription";
	String Q1 = "q1";
	String GOOGLE_SEARCH = "google.co.uk/search";
	String GOOGLE_COM_SEARCH = "google.com/search";
	String GOOGLE_LINK = "google.co.uk/url";
	String GOOGLE_COM_LINK = "google.com/url";
	String GOOGLE_HOME = "about:home";
	String IS_SEARCH_RESULTS = "SearchResults";
	String IS_LINK = "Link";
	String IS_INFO_PAGE = "Information";
	String IS_HOME_PAGE = "HomePage";
	String SCROLL = "Scroll";
	String NO_SCROLL = "NoScroll";

	String REGEX_TAB = "\\t";
	Pattern TIME_PATTERN = Pattern.compile("\\d\\d:\\d\\d:\\d\\d");
	Pattern DATE_PATTERN = Pattern.compile("\\d?\\d-\\d\\d-\\d\\d\\d\\d");
	String DASH = "-";
	String COLON = ":";
	String UNDERSCORE = "_";

	int MAX_ANSWERS = 3;
	String CELL_FILLER = "";
	int MAX_SEARCH_TERMS = 5;

	enum TaskTypes {
		PRE_QUESTION, POST_QUESTION, SUBMITTED_ANSWER, INTASK_REC, TASK_DESCRIPTION
	};

	String[] EVENTS = { "Focus", "Blur", "LoadCap", "LoadBub", "LClick", "MClick", "RClick", "TypeURL",
			"HomeButtonClick", "BackButtonClick", "ForwardButtonClick", "Scroll", "Show", "Hide", "AddTab", "RmTab",
			"SelTab" };

	String[] PARAMETER_NAMES = { "&hl", "adtes", "aq", "as_lq", "as_rq", "btnG", "channel", "client", "cop", "cr", "ei",
			"field-keywords", "fr", "fp", "gfe_rd", "gs_l", "gws_rd", "hvadid", "ie", "index", "introPage", "lang",
			"lr", "newwindow", "node", "oe", "oq", "pws", "qid", "ref", "rh", "rls", "s", "sa", "safe", "skip",
			"source", "sprefix", "sr", "tag", "toggle", "url", "usg", "ved" };
	
	String[] INVALID_PARAMETER_VALUES = { "google", "yahoo", "en", "1", "2", "image_result_group", "isch", "", "en-GB",
			"219912133", "ivns", "0", "AOD64_3I-el2l2b4rt7Uxwl1j4S07uCKZA",
			"C42-F62USVtzsFoPPiwb1hrOICKitl4cHsKzpm7oC0cnkgmsIABABYLuGgIDYCqABuMTA1APIAQGpAh16FVVbg7k-qgQqT9CuQtL2swcGxkD45mbFrdgF8amMX6aagxicapiuhrNWhlOc1qwUHezegAewu78rkAcDqAemvhvYBwE",
			"AOD64_01WL0d_P9vPFw7ePAJ4g8S8JOL7w",
			"CxgnC0GUSVriyNaWljAark7SIDpfc5-sFt5Gx9PYB5MWgDwgAEAFgu4aAgNgKoAGV-6HwA8gBAaoEJ0_QEuztbbt-dP4H2U78L91jrNzEfEaibzo0604VuB9elj1uERsUCIAH04TeD5AHA6gHpr4b2AcB" };
			
	String[] HEADER_URL = { "Part", "Que", "PreTask", "PostTask", "Duration", "TotalAnswers", "Answer1", "Ans1Domain",
			"Ans1URL", "Answer2", "Ans2Domain", "Ans2URL", "Answer3", "Ans3Domain", "Ans3URL", "TotalSearchTerms",
			"SearchTerms1", "SearchTerms2", "SearchTerms3", "SearchTerms4", "SearchTerms5", "Ws1Domain", "Ws1Duration",
			"Ws1Type", "Ws1Event", "Ws1URL", "Ws2Domain", "Ws2Duration", "Ws2Type", "Ws2Event", "Ws2URL", "Ws3Domain",
			"Ws3Duration", "Ws3Type", "Ws3Event", "Ws3URL", "Ws4Domain", "Ws4Duration", "Ws4Type", "Ws4Event", "Ws4URL",
			"Ws5Domain", "Ws5Duration", "Ws5Type", "Ws5Event", "Ws5URL", "Ws6Domain", "Ws6Duration", "Ws6Type",
			"Ws6Event", "Ws6URL", "Ws7Domain", "Ws7Duration", "Ws7Type", "Ws7Event", "Ws7URL", "Ws8Domain",
			"Ws8Duration", "Ws8Type", "Ws8Event", "Ws8URL" };
}
