package officePoi;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;

import java.io.*;
import java.util.Map;

public class Doc extends Office {
    private HWPFDocument doc = null;

    public Doc(String path) {
        file = new File(path);
        try {
            in = new FileInputStream(file);
            doc = new HWPFDocument(in);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void replaceStr(Map<String, String> params, boolean opEnd) {
        Range range = doc.getRange();
        for (String pattern : params.keySet()) {
            range.replaceText(pattern, params.get(pattern));
        }
        writeToDoc();
        if (opEnd) {
            close();
        }
    }

    private void writeToDoc() {
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