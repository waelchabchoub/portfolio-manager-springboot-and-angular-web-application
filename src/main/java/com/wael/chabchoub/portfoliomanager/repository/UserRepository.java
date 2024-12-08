package com.wael.chabchoub.portfoliomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wael.chabchoub.portfoliomanager.entity.User;

public interface UserRepository extends JpaRepository<User,Long> {

}
