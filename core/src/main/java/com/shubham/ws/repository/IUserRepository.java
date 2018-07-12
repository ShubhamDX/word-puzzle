package com.shubham.ws.repository;

import com.shubham.ws.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author : shubham
 */
public interface IUserRepository extends JpaRepository<User, Long> {

}
