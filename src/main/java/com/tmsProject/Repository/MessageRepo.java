package com.tmsProject.Repository;

import com.tmsProject.Entity.Message;
import org.springframework.data.repository.CrudRepository;

public interface MessageRepo extends CrudRepository<Message,Long> {
}
