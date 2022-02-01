package ru.pestov.alexey.plugins.spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Param {
    @NonNull
    private String system;
    @NonNull
    private String typeChange;

    private List<String> stage1;

    private List<String> stage21;

    private List<String> stage22;

    private List<String> stage23;

    private List<String> stage3;

    private List<String> authorize;

    private String active;

}
