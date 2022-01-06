package ru.pestov.alexey.plugins.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.XmlElement;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Param {
    @NonNull
    private String system;

    private String typeChange;

    private String step1;

    private String step21;

    private String step22;

    private String step23;

    private String step3;

    private String autorize;

    private String active;
}
