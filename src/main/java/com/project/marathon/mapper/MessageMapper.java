package com.project.marathon.mapper;

import com.project.marathon.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageMapper {

    List<Message> findAll();
    Optional<Message> findByContent(String content);
    Optional<Message> findById(Long id);
    void insert(Message message);
    void delete(Long id);

}
