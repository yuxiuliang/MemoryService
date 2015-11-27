package com.cd.dao;

import com.cd.entity.User;

/**
 * Created by yxl on 15-11-20.
 */
public interface UserDao {
    int login(User user);
    void register(User user);
}
