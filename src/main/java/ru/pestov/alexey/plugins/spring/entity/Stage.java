package ru.pestov.alexey.plugins.spring.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@XmlRootElement
@AllArgsConstructor
public class Stage {

    @XmlElement
    private String name;

    @XmlElement
    private String assignees;
}
