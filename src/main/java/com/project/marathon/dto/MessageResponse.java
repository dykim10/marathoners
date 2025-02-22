package com.project.marathon.dto;

import com.project.marathon.entity.Message;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.ibatis.type.Alias;

@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
@Alias("MessageResponse")
public class MessageResponse extends Message {

}
