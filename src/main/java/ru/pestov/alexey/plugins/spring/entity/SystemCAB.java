package ru.pestov.alexey.plugins.spring.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonRootName;


@Data
@RequiredArgsConstructor
@XmlRootElement
public class SystemCAB {

    @NonNull
    @XmlElement
    private String name;
    private List<TypeChange> typeChanges = new ArrayList<>();

    @XmlElementWrapper(name="typeChanges")
    @XmlElement
    public List<TypeChange> getTypeChanges() {
        return typeChanges;
    }

    public void addType(TypeChange typeChange)  {
        typeChanges.add(typeChange);
    }
}
