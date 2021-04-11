package com.fmStore.core.listener;

import com.fmStore.core.service.CmsService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Map;

public class PageListener implements MessageListener {
    @Autowired
    private CmsService cmsService;
    @Override
    public void onMessage(Message message) {
        ActiveMQObjectMessage atm =   (ActiveMQObjectMessage)message;
        try {
            Long[] object = (Long[]) atm.getObject();
            for (Long aLong : object) {
                Map<String, Object> goodsDate = cmsService.getGoodsDate(aLong);
                cmsService.createStaticPage(aLong, goodsDate);
                System.out.println("++++"+"创建了静态页面了"+"++++");
            }

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
