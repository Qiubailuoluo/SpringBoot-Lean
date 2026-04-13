package com.bookshop.config.startup;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动期数据库连接校验器。
 * 作用：应用启动时主动验证数据源可用性，避免运行期才暴露连库问题。
 */
@Component
public class DatabaseConnectionVerifier implements CommandLineRunner {

    private final DataSource dataSource;

    public DatabaseConnectionVerifier(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void run(String... args) throws Exception {
        // 启动时主动验证连接，确保当前配置可用。
        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(3)) {
                throw new IllegalStateException("数据库连接校验失败：connection.isValid=false");
            }
            System.out.println("数据库连接成功：" + connection.getMetaData().getURL());
        }
    }
}
