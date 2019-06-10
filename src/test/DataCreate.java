import com.heqichao.springBootDemo.SpringBootDemoApplication;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.mapper.EquipmentMapper;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.DateUtil;
import com.heqichao.springBootDemo.base.util.MathUtil;
import com.heqichao.springBootDemo.module.entity.DataDetail;
import com.heqichao.springBootDemo.module.entity.DataLog;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.DataLogMapper;
import com.heqichao.springBootDemo.module.mapper.ModelMapper;
import com.heqichao.springBootDemo.module.model.AttrEnum;
import com.heqichao.springBootDemo.module.model.ModelUtil;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import com.heqichao.springBootDemo.module.service.ModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * 创建测试数据
 * Created by heqichao on 2019-5-26.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringBootDemoApplication.class)
public class DataCreate {
    @Autowired
    private ModelAttrService modelAttr1Service;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private DataLogMapper dataLogMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Autowired
    private DataLogService dataLogService;
    
    private final static  ModelAttr modelAttr1 =new ModelAttr();
    private final static  ModelAttr modelAttr2 =new ModelAttr();
    private final static  ModelAttr modelAttr3 =new ModelAttr();
    private final static  ModelAttr modelAttr4 =new ModelAttr();
    private final static  ModelAttr modelAttr5 =new ModelAttr();
    private final static  ModelAttr modelAttr6 =new ModelAttr();
    private final static  ModelAttr modelAttr7 =new ModelAttr();
    private final static  ModelAttr modelAttr8 =new ModelAttr();
    private final static  ModelAttr modelAttr9 =new ModelAttr();
    private final static  ModelAttr modelAttr10 =new ModelAttr();
    
    static {
        modelAttr1.setAttrName("测试1");
        modelAttr1.setExpression(" X + 10 ");
        modelAttr1.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr1.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr1.setUnit("A");

        modelAttr2.setAttrName("测试2");
        modelAttr2.setExpression(" X - 10 ");
        modelAttr2.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr2.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr2.setUnit("us");

        modelAttr3.setAttrName("测试3");
        modelAttr3.setExpression(" X / 10 ");
        modelAttr3.setNumberFormat(3);
        modelAttr3.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr3.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr3.setUnit("ms");

        modelAttr4.setAttrName("测试4");
        modelAttr4.setExpression(" X * 2 ");
        modelAttr4.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr4.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr4.setUnit("V");

        modelAttr5.setAttrName("测试5");
        modelAttr5.setExpression(" X / 10 ");
        modelAttr5.setNumberFormat(2);
        modelAttr5.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr5.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr5.setUnit("H");

        modelAttr6.setAttrName("测试6");
        modelAttr6.setExpression(" X / 7 ");
        modelAttr6.setNumberFormat(3);
        modelAttr6.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr6.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr6.setUnit("us");

        modelAttr7.setAttrName("测试7");
        modelAttr7.setExpression(" X * 1.2 ");
        modelAttr7.setNumberFormat(3);
        modelAttr7.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr7.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr7.setUnit("ms");

        modelAttr8.setAttrName("测试8");
        modelAttr8.setExpression(" X + 1.2 ");
        modelAttr8.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr8.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr8.setUnit("php");

        modelAttr9.setAttrName("测试9");
        modelAttr9.setExpression(" X / 1.5  ");
        modelAttr9.setNumberFormat(2);
        modelAttr9.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr9.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType());
        modelAttr9.setUnit("C");

        modelAttr10.setAttrName("测试10");
        modelAttr10.setExpression(" X / 3 ");
        modelAttr10.setNumberFormat(3);
        modelAttr10.setDataType(AttrEnum.INT_TYPE__TWO_UNSIGNED.getType());
        modelAttr10.setValueType(AttrEnum.INT_TYPE__TWO_UNSIGNED    .getSubType());
        modelAttr10.setUnit("W");


        // dev



    }

    public static  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    public static Date start;//构造开始日期
    public static Date end;//构造结束日期

    static {
        try {
            end = format.parse("2019-06-01");
            start = format.parse("2018-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static List<String> expiraList =new ArrayList();

    static {
        String[] aaa = new String[]{"test_dev_nb_100","test_dev_nb_150","test_dev_nb_175","test_dev_nb_200","test_dev_nb_250","test_dev_nb_300","test_dev_nb_325","test_dev_nb_351","test_dev_nb_450","test_dev_nb_50","test_dev_nb_75","test_dev_lora_1","test_dev_lora_100","test_dev_lora_175","test_dev_lora_200","test_dev_lora_226","test_dev_lora_25","test_dev_lora_250","test_dev_lora_325","test_dev_lora_350","test_dev_lora_376","test_dev_lora_401","test_dev_lora_426","test_dev_lora_450","test_dev_lora_475","test_dev_lora_50","test_dev_lora_75","test_dev_nb_151","test_dev_lora_476","test_dev_nb_251","test_dev_lora_301","test_dev_lora_201","test_dev_nb_101","test_dev_nb_201","test_dev_nb_451","test_dev_lora_126","test_dev_nb_401","test_dev_nb_127","test_dev_lora_152","test_dev_nb_2","test_dev_nb_27","test_dev_nb_302","test_dev_lora_277","test_dev_nb_227","test_dev_nb_477","test_dev_nb_52","test_dev_nb_377","test_dev_nb_428","test_dev_lora_352","test_dev_lora_0","test_dev_nb_225","test_dev_nb_375","test_dev_nb_400","test_dev_lora_275","test_dev_lora_425","test_dev_lora_125","test_dev_nb_25","test_dev_nb_0","test_dev_nb_326","test_dev_nb_476","test_dev_nb_176","test_dev_lora_451","test_dev_lora_251","test_dev_lora_176","test_dev_nb_76","test_dev_nb_278","test_dev_lora_227","test_dev_lora_377","test_dev_lora_302","test_dev_lora_52","test_dev_lora_102","test_dev_lora_77","test_dev_lora_327","test_dev_lora_27","test_dev_nb_425","test_dev_nb_275","test_dev_nb_350","test_dev_lora_300","test_dev_nb_125","test_dev_lora_400","test_dev_lora_225","test_dev_nb_475","test_dev_lora_375","test_dev_lora_150"};
        expiraList =new ArrayList<>( Arrays.asList(aaa));
    }

    @Test
    public void createModelTest(){
        saveModelTest(10001,"TEST_MODEL_1",modelAttr1,modelAttr2,modelAttr3,modelAttr4,modelAttr5,modelAttr6,modelAttr7,modelAttr8,modelAttr9,modelAttr10);
    }

    @Test
    public void createDevTest(){
        for(int i=0;i<500;i++){
            saveEqu("test_dev_lora_"+i,"测试设备_LORA_"+i,10001,"L");
        }
        for(int i=0;i<500;i++){
            saveEqu("test_dev_nb_"+i,"测试设备_NB_"+i,10001,"N");
        }

    }



    @Test
    public void createDataTest(){
        System.out.println("start ");
        Date date =new Date();

        ExecutorService executor = Executors.newCachedThreadPool();
        int loarNum =5000;  //每个loar设备的数据量
        int loarSumDevNum =500; //loar总设备数
        int loarThreadNum =50; //loar线程数
        int loarPerDevNum=loarSumDevNum/loarThreadNum; //每个loar线程的处理设备数

        int nbNum =5000;  //每个nb设备的数据量
        int nbSumDevNum =500; //nb总设备数
        int nbThreadNum =50; //nb线程数
        int nbPerDevNum=nbSumDevNum/nbThreadNum; //每个nb线程的处理设备数

        CountDownLatch loarCountDownLatch =new CountDownLatch(loarThreadNum);
        CountDownLatch nbCountDownLatch =new CountDownLatch(loarThreadNum);

        for(int i =0;i<loarThreadNum;i++){
            int ii =i;
            Future f =executor.submit(new Runnable() {
                @Override
                public void run() {
                    int item =ii * loarPerDevNum;
                    for(int j=0;j<loarPerDevNum;j++){
                        creatDataDetail("test_dev_lora_"+(item+j),loarNum);
                    }
                    loarCountDownLatch.countDown();
                }
            });
        }


        for(int i =0;i<nbThreadNum;i++){
            int ii =i;
            Future f =executor.submit(new Runnable() {
                @Override
                public void run() {
                    int item =ii * nbPerDevNum;
                    for(int j=0;j<nbPerDevNum;j++){
                        creatDataDetail("test_dev_nb_"+(item+j),nbNum);
                    }
                    nbCountDownLatch.countDown();
                }
            });
        }


        try {
            loarCountDownLatch.await();
            nbCountDownLatch.await();
            System.out.println("end " +(DateUtil.minBetween(new Date(),date)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void creatDataDetail(String devId,int rowNum){
        try {
            if(expiraList.contains(devId)){
                return;
            }
            Equipment equipment = equipmentMapper.getEquById(devId);
            List<ModelAttr> attrList =modelAttr1Service.queryByModelId(equipment.getModelId());


            for(int num=0;num<rowNum;num++){
                List<DataDetail> dataDetailList =new ArrayList<>(attrList.size());
                Date date =randomDate();
                String src = "";
                String[] cs =new String[attrList.size()];
                for(int ii=0;ii<attrList.size();ii++){
                    String c=getContent();
                    cs[ii]=c;
                    src=src+c;
                }
                DataLog dataLog =new DataLog();
                dataLog.setDataStatus("N");
                dataLog.setDevType(equipment.getTypeCd());
                dataLog.setData("TEST_DATA");
                dataLog.setSrcData("TEST_DATA");
                dataLog.setDevId(devId);
                dataLog.setSrcData(src);
                dataLog.setAddDate(date);
                dataLog.setUdpDate(date);
                dataLogMapper.save(dataLog);
                int id =dataLog.getId();
                for(int jj =0;jj<attrList.size();jj++){
                    ModelAttr attr =attrList.get(jj);
                    String content= cs[jj];
                    AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getDataType(),attr.getValueType());
                    DataDetail dataDetail =new DataDetail();
                    dataDetail.setAttrId(attr.getId());
                    dataDetail.setDataStatus("N");
                    dataDetail.setUnit(attr.getUnit());
                    dataDetail.setDataName(attr.getAttrName());
                    dataDetail.setDataType(attrEnum.getType());
                    dataDetail.setDevId(devId);
                    dataDetail.setAddDate(date);
                    dataDetail.setUdpDate(date);
                    dataDetail.setDataSrc(content);
                    String res = ModelUtil.getData(attr,content);
                    dataDetail.setDataValue(res);
                    dataDetail.setLogId(id);
                    dataDetailList.add(dataDetail);
                }
                dataLogService.save(dataDetailList,null);

            }
        }catch (Exception e){

        }


        /*try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }


    private void saveModelTest(Integer id,String modelName,ModelAttr... attrs){
        Date date =new Date();
        Model model1 =new Model();
        model1.setId(id);
        model1.setModelName(modelName);
        model1.setAddDate(date);
        model1.setUdpDate(date);
        List<ModelAttr> modelAttr1List =new ArrayList<>();
        modelAttr1List.addAll(Arrays.asList(attrs));
        for(ModelAttr modelAttr : modelAttr1List){
            modelAttr.setModelId(model1.getId());
            modelAttr.setAddDate(date);
            modelAttr.setMemo("SSET测试——");
        }
        modelAttr1Service.saveModelAttr(modelAttr1List);
        modelMapper.saveModel(model1);
    }

    private String getContent(){
        int num = (int) (Math.random()*(500-1)+1);
        return  String.format("%04x", num);
    }



    private void saveEqu(String devId,String name,Integer modelId,String type){
        Equipment equipment =new Equipment();
        equipment.setModelId(modelId);
        equipment.setDevId(devId);
        equipment.setName(name);
        equipment.setOnline(EquipmentService.ON_LINE);
        equipment.setValid("N");
        equipment.setUid(1);
        equipment.setTypeCd(type);
        equipment.setAddDate(new Date());
        equipment.setUdpDate(new Date());
        equipment.setGroupId(1);
        equipment.setGroupAdmId(1);
        equipmentMapper.insertEquipment(equipment);
    }

    private static Date randomDate(){
        long date = random(start.getTime(),end.getTime());
        return new Date(date);
    }

    private static long random(long begin,long end){
        long rtn = begin + (long)(Math.random() * (end - begin));
        if(rtn == begin || rtn == end){
            return random(begin,end);
        }
        return rtn;
    }



}
