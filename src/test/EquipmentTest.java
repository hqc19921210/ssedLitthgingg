import com.heqichao.springBootDemo.SpringBootDemoApplication;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.util.DataCacheUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by heqichao on 2019-6-8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class EquipmentTest {

    @Test
    public void getEquipmentTest(){
        String devId="e5bc3cb3-ee1a-4861-a296-714a4c5acc26";
        Equipment equipment =DataCacheUtil.getEquipmentCache(devId);
        Assert.assertTrue(equipment != null);
    }
}
