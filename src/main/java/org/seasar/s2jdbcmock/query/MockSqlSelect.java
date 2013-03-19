package org.seasar.s2jdbcmock.query;

import java.util.List;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.SqlSelectImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockSqlSelect<T> extends SqlSelectImpl<T> {

	public MockSqlSelect(JdbcManagerImplementor jdbcManager,
			Class<T> baseClass, String sql, Object[] params) {
		super(jdbcManager, baseClass, sql, params);
	}

    @Override
    public List<T> getResultList() {
        prepare("getResultList");
        logSql();

        ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
        MockResultContext.addExecutedSql(executedSqlInfo);

        try {
        	@SuppressWarnings("unchecked")
        	List<T> result = MockResultContext.getNextResult(List.class);
        	return result;
        } finally {
            completed();
        }
    }

    @Override
    public T getSingleResult() throws SNonUniqueResultException {
        prepare("getSingleResult");
        logSql();

        ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
        MockResultContext.addExecutedSql(executedSqlInfo);

        try {
        	return MockResultContext.getNextResult(baseClass);
        } finally {
            completed();
        }
    }

}
