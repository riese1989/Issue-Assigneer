package ru.pestov.alexey.plugins.spring.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonRootName;
import org.json.simple.JSONObject;


@Data
@RequiredArgsConstructor
@XmlRootElement
public class SystemCAB implements GeneralClass {

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

    @Override
    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        JSONObject typeChangesJSON = new JSONObject();
        for (TypeChange typeChange : typeChanges)   {
            typeChangesJSON.put(typeChange.getName(), typeChange.toJson());
        }
        jsonObject.put(this.name, typeChangesJSON);
        return null;
    }
}
