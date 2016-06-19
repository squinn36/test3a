package healthliteracy.services;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import healthliteracy.const_values.HealthLiteracyConst;
import healthliteracy.models.DistinctWebPage;
import healthliteracy.models.Question;
import healthliteracy.models.Answer;

public class OutputExcelFile implements HealthLiteracyConst {

	private int cellIndex;

	private void addStringValue(Row row, String contents) {
		cellIndex++;
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(contents);
	}

	private void addStringValue(Row row, int cellInd, String contents) {
		Cell cell = row.createCell(cellInd);
		cell.setCellValue(contents);
	}

	private void addIntegerValue(Row row, int contents) {
		cellIndex++;
		Cell cell = row.createCell(cellIndex);
		cell.setCellValue(contents);
	}

	private Workbook addAllParticipants(Map<Integer, Map<Integer, Question>> questions) {
		Workbook wb = new HSSFWorkbook();
		Sheet sheet = wb.createSheet("hci data");
		Row row = sheet.createRow((short) 0);
		for (int i = 0; i < HEADER_URL.length; i++) {
			addStringValue(row, i, HEADER_URL[i]);
		}

		Set<Integer> outerKeys = questions.keySet();
		int rowNo = 0;
		for (Integer outerKey : outerKeys) {
			Map<Integer, Question> participant = questions.get(outerKey);
			Set<Integer> innerKeys = participant.keySet();
			for (Integer innerKey : innerKeys) {
				rowNo++;
				row = sheet.createRow((short) rowNo);
				cellIndex = -1;
				addIntegerValue(row, outerKey);
				addIntegerValue(row, innerKey);
				Question que = participant.get(innerKey);
				addIntegerValue(row, que.getPreTaskAnswer());
				addIntegerValue(row, que.getPostTaskAnswer());
				addIntegerValue(row, (int) que.getQuestionDuration());

				List<Answer> answers = que.getAnswer();
				addIntegerValue(row, answers.size());
				for (Answer ans : answers) {
					addStringValue(row, ans.getAnswerText());
					addStringValue(row, ans.getAnswerDomain());
					addStringValue(row, ans.getAnswerURL());
				}

				if (answers.size() < MAX_ANSWERS) {
					for (int i = answers.size(); i < MAX_ANSWERS; i++) {
						addStringValue(row, CELL_FILLER);
						addStringValue(row, CELL_FILLER);
						addStringValue(row, CELL_FILLER);
					}
				}

				Set<String> searchTerms = que.getSearchTerms();
				addIntegerValue(row, searchTerms.size());
				for (String str : searchTerms) {
					addStringValue(row, str);
				}

				if (searchTerms.size() < MAX_SEARCH_TERMS) {
					for (int i = searchTerms.size(); i < MAX_SEARCH_TERMS; i++) {
						addStringValue(row, CELL_FILLER);
					}
				}
				
				List<DistinctWebPage> website = que.getWebsites();
				for (DistinctWebPage webpage : website) {
					addStringValue(row, webpage.getDomain());
					addIntegerValue(row, (int) webpage.getDuration());
					addStringValue(row, webpage.getEventType());
					addStringValue(row, webpage.getScrollType());
					addStringValue(row, webpage.getURL());
				}
			}
		}
		return wb;
	}

	public void outputExcelWorkbook(Map<Integer, Map<Integer, Question>> questions) {
		Workbook wb = addAllParticipants(questions);
		try {
			FileOutputStream fileOut = new FileOutputStream(EXCEL_OUTPUT_FILE);
			wb.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}