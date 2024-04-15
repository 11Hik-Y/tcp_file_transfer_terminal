import com.Astar.App;
import com.Astar.annotation.Paramable;
import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.tools.FileSliceTool;
import com.Astar.tools.TcpConnectionTool;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

public class TestClass {

    @Test
    public void ipTest() {
        String ip = TcpConnectionTool.getIPAddress();
        System.out.println(ip);
    }

    @Test
    public void fileSliceTest() {
        ArrayList<FileSliceInfo> fileSliceInfos = FileSliceTool.fileSlice(
                "C:\\Users\\ASUS\\PycharmProjects\\THUDM\\ChatGpt\\windows\\app\\ChatGpt\\src\\ChatGpt.exe",
                6);
        Log.info("切分文件成功，切分后的文件信息为 {}\n", fileSliceInfos);
    }

    @Test
    public void getFieldTest() {
        Field[] fields = Constant.Param.class.getFields();
        for (Field field : fields) {
            System.out.println(field);
        }
    }

    @Test
    public void serverTest() {
        App.main(new String[]{"--type=server",
                "--slicenum=7",
                "--path=F:\\AKI online\\Client 3.8.0\\Client.0.14.1.2.29197.zip"});
    }

    @Test
    public void clientTest() {
        App.main(new String[]{"--type=client",
                "--ip=192.168.35.1",
                "--slicenum=7",
                "--path=F:\\AKI online"});
    }
}
