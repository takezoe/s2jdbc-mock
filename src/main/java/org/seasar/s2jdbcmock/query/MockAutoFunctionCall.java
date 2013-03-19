package org.seasar.s2jdbcmock.query;

import java.util.List;

import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.AutoFunctionCallImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockAutoFunctionCall<T> extends AutoFunctionCallImpl<T> {

	public MockAutoFunctionCall(JdbcManagerImplementor jdbcManager,
			Class<T> resultClass, String functionName) {
		super(jdbcManager, resultClass, functionName);
	}

	public MockAutoFunctionCall(JdbcManagerImplementor jdbcManager,
			Class<T> resultClass, String functionName, Object param) {
		super(jdbcManager, resultClass, functionName, param);
	}

	@Override
	public T getSingleResult() {
        prepare("getSingleResult");
        logSql();

        ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
        MockResultContext.addExecutedSql(executedSqlInfo);

        try {
        	return MockResultContext.getNextResult(resultClass);
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




}
