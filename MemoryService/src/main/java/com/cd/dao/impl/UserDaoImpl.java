package com.cd.dao.impl;

import com.cd.dao.UserDao;
import com.cd.entity.User;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;

/**
 * Created by yxl on 15-11-20.
 */
public class UserDaoImpl implements UserDao{
    @Resource
    MongoTemplate mongoTemplate;
    public int login(User user) {
        Criteria criatira = new Criteria();
        criatira.andOperator(Criteria.where("name").is(user.getName()), Criteria.where("password").is(user.getPassword()));
        User one = mongoTemplate.findOne(new Query(criatira), User.class);
        return one != null ? 1 : 0;
    }

    /**
     * 用户注册
     * @param user 用户实体
     */
    public void register(User user) {
        mongoTemplate.insert(user);
    }

    /**
     * 根据用户名查找用户是否存在
     * @param name 用户名
     * @return 返回用户实体
     */
    public User queryUserByName(String name){
        return mongoTemplate.findOne(new Query(Criteria.where("name").is(name)), User.class);
    }
}
