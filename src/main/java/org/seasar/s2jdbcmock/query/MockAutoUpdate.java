package org.seasar.s2jdbcmock.query;

import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.AutoUpdateImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockAutoUpdate<T> extends AutoUpdateImpl<T> {

	public MockAutoUpdate(JdbcManagerImplementor jdbcManager, T entity) {
		super(jdbcManager, entity);
	}

	@Override
    protected int executeInternal() {
        try {
            logSql();

            ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
            MockResultContext.addExecutedSql(executedSqlInfo);

            Integer rows = MockResultContext.getNextResult(Integer.class);
            if(rows == null){
            	rows = 1;
            }
            postExecute(null);
            if (isOptimisticLock()) {
                validateRows(rows);
            }
            if (entityMeta.hasVersionPropertyMeta()) {
                incrementVersion();
            }
            return rows;
        } finally {
        }
    }
}
