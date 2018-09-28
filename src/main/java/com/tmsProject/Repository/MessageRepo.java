package com.tmsProject.Repository;

import com.tmsProject.Entity.Message;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message,Long> {
    List<Message> findByCity(String city);

    @Override
    void deleteById(Long aLong);
}
