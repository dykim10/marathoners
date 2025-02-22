package com.project.marathon.mapper;

import com.project.marathon.dto.MessageResponse;
import com.project.marathon.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MessageMapper {

    List<MessageResponse> findAll();
    Optional<Message> findByContent(String content);
    Optional<Message> findById(Long id);
    void insert(Message message);
    void delete(Long id);

}
