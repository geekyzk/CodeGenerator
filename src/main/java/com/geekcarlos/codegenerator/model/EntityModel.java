package com.geekcarlos.codegenerator.model;


import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Getter
@ToString
public class EntityModel {

    @NonNull
    private String name;

    private String tableName;

    @Builder.Default
    private List<EntityItemModel> entityItems = new ArrayList<>();
}
