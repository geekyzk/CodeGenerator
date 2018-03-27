package com.geekcarlos.codegenerator.model;


import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class EntityItemModel {

    private String name;

    private Class typeClass;


    private String columnName;

    @Builder.Default
    private Boolean unique = false;

    @Builder.Default
    private Boolean nullable = false;
}
