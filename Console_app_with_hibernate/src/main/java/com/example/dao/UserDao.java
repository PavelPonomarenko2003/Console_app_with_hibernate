package com.example.dao;

import com.example.entity.UserEntity;

import java.util.List;


public interface UserDao{

    public void save(UserEntity user);

    public UserEntity update(UserEntity user);

    public void delete(Long id);

    public List<UserEntity> findAll();

    public UserEntity findById(Long id);

}