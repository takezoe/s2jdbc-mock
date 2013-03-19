package org.seasar.s2jdbcmock.query;

import java.util.List;

import org.seasar.extension.jdbc.exception.SNonUniqueResultException;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.AutoSelectImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockAutoSelect<T> extends AutoSelectImpl<T> {

	public MockAutoSelect(JdbcManagerImplementor jdbcManager, Class<T> baseClass) {
		super(jdbcManager, baseClass);
	}

    @Override
	public long getCount() {
        count = true;
        prepare("getCount");
        logSql();

        ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
        MockResultContext.addExecutedSql(executedSqlInfo);

        try {
        	Long result = MockResultContext.getNextResult(Long.class);
        	if(result == null){
        		result = new Long(0);
        	}
        	return result;
        } finally {
            completed();
        }
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
