package org.rainy.commerce.service;

import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.util.Collections;

/**
 * <p>
 * 生成数据库文档
 * </p>
 *
 * @author zhangyu
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DataBaseDocTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void generateDataBaseDoc() {
        String filePath = "/Users/rainy/Developer/code/java/commerce";
        EngineConfig engineConfig = EngineConfig.builder()
                // 文档存放路径
                .fileOutputDir(filePath)
                // 文档类型
                .fileType(EngineFileType.HTML)
                // 是否自动打开
                .openOutputDir(false)
                .produceType(EngineTemplateType.freemarker)
                .build();

        ProcessConfig processConfig = ProcessConfig.builder()
                // 生成指定名称的数据表
                .designatedTableName(Collections.emptyList())
                // 生成指定前缀的数据表
                .designatedTablePrefix(Collections.emptyList())
                // 生成指定后缀的数据表
                .designatedTableSuffix(Collections.emptyList())
                // 要忽略的表
                .ignoreTableName(Collections.emptyList())
                // 按照前缀忽略表
                .ignoreTablePrefix(Collections.emptyList())
                // 按照后缀忽略表
                .ignoreTableSuffix(Collections.emptyList())
                .build();

        // 数据库名称_description_version.html
        Configuration configuration = Configuration.builder()
                .dataSource(applicationContext.getBean(DataSource.class))
                .description("dev")
                .version("1.0")
                .engineConfig(engineConfig)
                .produceConfig(processConfig)
                .build();

        new DocumentationExecute(configuration).execute();
        log.info("database document create success, see: {}", filePath);

    }

}
