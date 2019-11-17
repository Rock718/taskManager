import officePoi.Doc;

import java.util.HashMap;
import java.util.Map;

public class Debug {
    public static void main(String args[]){
//        Docx docx = new Docx("C:\\Users\\luo_x\\Desktop\\1.docx");
//        Map<String, String> map = new HashMap<>();
//        map.put("你好", "hello world");
//        docx.replaceStr(map,docx.CLOSE);

        Doc doc = new Doc("C:\\Users\\luo_x\\Desktop\\1.doc");
        Map<String, String> map = new HashMap<>();
        map.put("你好", "hello world");
        doc.replaceStr(map, doc.CLOSE);
    }
}
