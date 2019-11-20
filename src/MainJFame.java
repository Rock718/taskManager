import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MainJFame {
    public JFrame mainJFrame = new JFrame();
    public String workSpaceConfFileStr = null;
    public String tmpDirStr = null;
    public ArrayList<Task> taskList = new ArrayList<>();
    private JTextField searchField = new JTextField("搜索...");
    private JTable taskTable;
    private String[] columuNames = {"任务","完成情况"};
    private ArrayList<Object[]> tableData = new ArrayList<Object[]>();

    public MainJFame() {
        try {
            loadProperty();
        } catch (FileNotFoundException e) {
            System.out.println("配置文件缺失");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("配置文件读取失败");
            System.exit(1);
        }
        try {
            loadTaskFiles();
        } catch (FileNotFoundException e) {
            System.out.println("工作区定义文件缺失");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("工作区定义文件读取失败");
            System.exit(1);
        }
        onStart();
        mainJFrame.setLayout(null);

        searchField.setBounds(10,10,330,30);
        Object[] test = {"任务1",new JCheckBox()};
        tableData.add(test);
        taskTable = new JTable(new DefaultTableModel((Object[][])tableData.toArray(new Object[tableData.size()][2]), columuNames));
        taskTable.setBounds(10,60,330,60);
        mainJFrame.add(searchField);
        mainJFrame.add(taskTable);
        mainJFrame.setSize(350, 650);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.setVisible(true);
    }

    public void onStart(){
        //移动工作目录文件至临时文件夹
        File tmpDir = new File(tmpDirStr);
        if(!tmpDir.isDirectory()){
            tmpDir.mkdir();
        }

        for (Task dir: taskList) {
            File file = new File(dir.filePath);
            String path = file.getAbsolutePath();
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (File file1:files) {
                    try {
                        moveFile(file1);
                    } catch (IOException e) {
                        System.out.println("文件不存在");
                        e.printStackTrace();
                    }
                }
            }else {
                try {
                    moveFile(file);
                } catch (IOException e) {
                    System.out.println("文件不存在");
                    e.printStackTrace();
                }
            }
        }
    }

    private void moveFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        File newFile = new File(tmpDirStr+"/"+file.getName());
        if(!newFile.exists()){
            newFile.createNewFile();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(newFile);
        byte[] buffer = new byte[4096];
        while (fileInputStream.read(buffer) != -1){
            fileOutputStream.write(buffer);
        }
        fileOutputStream.close();
        fileInputStream.close();
    }

    public void loadProperty() throws IOException {
        //读取配置文件
        Properties properties = new Properties();
        InputStream inputStream = new FileInputStream("prop.conf");
        properties.load(inputStream);
        workSpaceConfFileStr = properties.getProperty("WorkSpaceConfFile");
        tmpDirStr = properties.getProperty("TmpDir");
        inputStream.close();
    }

    public void loadTaskFiles() throws IOException {
        //获取工作目录列表
        InputStream inputStream = new FileInputStream(workSpaceConfFileStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String buffer = null;
        while ((buffer=bufferedReader.readLine()) != null) {
            JSONObject jsonObject = JSONObject.parseObject(buffer);
            String filePath = jsonObject.keySet().iterator().next();
            Map<String,String> replacePattern = new HashMap<>();
            JSONArray jsonArray = jsonObject.getJSONArray(filePath);
            for(int i = 0; i< jsonArray.size();i++){
                Map<String,Object> map = (Map<String, Object>) jsonArray.get(i);
                String pattern = map.keySet().iterator().next();
                replacePattern.put(pattern,map.get(pattern).toString());
            }
            taskList.add(new Task(filePath,replacePattern));
        }
    }
}
