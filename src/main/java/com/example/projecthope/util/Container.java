package com.example.projecthope.util;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Container {
    private int it;

    public Container(int it) {
        this.it = it;
    }

    public int inc() {
        return ++it;
    }
}
