package healthliteracy.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import healthliteracy.services.ProcessHCIFile;
import healthliteracy.const_values.HealthLiteracyConst;

public class HealthLiteracyController implements HealthLiteracyConst {

	public static void main(String args[]) {
		try {
			ProcessHCIFile hciData = new ProcessHCIFile();
			File file = new File(HCI_BROWSER_INPUT_FILE);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str;
			while ((str = bufferedReader.readLine()) != null) {
				hciData.readHCIFile(str);
			}
			hciData.outputXML();
			hciData.sort();
			hciData.outputExcel();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}
