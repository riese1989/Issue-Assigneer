package ru.pestov.alexey.plugins.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

@Data
@AllArgsConstructor
public class Param {
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
