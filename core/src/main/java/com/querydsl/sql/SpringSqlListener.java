package com.querydsl.sql;

import com.querydsl.core.QueryException;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SpringSqlListener extends SQLBaseListener {
    private DataSource dataSource;

    public SpringSqlListener(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void end(SQLListenerContext context) {
        Connection connection = context.getConnection();
        if (connection != null
                && context.getData(AbstractSQLQuery.PARENT_CONTEXT) == null
                && !DataSourceUtils.isConnectionTransactional(connection, this.dataSource)) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new QueryException(e);
            }
        }
    }

}
