package com.fmStore.core.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fmStore.core.pojo.entity.Result;
import com.fmStore.core.pojo.user.User;
import com.fmStore.core.service.UserService;
import com.fmStore.core.utils.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserWeb {
    @Reference
    private UserService userService;

    // 发送验证码
    @RequestMapping("/sendCode")
    public Result sendCode(String phone) {
        Result result = new Result();
        try {
            if (phone == null || "".equals(phone)) {
                result.setMessage("手机号不能为空!");
                result.setIsSuccess(false);
            }
            if (!PhoneFormatCheckUtils.isPhoneLegal(phone)) {
                result.setMessage("手机号格式不正确!");
                result.setIsSuccess(false);
            }
            userService.sendCode(phone);
            result.setMessage("发送成功!");
            result.setIsSuccess(true);
        } catch (Exception e) {
            e.printStackTrace();
            result.setMessage("发送失败!");
            result.setIsSuccess(false);
        }
        return result;
    }

    // 注册用户
    @RequestMapping("/add")
    public void add(@RequestBody User user, String smsCode) {
        Boolean isCheck = userService.checkCode(user.getPhone(), smsCode);
        Result result = new Result();
        if (isCheck) {
            // 保存数据库
            try {
                userService.saveUser(user);
                result.setIsSuccess(false);
                result.setMessage("保存成功");
            } catch (Exception e) {
                e.printStackTrace();
                result.setIsSuccess(false);
                result.setMessage("保存失败");
            }
        } else {
            result.setIsSuccess(false);
            result.setMessage("验证码错误");
        }
    }
}
