import com.Astar.App;
import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.tools.FileSliceTool;
import com.Astar.tools.TcpConnectionTool;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;

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
        App.main(new String[]{
                "--type=server",
                "--slicenum=4",
                "--path=F:\\AKI 3.7.0\\mod\\KAT2.2.5.zip",
                "--bufferSize=240"
        });
    }

    @Test
    public void clientTest() {
        App.main(new String[]{
                "--type=client",
                "--ip=localhost",
                "--slicenum=4",
                "--path=F:\\AKI 3.7.0"
        });
    }

    @Test
    public void serverTest2() {
        App.main(new String[]{
                "--type=server",
                "--path=F:\\AKI 3.7.0\\mod\\ATLAS-GEAR_1.0.4.zip",
                "--timeout=6000000"
        });
    }
}
