package officePoi;


import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Docx extends Office {
	private XWPFDocument doc = null;

	public Docx(String path) {
		file = new File(path);
		try {
			in = new FileInputStream(file);
			doc = new XWPFDocument(in);
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Matcher SearchStr(String pattern) {

		String text = new XWPFWordExtractor(doc).getText();
		Pattern r = Pattern.compile(pattern);
		Matcher matcher = r.matcher(text);
		return matcher;
	}

	public void replaceStr(Map<String, String> params, boolean opEnd) {

		Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
		XWPFParagraph para;

		while (iterator.hasNext()) {
			para = iterator.next();
			List<XWPFRun> runs = para.getRuns();
			for (int i = 0; i < runs.size(); i++) {
				XWPFRun run = runs.get(i);
				String runText = run.toString();
				for (String pattern : params.keySet()) {
					if (runText.contains(pattern)) {
						runText = runText.replaceFirst(pattern, params.get(pattern));
						para.removeRun(i);
						if (runText.equals("null")) {
							runText = "";
						}
						para.insertNewRun(i).setText(runText);
					}
				}
			}
		}
		writeToDocx();
		if (opEnd) {
			close();
		}
	}

	private void writeToDocx() {
		try {
			out = new FileOutputStream(file);
			doc.write(out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
