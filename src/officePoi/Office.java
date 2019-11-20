package officePoi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Office {
    protected FileInputStream in = null;
    protected FileOutputStream out = null;
    protected File file = null;
    public final boolean CLOSE = true;


    public void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}