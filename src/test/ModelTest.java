import com.heqichao.springBootDemo.SpringBootDemoApplication;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 测试模板
 * Created by heqichao on 2018-12-1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class ModelTest {

    @Autowired
    private DataLogService dataLogService;

    @Test
    public void  saveDataLog(){
       // dataLogService.saveDataLog("1","AQMAGQABGAUkFhYj+xkE5gAQDwIO/lgFAAEAAgADAAQABWQf","TEST");
       // dataLogService.saveDataLog("1807238000000188","{\"devEUI\":\"1807238000000188\",\"time\":\"2018-12-16T08:07:19.821003Z\",\"fPort\":8,\"gatewayCount\":1,\"rssi\":-39,\"fCnt\":982,\"loRaSNR\":8.8,\"data\":\"AQMGAAGGoAAB9B8=\"}","TEST");
        dataLogService.saveDataLog("e5bc3cb3-ee1a-4861-a296-714a4c5acc26","010304000028A0E44B",null,"TEST");

    }
}
