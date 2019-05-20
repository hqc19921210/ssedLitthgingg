package com.heqichao.springBootDemo.module.mqtt;

import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.LightningLogService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by heqichao on 2018-7-9.
 */

/**
 * 发布消息的回调类
 *
 * 必须实现MqttCallback的接口并实现对应的相关接口方法CallBack 类将实现 MqttCallBack。
 * 每个客户机标识都需要一个回调实例。在此示例中，构造函数传递客户机标识以另存为实例数据。
 * 在回调中，将它用来标识已经启动了该回调的哪个实例。
 * 必须在回调类中实现三个方法：
 *
 *  public void messageArrived(MqttTopic topic, MqttMessage message)接收已经预订的发布。
 *
 *  public void connectionLost(Throwable cause)在断开连接时调用。
 *
 *  public void deliveryComplete(MqttDeliveryToken token))
 *  接收到已经发布的 QoS 1 或 QoS 2 消息的传递令牌时调用。
 *  由 MqttClient.connect 激活此回调。
 *
 */
@Component
public class MqttUtilCallback implements MqttCallback {


    public static MqttUtilCallback mqttUtilCallback;

    public MqttUtilCallback(){}

    @PostConstruct
    public void init() {
        mqttUtilCallback = this;
        mqttUtilCallback.lightningLogService = this.lightningLogService;
        mqttUtilCallback.equipmentService = this.equipmentService;
        mqttUtilCallback.dataLogService=this.dataLogService;
    }

    @Autowired
    private LightningLogService lightningLogService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private DataLogService dataLogService;


    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void connectionLost(Throwable throwable) {
        // 连接丢失后，一般在这里面进行重连
        logger.error(" MQTT 连接异常断开，开始重连",throwable);
        MqttUtil.init();
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
// subscribe后得到的消息会执行到这里面
        String devId =topic.replace("application/0000000000000001/node/","");
        devId =devId.replace("/rx","");
        String mes =new String(message.getPayload());
        logger.info("接收消息主题 : " + topic);
        logger.info("接收消息Qos : " + message.getQos());
        logger.info("接收消息内容 : " + mes);
        mqttUtilCallback.dataLogService.saveDataLog(devId,mes,EquipmentService.EQUIPMENT_LORA);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        logger.info("deliveryComplete---------" + token.isComplete());
    }
}
