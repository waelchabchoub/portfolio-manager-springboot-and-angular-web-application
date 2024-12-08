package com.wael.chabchoub.portfoliomanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.wael.chabchoub.portfoliomanager.entity.Admin;


public interface AdminRepository extends JpaRepository<Admin,Long> {
	@Query("SELECT a FROM Admin a where a.login = :login")
    Admin findByLogin(@Param("login") String login);
}
