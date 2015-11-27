package com.cd.controller;

import com.alibaba.fastjson.JSON;
import com.cd.dao.impl.UserDaoImpl;
import com.cd.entity.User;
import com.cd.utils.ClientInfo;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yxl on 15-11-20.
 */
@Controller
@RequestMapping(value = "/userService")
public class LoginController {

    UserDaoImpl userDao;

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(HttpServletRequest request,HttpServletResponse response) throws Exception {
        String json = request.getParameter("login");
        User user = JSON.parseObject(json, User.class);
        Map<String, Object> map = new HashMap<String, Object>();
        int login = userDao.login(user);
        map.put("mark", login);
        String s = JSON.toJSONString(map);
        response.getWriter().print(s);
        response.getWriter().close();
        ClientInfo info = new ClientInfo(request);
        System.out.println(info.getExplorerName());
        //System.out.println(info.getOSName());
        String ip = info.getIp();
        System.out.println(ip);
        String address = info.getAddress();
        System.out.println(address);
    }
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String json = request.getParameter("login");
        String objectId = ObjectId.get() + "";
        User user = JSON.parseObject(json,User.class);
        String name = user.getName();
        User user1 = userDao.queryUserByName(name);
        Map<String, Object> map = new HashMap<String, Object>();
        if (user1==null){
            user.setId(objectId);
            user.setDate(new Date());
            try {
                userDao.register(user);
                map.put("mark", 1);
            }catch (Exception e){
                map.put("mark", 0);
            }
        }else {
            map.put("mark", 0);
            map.put("messages", "用户已存在");
        }
        response.getWriter().print(JSON.toJSON(map));
        response.getWriter().close();
    }
    @ModelAttribute
    public void autoService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        userDao = (UserDaoImpl) context.getBean("userDaoImpl");
        //放开下面注释的代码就可以支持跨域
		if (response instanceof javax.servlet.http.HttpServletResponse) {
			// "*"表明允许任何地址的跨域调用，正式部署时应替换为正式地址
			response.addHeader("Access-Control-Allow-Origin", "*");
		}

    }
}
