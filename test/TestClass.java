import com.Astar.infoClass.FileSliceInfo;
import com.Astar.infoClass.Log;
import com.Astar.resource.Constant;
import com.Astar.tools.FileSliceTool;
import com.Astar.tools.TcpConnectionTool;
import org.junit.Test;

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
}
