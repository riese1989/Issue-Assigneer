package ru.pestov.alexey.plugins.spring.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Property {
    public String getProperty(String name)  {
        InputStream fis;
        Properties property = new Properties();
        try {
            fis = Property.class.getClassLoader().getResourceAsStream("issue-assigneer.properties");
            property.load(fis);
            System.out.println(property.getProperty(name));
            return property.getProperty(name);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
            return new String();
        }
    }
}
