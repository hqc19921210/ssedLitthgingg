import com.heqichao.springBootDemo.SpringBootDemoApplication;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.DataCacheUtil;
import com.heqichao.springBootDemo.module.onenet.OneNetConfig;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by heqichao on 2019-6-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class EquipmentTest {
    @Autowired
    private DataLogService dataLogService;
    @Test
    public void getEquipmentTest(){
        String devId="e5bc3cb3-ee1a-4861-a296-714a4c5acc26";
        Equipment equipment =DataCacheUtil.getEquipmentCache(devId);
        Assert.assertTrue(equipment != null);
    }

    @Test
    public void getKeyTest(){
        String devId = "e5bc3cb3-ee1a-4861-a296-714a4c5acc26";
        String data ="0103006801EC01E701EB01EA01E701EB01E801E901EA01E701EC01E701EB01EA01E701EB01E801E901EA01E701EC01E801E901EB01E701EC01E901E801EB01E701EB01E901E701EC01E701E801E901E801EB01E701EB01E901E701EC01E701EA01EB01E701EB01E801E901EB669D";
        dataLogService.sendToOneNet(devId, EquipmentService.EQUIPMENT_NB,data,1);
    }


}
