package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.good.GoodsDao;
import com.fmStore.core.dao.good.GoodsDescDao;
import com.fmStore.core.dao.item.ItemCatDao;
import com.fmStore.core.dao.item.ItemDao;
import com.fmStore.core.pojo.good.Goods;
import com.fmStore.core.pojo.good.GoodsDesc;
import com.fmStore.core.pojo.item.Item;
import com.fmStore.core.pojo.item.ItemCat;
import com.fmStore.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class CmsServiceImpl implements CmsService, ServletContextAware {
    @Autowired
    private GoodsDao goodsDao;
    @Autowired
    private GoodsDescDao goodsDescDao;
    @Autowired
    private ItemDao itemDao;
    @Autowired
    private ItemCatDao itemCatDao;
    @Autowired
    private FreeMarkerConfig freeMarkerConfig;

    private ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public Map<String, Object> getGoodsDate(Long goodsId) {
        HashMap<String, Object> resMap = new HashMap<>();
        // 获取商品数据
        Goods goods = goodsDao.selectByPrimaryKey(goodsId);
        // 商品详情
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(goodsId);
        // 库存数据
        ItemQuery itemQuery = new ItemQuery();
        ItemQuery.Criteria criteria = itemQuery.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        List<Item> items = itemDao.selectByExample(itemQuery);
        // 获取商品对应的分类名称
        if (goods != null) {
            ArrayList<String> categoryList = new ArrayList<>();
            ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());
            ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());
            ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
            categoryList.add(itemCat1.getName());
            categoryList.add(itemCat2.getName());
            categoryList.add(itemCat3.getName());
            resMap.put("categoryList", categoryList);
        }

        // 添加数据
        resMap.put("goods", goods);
        resMap.put("goodsDesc", goodsDesc);
        resMap.put("itemList", items);
        return resMap;
    }

    @Override
    public void createStaticPage(Long goodsId, Map<String, Object> goodsData) {
        // 根据id和数据生成静态页面
        // 获取配置对象
        Configuration configuration = freeMarkerConfig.getConfiguration();
        // 获取模板对象
        try {
            Template template = configuration.getTemplate("item.ftl");

            // 创建生成的静态页面
            String newPage = goodsId + ".html";
            System.out.println("---------" + newPage + "---------");
            String homePath = servletContext.getRealPath(newPage);
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(new File(homePath)), "utf-8");
            template.process(goodsData, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
