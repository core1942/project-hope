package com.example.projecthope.processor;

import com.example.projecthope.annotation.ExlBind;
import com.example.projecthope.dto.Bind;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnoProcessor {
    public static List<Bind> process(Class<?> tClass) {
        List<Bind> list = new ArrayList<>();
        Field[] declaredFields = tClass.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            ExlBind exlBind = declaredField.getAnnotation(ExlBind.class);
            if (exlBind != null) {
                declaredField.setAccessible(true);
                Bind bind = new Bind(exlBind.value(), exlBind.required(), declaredField);
                list.add(bind);
            }
        }
        return list;
    }
}
