import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class MainJFame {
    public JFrame mainJFrame = new JFrame();
    public String workSpaceConfFileStr = null;
    public String tmpDirStr = null;
    public ArrayList<String> workSpaceDirList = new ArrayList<>();

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
            loadWorkSpaceDir();
        } catch (FileNotFoundException e) {
            System.out.println("工作区定义文件缺失");
            System.exit(1);
        } catch (IOException ioe) {
            System.out.println("工作区定义文件读取失败");
            System.exit(1);
        }
        onStart();
        mainJFrame.setSize(350, 650);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.setVisible(true);
    }

    public void onStart(){
        //移动工作目录文件至临时文件夹
        for (String dir:workSpaceDirList) {
            File file = new File(dir);
            if(file.isDirectory()){
                File[] files = file.listFiles();
                for (File file1:files) {
                    try {
                        moveFile(file1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else {

            }
        }
    }

    private void moveFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        FileOutputStream fileOutputStream = new FileOutputStream(tmpDirStr);
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

    public void loadWorkSpaceDir() throws IOException {
        //获取工作目录列表
        InputStream inputStream = new FileInputStream(workSpaceConfFileStr);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String buffer = null;
        while ((buffer=bufferedReader.readLine()) != null) {
            workSpaceDirList.add(buffer);
        }
    }
}
