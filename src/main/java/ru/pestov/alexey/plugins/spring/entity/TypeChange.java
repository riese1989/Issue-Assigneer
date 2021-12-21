package ru.pestov.alexey.plugins.spring.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
@XmlRootElement
public class TypeChange {
    @NonNull
    @XmlElement
    private String name;
    protected List<Stage> stages = new ArrayList<>();

    @XmlElementWrapper(name="stages")
    @XmlElement
    public List<Stage> getStages() {
        return stages;
    }

    public void addStage(Stage stage)   {
        stages.add(stage);
    }
}
