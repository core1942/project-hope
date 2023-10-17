package com.example.projecthope.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadDetail {
    private int successCount;

    private int errCount;

    private String tempFileName;

    public void addSuccess() {
        this.successCount++;
    }

    public void addErr() {
        this.errCount++;
    }
}
