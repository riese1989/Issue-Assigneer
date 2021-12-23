package ru.pestov.alexey.plugins.spring.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

@XmlRootElement
@AllArgsConstructor
@Data
public class Stage implements GeneralClass {

    @XmlElement
    private String name;

    @XmlElement
    private String assignees;

    @Override
    public JSONObject toJson() {
        return null;
    }
}
