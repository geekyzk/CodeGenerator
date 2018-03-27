package com.geekcarlos.codegenerator.factory;

import com.geekcarlos.codegenerator.model.EntityItemModel;
import com.geekcarlos.codegenerator.model.EntityModel;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import com.xiaoleilu.hutool.util.StrUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.ClasspathResourceLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class GeneratorFactory {


    @NonNull
    private String packageName;

    @NonNull
    private String projectPath;

    private GroupTemplate groupTemplate;

    public GeneratorFactory(String packageName, String projectPath) throws IOException {
        this.packageName = packageName;
        this.projectPath = projectPath;
        ClasspathResourceLoader resourceLoader = new ClasspathResourceLoader("templates/");
        Configuration cfg = new Configuration();
        cfg.setHtmlTagStart("@");
        cfg.setHtmlTagEnd(null);
        this.groupTemplate = new GroupTemplate(resourceLoader, cfg);
    }

    @Setter
    @Getter
    private List<EntityModel> entityModelList = new ArrayList<>();

    private static final String repositoryFilePath = "/Repository.btl";
    private static final String controllerFilePath = "/Controller.btl";
    private static final String serviceFilePath = "/Service.btl";
    private static final String serviceImplFilePath = "/ServiceImpl.btl";
    private static final String repositoryFileName = "%sRepository.java";
    private static final String controllerFileName = "/Controller.btl";
    private static final String serviceFileName = "/Service.btl";
    private static final String serviceImplFileName = "/ServiceImpl.btl";


    public Boolean generatorCode() throws IOException {
        if (!FileUtil.exist(projectPath)) {
            log.info("ProjectPath not exist,will create project path");
            FileUtil.mkdir(projectPath);
        }
        if (FileUtil.isFile(projectPath)) {
            throw new RuntimeException("Project path is not a directory");
        }

        if (entityModelList.size() < 1) {
            throw new RuntimeException("You must setting EntityModel");
        }
        String packagePath = CollectionUtil.join(StrUtil.splitTrim(packageName, '.'), "/");
        String fullPath = projectPath.endsWith("/") ?projectPath + packagePath :projectPath + "/" + packagePath;
        log.info("Init project struct");
        String modelDirectory = fullPath + "/model";
        String repositoryDirectory = fullPath + "/repository";
        String controllerDirectory = fullPath + "/controller";
        String serviceDirectory = fullPath + "/service";
        String serviceImplDirectory = fullPath + "/service/impl";
        FileUtil.mkdir(modelDirectory);
        FileUtil.mkdir(controllerDirectory);
        FileUtil.mkdir(serviceDirectory);
        FileUtil.mkdir(serviceImplDirectory);

        entityModelList.stream()
                .peek(entityModel -> {
                    if (entityModel.getEntityItems().size() < 1) {
                        throw new RuntimeException("You must setting EntityModelItem in EntityModel");
                    }
                })
                .forEach(entityItem ->{
                    entityItem.getEntityItems()
                            .forEach(item -> {
                                if (item.getName().equals("id")) {
                                    throw new RuntimeException("Not setting id field,this default create id field for UUID");
                                }
                            });
                });
        renderCode(repositoryFilePath);
        return false;
    }

    public String renderCode(String templateFilePath) throws IOException {
        entityModelList
                .forEach(entityModel -> {
                    System.out.println(groupTemplate.getClassLoader().getResource("").getPath());
                    Template template = groupTemplate.getTemplate(repositoryFilePath);
                    template.binding("obj", entityModel);
                    template.binding("author", "geekyzk");
                    template.binding("packageName", packageName + ".repository");
                    System.out.println(template.render());
                });
        return null;
    }

    public static void main(String[] args) throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory("com.em248", "/home/geekyzk/tmp/testProject");
        EntityItemModel entityItemModel = EntityItemModel.builder()
                .name("name")
                .unique(true)
                .typeClass(String.class)
                .build();

        EntityModel entityModel = EntityModel.builder()
                .name("user")
                .entityItems(Collections.singletonList(entityItemModel))
                .build();
        generatorFactory.getEntityModelList().add(entityModel);
        generatorFactory.generatorCode();

    }
}
