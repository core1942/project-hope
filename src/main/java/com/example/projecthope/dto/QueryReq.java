package com.example.projecthope.dto;

import com.example.projecthope.entity.ProjectHope;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QueryReq extends PageQuery {
    private ProjectHope projectHope;
}
