package com.fmStore.core.listener;

import com.fmStore.core.dao.item.ItemDao;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.pojo.item.ItemQuery;
import com.fmStore.core.service.UpdateSolrService;
import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.Arrays;
import java.util.List;

public class ItemSearchListener implements MessageListener {
    @Autowired
    private UpdateSolrService updateSolrService;
    @Autowired
    private ItemDao itemDao;
    @Override
    public void onMessage(Message message) {
        ActiveMQObjectMessage atm =   (ActiveMQObjectMessage) message;
        try {
            Long[] object = (Long[]) atm.getObject();
            ItemQuery itemQuery = new ItemQuery();
            ItemQuery.Criteria criteria = itemQuery.createCriteria();
            criteria.andGoodsIdIn(Arrays.asList(object));
            List<Item> items = itemDao.selectByExample(itemQuery);
            if (items != null && items.size() > 0) {
                updateSolrService.addSolr(items);
            }
            System.out.println("+++++++++"+"添加成功solr"+"+++++++++");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
