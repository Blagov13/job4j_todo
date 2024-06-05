package ru.job4j.todo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TimeZoneWrapper {
    private String id;
    private String displayName;
}
