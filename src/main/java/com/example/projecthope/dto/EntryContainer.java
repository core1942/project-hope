package com.example.projecthope.dto;

import com.example.projecthope.entity.ProjectHope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntryContainer<T> {
    private int rowIndex;

    private T data;
}
