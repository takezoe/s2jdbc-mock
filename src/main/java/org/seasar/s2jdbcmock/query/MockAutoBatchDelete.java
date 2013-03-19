package org.seasar.s2jdbcmock.query;

import java.util.List;

import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.AutoBatchDeleteImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockAutoBatchDelete<T> extends AutoBatchDeleteImpl<T> {

	public MockAutoBatchDelete(JdbcManagerImplementor jdbcManager, List<T> entities) {
		super(jdbcManager, entities);
	}

    /**
     * データベースのバッチ更新を実行します。
     *
     * @return 更新した行数の配列
     */
    protected int[] executeInternal() {
        final int[] updateRows = new int[entities.size()];
        for (int i = 0; i < updateRows.length; ++i) {
            final T entity = entities.get(i);
            prepareParams(entity);
            logSql();

            ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
            MockResultContext.addExecutedSql(executedSqlInfo);

            resetParams();

            updateRows[i] = 1;
        }
        return updateRows;
    }

}
