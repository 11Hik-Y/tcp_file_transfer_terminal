import com.Astar.tools.TcpConnectionTool;
import org.junit.Test;

public class TestClass {

    @Test
    public void ipTest() {
        String ip = TcpConnectionTool.getIPAddress();
        System.out.println(ip);
    }
}
