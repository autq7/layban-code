package com.layban.code;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.CustomFile;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;
import org.apache.ibatis.annotations.Mapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author wangxx
 * @version 1.0.0
 * @date 2024/11/5 15:02
 */
public class AutoCodeGenerator {

    /**
     * 数据源
     */
    private static final String DATASOURCE_URL = "jdbc:mysql://localhost:3306/layban_pro";

    /**
     * 用户名
     */
    private static final String USERNAME = "root";

    /**
     * 密码
     */
    private static final String PASSWORD = "root";

    private static final String MODULE_NAME = "";

    private static final String OUTPUT_PATH = System.getProperty("user.dir") + "/tmp";

    /**
     * 父包名
     */
    private static final String PARENT_PATH = "com.layban";

    /**
     * 作者
     */
    private static final String AUTHOR = "wangxx";

    /**
     * 表名
     */
    private static final List<String> TABLES = new ArrayList<>(
            Arrays.asList("system_dict_data"));

    public static void main(String[] args) {
        try {
            Files.createDirectories(Paths.get(OUTPUT_PATH + "/src/main/java"));
            Files.createDirectories(Paths.get(OUTPUT_PATH + "/src/main/resources/mapper"));
        } catch (Exception e) {
            return; // Exit if the directory creation fails
        }
        FastAutoGenerator.create(DATASOURCE_URL, USERNAME, PASSWORD)
                .globalConfig(builder -> builder
                        .author(AUTHOR)
                        .outputDir(OUTPUT_PATH + "/src/main/java")
                )
                .packageConfig(builder -> builder
                        .parent(PARENT_PATH)
                        .moduleName(MODULE_NAME)
                        .entity("model.dataobject")
                        .service("service")
                        .serviceImpl("service.impl")
                        .mapper("mapper")
                        .xml("mapper.xml")
                        .controller("controller")
                        .pathInfo(Collections.singletonMap(OutputFile.xml, OUTPUT_PATH + "/src/main/resources/mapper"))
                )
                .strategyConfig(builder -> builder
                        .addInclude(TABLES)
                        .entityBuilder()
                        .superClass("com.layban.common.model.BaseModel")
                        .addSuperEntityColumns("creator", "create_time", "update_time", "updater")
                        .versionColumnName("version")
                        .logicDeleteColumnName("deleted")
                        .formatFileName("%sDO")
                        .enableLombok()
                        .controllerBuilder()
                        .superClass("com.layban.common.controller.BaseController")
                        .enableRestStyle()
                        .serviceBuilder()
                        .superServiceClass("com.layban.common.service.BaseService")
                        .superServiceImplClass("com.layban.common.service.BaseServiceImpl")
                        .formatServiceFileName("%sService")
                        .formatServiceImplFileName("%sServiceImpl")
                        .mapperBuilder()
                        .mapperAnnotation(Mapper.class)
                        .enableBaseResultMap()
                        .enableBaseColumnList()
                )
                .injectionConfig(consumer -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("dto", "model.dto");
                    map.put("add", "model.vo.add");
                    map.put("update", "model.vo.update");
                    map.put("vo", "model.vo");
                    map.put("qry", "model.vo.qry");
                    map.put("convert", "model.convert");
                    consumer.customMap(map);
                    List<CustomFile> customFiles = new ArrayList<>();
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.dto")
                            .fileName("DTO.java")
                            .templatePath("/templates/dto/DTO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.vo.add")
                            .fileName("AddVO.java")
                            .templatePath("/templates/vo/AddVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.vo.update")
                            .fileName("UpdateVO.java")
                            .templatePath("/templates/vo/UpdateVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.vo")
                            .fileName("VO.java")
                            .templatePath("/templates/vo/VO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.vo.qry")
                            .fileName("PageQryVO.java")
                            .templatePath("/templates/vo/PageQryVO.java.vm")
                            .enableFileOverride()
                            .build());
                    customFiles.add(new CustomFile.Builder()
                            .formatNameFunction(tableInfo ->
                                    tableInfo.getEntityName().replaceAll("DO", ""))
                            .packageName("model.convert")
                            .fileName("Convert.java")
                            .templatePath("/templates/convert/Convert.java.vm")
                            .enableFileOverride()
                            .build());
                    consumer.customFile(customFiles);
                }).templateEngine(new VelocityTemplateEngine()).execute();
    }
}
