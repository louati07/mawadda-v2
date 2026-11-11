package com.mawadda.service;

import com.mawadda.entity.User;

import java.util.List;

public interface IUserService {
    User add(User user);
    User modify(Integer id, User user);
    User show(Integer id);
    User showByEmail(String email);
    List<User> showAll();
    void delete(Integer id);
}
