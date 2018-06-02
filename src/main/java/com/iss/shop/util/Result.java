package com.iss.shop.util;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Setter
@Getter
public class Result<T> {
    private String message;
    private Boolean value;
    private T data;
    private List<T> content;
}
