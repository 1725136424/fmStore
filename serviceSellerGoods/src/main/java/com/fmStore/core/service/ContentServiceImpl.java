package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.fmStore.core.dao.ad.ContentDao;
import com.fmStore.core.pojo.ad.Content;
import com.fmStore.core.pojo.ad.ContentQuery;
import com.fmStore.core.pojo.entity.PageResult;
import com.fmStore.core.utils.RedisConst;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageResult getContent(Content content, Integer page, Integer pageSize) {
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        if (content != null) {
            if (content.getTitle() != null && !"".equals(content.getTitle())) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
        }
        PageHelper.startPage(page, pageSize);
        Page<Content> contents = (Page<Content>) contentDao.selectByExample(contentQuery);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(contents.getTotal());
        pageResult.setRows(contents.getResult());
        return pageResult;
    }

    @Override
    public void saveContent(Content content) {
        contentDao.insertSelective(content);

        // 获取广告分类 --> 添加至Redis中
        redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).delete(content.getCategoryId());
    }

    @Override
    public void deleteContent(Long[] idx) {

        // 同步Redis数据
        for (Long id: idx) {
            Content content = contentDao.selectByPrimaryKey(id);

            // 获取广告分类 --> 添加至Redis中
            redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).
                    delete(content.getCategoryId());
        }

        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andIdIn(Arrays.asList(idx));
        contentDao.deleteByExample(contentQuery);
    }

    @Override
    public void editContent(Content content) {
        // 查询原来的分类
        Content originContent = contentDao.selectByPrimaryKey(content.getCategoryId());

        // 同步Redis数据
        redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).
                delete(originContent.getCategoryId());

        redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).
                delete(content.getCategoryId());

        // 更新广告数据
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public List<Content> getContentByCategory(Long categoryId) {
        ContentQuery contentQuery = new ContentQuery();
        ContentQuery.Criteria criteria = contentQuery.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        return contentDao.selectByExample(contentQuery);
    }

    @Override
    public List<Content> getContentByCategoryFromRedis(Long categoryId) {
        // 添加Redis数据缓存
        // 从分类id获取数据
        List<Content> contents = (List<Content>) redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).get(categoryId);

        // 如果没有数据则从数据库中取出
        if (contents == null) {
            contents = getContentByCategory(categoryId);
            // 保存至Redis
            redisTemplate.boundHashOps(RedisConst.CONTENT_LIST_REDIS).put(categoryId, contents);
        }
        return contents;
    }
}
