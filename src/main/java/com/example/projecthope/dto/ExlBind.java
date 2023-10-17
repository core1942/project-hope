package com.example.projecthope.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Field;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExlBind extends Bind {

    private Integer cellIndex;

    public ExlBind(Bind bind) {
        super(bind.getHeaderName(), bind.isRequired(), bind.getField());
    }
}
