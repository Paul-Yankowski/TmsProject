package com.tmsProject.Repository;

import com.tmsProject.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<User,String> {
User findUserByName(String name);
User findUserByEmail(String name);
}
