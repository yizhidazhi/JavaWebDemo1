package com.example.Webapp.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Response<T> implements Serializable {

    //响应状态码
    private Integer code;

    //响应消息
    private String message;

    //响应数据
    private T data;


}
