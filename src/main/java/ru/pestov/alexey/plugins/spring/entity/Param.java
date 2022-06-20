package ru.pestov.alexey.plugins.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.pestov.alexey.plugins.spring.model.System;
import ru.pestov.alexey.plugins.spring.model.TypeChangeDB;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Param {
    @NonNull
    private Integer systemId;
    @NonNull
    private Integer typeChangeId;
    @NonNull
    private List<String> stage1;
    @NonNull
    private List<String> stage21;
    @NonNull
    private List<String> stage22;
    @NonNull
    private List<String> stage23;
    @NonNull
    private List<String> stage3;
    @NonNull
    private List<String> authorize;

    private String delivery;
    @NonNull
    private Boolean active;

    public List<String> getRequiredStage(String nameStage)  {
        switch (nameStage)  {
            case ("stage1"):    {
                return stage1;
            }
            case ("stage21"):    {
                return stage21;
            }
            case ("stage22"):    {
                return stage22;
            }
            case ("stage23"):    {
                return stage23;
            }
            case ("stage3"):    {
                return stage3;
            }
            case ("authorize"):    {
                return authorize;
            }
        }
        return null;
    }

}
