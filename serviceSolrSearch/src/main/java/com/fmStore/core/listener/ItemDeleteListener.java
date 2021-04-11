package com.fmStore.core.listener;

import com.fmStore.core.service.UpdateSolrService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class ItemDeleteListener implements MessageListener {
    @Autowired
    private UpdateSolrService updateSolrService;
    @Override
    public void onMessage(Message message) {
        ActiveMQObjectMessage atm = (ActiveMQObjectMessage) message;
        try {
            Long[] object = (Long[]) atm.getObject();
            updateSolrService.deleteGoods(object);
            System.out.println("+++++++++"+"删除成功solr"+"+++++++++");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
