package ru.pestov.alexey.plugins.spring.service;

import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;

import javax.inject.Named;

@Named
@ExportAsService
public class StringService {
    public String replaceDownToSpace(String string)    {
        return string.replaceAll("_", " ");
    }
    public String replaceSpaceToDown(String string)    {
        return string.replaceAll(" ", "_");
    }
}
