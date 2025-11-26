package com.wzh.intelligentcodeapp.generator;

import cn.hutool.core.lang.Dict;
import cn.hutool.setting.yaml.YamlUtil;
import com.mybatisflex.codegen.Generator;
import com.mybatisflex.codegen.config.ColumnConfig;
import com.mybatisflex.codegen.config.GlobalConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.util.Map;

public class IntelligentGenerator {

    private static final String[] TABLES_NAME = {
            "user"
    };

    public static void main(String[] args) {
        //获取数据源配置
        Dict dict = YamlUtil.loadByPath("application.yml");
        Map<String, Object> dataSourceData = dict.getByPath("spring.datasource");

        //配置数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(dataSourceData.get("url").toString());
        dataSource.setUsername(dataSourceData.get("username").toString());
        dataSource.setPassword(dataSourceData.get("password").toString());
        //创建配置内容，两种风格都可以。
        GlobalConfig globalConfig = createGlobalConfigUseStyle();

        //通过 datasource 和 globalConfig 创建代码生成器
        Generator generator = new Generator(dataSource, globalConfig);

        //生成代码
        generator.generate();
    }

    public static GlobalConfig createGlobalConfigUseStyle() {
        //创建配置内容
        GlobalConfig globalConfig = new GlobalConfig();

        //设置根包
        globalConfig.getPackageConfig()
                .setBasePackage("com.wzh.intelligentcodeapp.genresult");

        //设置表前缀和只生成哪些表，setGenerateTable 未配置时，生成所有表
        globalConfig.getStrategyConfig()
                .setGenerateTable(TABLES_NAME)
                .setLogicDeleteColumn("is_delete");

        //设置生成 entity 并启用 Lombok
        globalConfig.enableEntity()
                .setWithLombok(true)
                .setJdkVersion(21);

        //设置生成 mapper
        globalConfig.enableMapper();
        globalConfig.enableMapperXml();

        //设置生成 service
        globalConfig.enableService();
        globalConfig.enableServiceImpl();
        //设置生成 controller
        globalConfig.enableController();

        //设置生成作者
        globalConfig.setAuthor("wzh");

        return globalConfig;
    }
}