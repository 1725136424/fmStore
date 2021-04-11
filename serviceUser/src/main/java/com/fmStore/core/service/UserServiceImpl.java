package com.fmStore.core.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fmStore.core.dao.user.UserDao;
import com.fmStore.core.pojo.user.User;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    private ActiveMQQueue smsDestination;
    @Autowired
    private UserDao userDao;
    @Override
    public void sendCode(final String phone) {
        // 生成一个6为随机数 作为验证码
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < 7; i++) {
            int s = new Random().nextInt(10);
            sb.append(s);
        }
        // 手机号位key 验证码为value保存到redis，生存事件为10min
        redisTemplate.boundValueOps(phone).set(sb.toString(), 60 * 10, TimeUnit.SECONDS);

        final String smsCode = sb.toString();
        // 将手机号。短信内容，模板编号，签名封装map消息发送给服务器
        jmsTemplate.send(smsDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("mobile", phone); // 手机号
                mapMessage.setString("template_code", "SMS_169111508"); // 模板编码
                mapMessage.setString("sign_name", "疯码"); // 签名
                HashMap<String, String> codeMap = new HashMap<>();
                codeMap.put("code", smsCode); // 验证码必须放入map数组中
                mapMessage.setString("param", JSON.toJSONString(codeMap)); // 必须为一个字符串map
                return (Message) mapMessage;
            }
        });
    }

    @Override
    public Boolean checkCode(String phone, String smsCode) {
        // 取出redis中的验证码
        String redisCode = (String) redisTemplate.boundValueOps(phone).get();
        if (smsCode != null) {
            if (smsCode.equals(redisCode)) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public void saveUser(User user) {
        // 初始化数据
        user.setCreated(new Date());

        user.setUpdated(new Date());

        user.setSourceType("1");

        user.setNickName(user.getName());

        user.setStatus("Y");

        userDao.insertSelective(user);
    }
}
