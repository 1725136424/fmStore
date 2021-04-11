package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmStore.core.dao.good.BrandDao;
import com.fmStore.core.dao.good.GoodsDao;
import com.fmStore.core.dao.good.GoodsDescDao;
import com.fmStore.core.dao.item.ItemCatDao;
import com.fmStore.core.dao.item.ItemDao;
import com.fmStore.core.pojo.entity.GoodsEntity;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.pojo.good.*;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.pojo.item.ItemCat;
import com.fmStore.core.pojo.item.ItemQuery;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.lang.reflect.Array;
import java.util.*;

@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private BrandDao brandDao;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    // 商品上架使用
    private ActiveMQTopic topicPageAndSolrDestination;
    @Autowired
    // 商品下架使用
    private ActiveMQQueue queueSolrDeleteDestination;
    @Override
    public void saveGood(GoodsEntity goodEntity) {
        Goods goods = goodEntity.getGoods();
        GoodsDesc goodsDesc = goodEntity.getGoodsDesc();
        List<Item> items = goodEntity.getItems();
        // 保存商品
        // --> 设置商品的审核状态
        goods.setAuditStatus("0");
        // --> 设置删除信息
        goods.setIsDelete("0");
        goodsDao.insertSelective(goods);

        // 保存商品详情 --> 获取已经保存商品的主键
        Long goodsId = goods.getId();
        goodsDesc.setGoodsId(goodsId);
        goodsDescDao.insertSelective(goodsDesc);

        // 保存库存信息
        if ("1".equals(goods.getIsEnableSpec()) && items != null) {
            for (Item item: items) {
                // 初始化库存标题
                String specStr = item.getSpec();
                Map specMap = JSON.parseObject(specStr, Map.class);
                Collection mapValues = specMap.values();
                String title = goods.getGoodsName() + " " + goods.getCaption();
                for (Object str: mapValues) {
                    title = title + " " + str;
                }
                item.setTitle(title);
                // 初始化图片
                String itemImages = goodsDesc.getItemImages();
                List<Map> imageMaps = JSON.parseArray(itemImages, Map.class);
                item.setImage(imageMaps.get(0).get("url") + "");

                // 分类id 和分类名称
                Long category3Id = goods.getCategory3Id();
                if (category3Id != null) {
                    item.setCategoryid(category3Id);
                    ItemCat itemCat = itemCatDao.selectByPrimaryKey(category3Id);
                    item.setCategory(itemCat.getName());
                } else {
                    Long category2Id = goods.getCategory2Id();
                    if (category2Id != null) {
                        item.setCategoryid(category2Id);
                        ItemCat itemCat = itemCatDao.selectByPrimaryKey(category2Id);
                        item.setCategory(itemCat.getName());
                    } else {
                        Long category1Id = goods.getCategory1Id();
                        if (category1Id != null) {
                            item.setCategoryid(category1Id);
                            ItemCat itemCat = itemCatDao.selectByPrimaryKey(category1Id);
                            item.setCategory(itemCat.getName());
                        }
                    }
                }

                // 商品状态
                item.setStatus("0");

                // 创建时间
                item.setCreateTime(new Date());

                // 修改时间
                item.setUpdateTime(new Date());

                // 商品id
                item.setGoodsId(goods.getId());

                // 品牌名称
                Brand brand = brandDao.selectByPrimaryKey(goods.getBrandId());
                item.setBrand(brand.getName());
                // 设置商家
                item.setSellerId(goods.getSellerId());
                item.setSeller(goods.getSellerId());
                itemDao.insertSelective(item);
            }
        } else {
            Item item = new Item();
            item.setCreateTime(new Date());
            item.setUpdateTime(new Date());
            item.setGoodsId(goods.getId());
            itemDao.insertSelective(item);
        }

    }

    @Override
    public void editGoods(GoodsEntity goodsEntity) {
        Goods goods = goodsEntity.getGoods();
        GoodsDesc goodsDesc = goodsEntity.getGoodsDesc();
        List<Item> items = goodsEntity.getItems();
        // 保存商品
        // --> 设置商品的审核状态
        goods.setAuditStatus("0");
        // --> 设置删除信息
        goods.setIsDelete("0");
        goodsDao.updateByPrimaryKeySelective(goods);

        // 保存商品详情 --> 获取已经保存商品的主键
        Long goodsId = goods.getId();
        goodsDesc.setGoodsId(goodsId);
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);

        // 保存库存信息
        if ("1".equals(goods.getIsEnableSpec()) && items != null) {
            for (Item item: items) {
                // 初始化库存标题
                String specStr = item.getSpec();
                Map specMap = JSON.parseObject(specStr);
                Collection mapValues = specMap.values();
                String title = goods.getGoodsName() + " " + goods.getCaption();
                for (Object str: mapValues) {
                    title = title + " " + str;
                }
                item.setTitle(title);
                // 初始化图片
                String itemImages = goodsDesc.getItemImages();
                List<Map> imageMaps = JSON.parseArray(itemImages, Map.class);
                item.setImage(imageMaps.get(0).get("url") + "");

                // 分类id 和分类名称
                Long category3Id = goods.getCategory3Id();
                if (category3Id != null) {
                    item.setCategoryid(category3Id);
                    ItemCat itemCat = itemCatDao.selectByPrimaryKey(category3Id);
                    item.setCategory(itemCat.getName());
                } else {
                    Long category2Id = goods.getCategory2Id();
                    if (category2Id != null) {
                        item.setCategoryid(category2Id);
                        ItemCat itemCat = itemCatDao.selectByPrimaryKey(category2Id);
                        item.setCategory(itemCat.getName());
                    } else {
                        Long category1Id = goods.getCategory1Id();
                        if (category1Id != null) {
                            item.setCategoryid(category1Id);
                            ItemCat itemCat = itemCatDao.selectByPrimaryKey(category1Id);
                            item.setCategory(itemCat.getName());
                        }
                    }
                }

                // 商品状态
                item.setStatus("0");

                // 修改时间
                item.setUpdateTime(new Date());

                // 商品id
                item.setGoodsId(goods.getId());

                // 品牌名称
                Brand brand = brandDao.selectByPrimaryKey(goods.getBrandId());
                item.setBrand(brand.getName());
                // 设置商家
                item.setSellerId(goods.getSellerId());
                item.setSeller(goods.getSellerId());
                itemDao.updateByPrimaryKeySelective(item);
            }
        }
    }

    @Override
    public void deleteGoods(Long[] idx) {
        // 删除商品
        for (int i = 0; i < idx.length; i++ ) {
            Goods goods = new Goods();
            goods.setId(idx[i]);
            goods.setIsDelete("1");
            goodsDao.updateByPrimaryKeySelective(goods);
        }
    }

    @Override
    public void deleteGoodsRel(Long[] idx) {
        // 删除商品
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        goodsDao.deleteByExample(goodsQuery);

        // 删除商品详情
        GoodsDescQuery goodsDescQuery = new GoodsDescQuery();
        GoodsDescQuery.Criteria criteria1 = goodsDescQuery.createCriteria();
        criteria1.andGoodsIdIn(Arrays.asList(idx));

        // 删除库存
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria2 = itemQuery.createCriteria();
        criteria2.andGoodsIdIn(Arrays.asList(idx));
        itemDao.deleteByExample(itemQuery);
    }

    @Override
    public void passCommitGoods(final Long[] idx) {
        Goods goods = new Goods();
        goods.setAuditStatus("2");
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        goodsDao.updateByExampleSelective(goods, goodsQuery);

        // 通过库存
        Item item = new Item();
        item.setStatus("2");
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria1 = itemQuery.createCriteria();
        criteria1.andGoodsIdIn(Arrays.asList(idx));
        itemDao.updateByExampleSelective(item, itemQuery);

        // 发送消息
        jmsTemplate.send(topicPageAndSolrDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(idx);
                return objectMessage;
            }
        });
    }

    @Override
    public void rollbackGoods(final Long[] idx) {
        Goods goods = new Goods();
        goods.setAuditStatus("3");
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        goodsDao.updateByExampleSelective(goods, goodsQuery);

        // 通过库存
        Item item = new Item();
        item.setStatus("3");
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria1 = itemQuery.createCriteria();
        criteria1.andGoodsIdIn(Arrays.asList(idx));
        itemDao.updateByExampleSelective(item, itemQuery);

        jmsTemplate.send(queueSolrDeleteDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                ObjectMessage objectMessage = session.createObjectMessage(idx);
                return objectMessage;
            }
        });
    }

    @Override
    public List<Item> findCommitGoodsById(Long[] idx) {
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdIn(Arrays.asList(idx));
        criteria.andStatusEqualTo("2");
        List<Item> items = itemDao.selectByExample(itemQuery);
        return items;
    }

    @Override
    public PageResult getCommitGoods(Goods goods, Integer page, Integer pageSize) {
        GoodsQuery goodsQuery = new GoodsQuery();
        GoodsQuery.Criteria criteria = goodsQuery.createCriteria();
        if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName())) {
            // 添加条件
            criteria.andGoodsNameLike("%"+goods.getGoodsName() +"%");
        }
        if (goods.getAuditStatus() != null && !"".equals(goods.getAuditStatus())) {
            // 添加条件
            criteria.andAuditStatusEqualTo(goods.getAuditStatus());
        }
        if (!"admin".equals(goods.getSellerId())) {
            criteria.andSellerIdEqualTo(goods.getSellerId());
            criteria.andIsDeleteEqualTo("0");
        }
        PageHelper.startPage(page, pageSize);
        Page<Goods> goods1 = (Page<Goods>) goodsDao.selectByExample(goodsQuery);
        PageResult pageResult = new PageResult();
        pageResult.setRows(goods1.getResult());
        pageResult.setTotal(goods1.getTotal());
        return pageResult;
    }

    @Override
    public GoodsEntity getGoods(Long id) {
        // 获取商品
        Goods goods = goodsDao.selectByPrimaryKey(id);
        // 获取商品详情
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        // 获取库存 -> 通过商品主键获取
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(itemQuery);
        GoodsEntity goodsEntity = new GoodsEntity();
        goodsEntity.setGoods(goods);
        goodsEntity.setGoodsDesc(goodsDesc);
        goodsEntity.setItems(items);
        return goodsEntity;
    }

}
