package healthliteracy.services;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;

import healthliteracy.models.QuestionModelXML;
import healthliteracy.const_values.HealthLiteracyConst;

public class OutputXMLFile implements HealthLiteracyConst {

	public void outputXML(QuestionModelXML questionModelXML) {
		try {
			JAXBContext jc = JAXBContext.newInstance(QuestionModelXML.class);
			Marshaller marshaller = jc.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(questionModelXML, new File(XML_OUTPUT_FILE));
		} catch(MarshalException e) {
			e.printStackTrace();
		} catch(JAXBException e) { 
		    e.printStackTrace();
		}
	}

}
