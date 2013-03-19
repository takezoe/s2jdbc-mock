package org.seasar.s2jdbcmock.query;

import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.extension.jdbc.query.AutoProcedureCallImpl;
import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class MockAutoProcedureCall extends AutoProcedureCallImpl {

	public MockAutoProcedureCall(JdbcManagerImplementor jdbcManager, String procedureName) {
		super(jdbcManager, procedureName);
	}

	public MockAutoProcedureCall(JdbcManagerImplementor jdbcManager, String procedureName, Object param) {
		super(jdbcManager, procedureName, param);
	}

	@Override
    public void execute() {
        prepare("execute");
        logSql();

        ExecutedSqlInfo executedSqlInfo = new ExecutedSqlInfo(executedSql, getParamValues());
        MockResultContext.addExecutedSql(executedSqlInfo);

        completed();
    }

}
