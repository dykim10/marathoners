package com.project.marathon.dto;

import com.project.marathon.entity.MarathonEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true) // 부모 클래스 필드까지 포함
public class MarathonEventDto extends MarathonEvent {
    // 필요한 추가 필드가 있다면 여기에 추가
}
