package org.seasar.s2jdbcmock.internal;

/**
 * JdbcManagerを使用して実行されたSQLの情報を格納するDTOです。
 *
 *
 * @author Naoki Takezoe
 */
public class ExecutedSqlInfo {

	private String sql;
	private Object[] params;

	/**
	 * コンストラクタ。
	 *
	 * @param sql 実行されたSQL
	 * @param params パラメータ
	 */
	public ExecutedSqlInfo(String sql, Object[] params){
		this.sql = sql;
		this.params = params;
	}

	/**
	 * 実行されたSQLを取得します。
	 *
	 * @return 実行されたSQL
	 */
	public String getSql(){
		return this.sql;
	}

	/**
	 * PreparedStatementのパラメータを取得します。
	 *
	 * @return PreparedStatementのパラメータ
	 */
	public Object[] getParams(){
		if(params == null){
			params = new Object[0];
		}
		return this.params;
	}

}
