package com.mawadda.service;

import com.mawadda.entity.Parent;

import java.util.List;

public interface IParentService {
    Parent add(Parent parent);
    Parent modify(Integer id, Parent parent);
    Parent show(Integer id);
    Parent showByCin(String cin);
    List<Parent> showAll();
    void delete(Integer id);
}
