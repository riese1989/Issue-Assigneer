package ru.pestov.alexey.plugins.spring.entity;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;

@Data
public class FormParam {
    @XmlElement
    private String system;

    @XmlElement
    private String typechange;

    @XmlElement
    private String step1;

    @XmlElement
    private String step2;

    @XmlElement
    private String step3;

}
