package com.project.marathon.repository;

import com.project.marathon.entity.Message;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {

    List<Message> findAll();
    Optional<Message> findByContent(String content);
    Optional<Message> findById(Long id);
    void insert(Message message);
    void delete(Long id);

}
