package com.project.marathon.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class MarathonEvent {
    private Long id;
    private String name;
    private String date;
    private String location;
    private String website;
}
