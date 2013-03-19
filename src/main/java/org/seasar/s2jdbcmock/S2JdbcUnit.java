package org.seasar.s2jdbcmock;

import static junit.framework.Assert.*;
import junit.framework.Assert;

import org.seasar.s2jdbcmock.internal.ExecutedSqlInfo;
import org.seasar.s2jdbcmock.internal.MockResultContext;

public class S2JdbcUnit {

	/**
	 * プライベートコンストラクタ。
	 */
	private S2JdbcUnit(){
	}

	/**
	 * S2JDBCUnitを初期化します。
	 * テストメソッドの実行前に必ず呼び出してください。
	 */
	public static void initS2JdbcUnit(){
		MockResultContext.reset();
	}

	/**
	 * MockJdbcManagerが返却する結果を追加します。
	 * <p>
	 * このメソッドで結果を設定したあとでMockJdbcManagerを呼び出すと、
	 * ここで設定された値が順番に返却されます。
	 * </p>
	 *
	 * @param result MockJdbcManagerが返却する値
	 */
	public static void addResult(Object result){
		MockResultContext.addResult(result);
	}

	/**
	 * SQLが発行された回数を検証します。
	 *
	 * @param expected 期待値
	 */
	public static void verifySqlNumber(int expected){
		assertEquals(expected, MockResultContext.getExecutedSqlInfoList().size());
	}

	/**
	 * 実行されたSQLを検証します。
	 *
	 * @param indexOfSql 実行順序
	 * @param sql 実行されたSQL
	 */
	public static void verifySql(int indexOfSql, String sql){
		ExecutedSqlInfo executedSql = MockResultContext.getExecutedSqlInfoList().get(indexOfSql);
		String result = executedSql.getSql();
		Assert.assertEquals(normalizeSql(sql), normalizeSql(result));
	}

	/**
	 * 実行されたSQLとパラメータを検証します。
	 *
	 * @param indexOfSql 実行順序
	 * @param sql 実行されたSQL
	 * @param values パラメータの配列
	 */
	public static void verifySql(int indexOfSql, String sql, Object... values){
		verifySql(indexOfSql, sql);
		verifyParameters(indexOfSql, values);
	}

	/**
	 * 実行されたSQLを正規表現で検証します。
	 *
	 * @param indexOfSql 実行順序
	 * @param pattern 正規表現
	 */
	public static void verifySqlByRegExp(int indexOfSql, String regexp){
		ExecutedSqlInfo executedSql = MockResultContext.getExecutedSqlInfoList().get(indexOfSql);
		String result = normalizeSql(executedSql.getSql());
		Assert.assertTrue(result.matches(regexp));
	}

	/**
	 * Verifies the executed SQL and parameters using the regular expression.
	 *
	 * @param indexOfSql the index of executed SQL
	 * @param pattern the pattern of expected SQL
	 * @param values PreparedStatement parameters
	 */
	public static void verifySqlByRegExp(int indexOfSql, String regexp, Object... values){
		verifySqlByRegExp(indexOfSql, regexp);
		verifyParameters(indexOfSql, values);
	}

//	/**
//	 * Verifies the SQL and parameters which is executed by {@link SqlManager#findEntity(Class, Object...)}.
//	 *
//	 * @param indexOfSql the index of executed SQL
//	 * @param id the values of primary key
//	 */
//	public static void verifyFindSql(int indexOfSql, Class<?> entityClass, Object... id){
//		verifySql(indexOfSql, MirageUtil.buildSelectSQL(entityClass, nameConverter));
//		verifyParameters(indexOfSql, id);
//	}
//
//	/**
//	 * Verifies the SQL and parameters which is executed by {@link SqlManager#insertEntity(Object)}.
//	 *
//	 * @param indexOfSql the index of executed SQL
//	 * @param entity the entity which should be inserted
//	 */
//	public static void verifyInsertSql(int indexOfSql, Object entity){
//		List<Object> values = new ArrayList<Object>();
//		verifySql(indexOfSql, MirageUtil.buildInsertSql(entity, nameConverter, values));
//		verifyParameters(indexOfSql, values.toArray());
//	}
//
//	/**
//	 * Verifies the SQL and parameters which is executed by {@link SqlManager#updateEntity(Object)}.
//	 *
//	 * @param indexOfSql the index of executed SQL
//	 * @param entity the entity which should be updated
//	 */
//	public static void verifyUpdatetSql(int indexOfSql, Object entity){
//		List<Object> values = new ArrayList<Object>();
//		verifySql(indexOfSql, MirageUtil.buildUpdateSql(entity, nameConverter, values));
//		verifyParameters(indexOfSql, values.toArray());
//	}
//
//	/**
//	 * Verifies the SQL and parameters which is executed by {@link SqlManager#deleteEntity(Object)}.
//	 *
//	 * @param indexOfSql the index of executed SQL
//	 * @param entity the entity which should be deleted
//	 */
//	public static void verifyDeleteSql(int indexOfSql, Object entity){
//		List<Object> values = new ArrayList<Object>();
//		verifySql(indexOfSql, MirageUtil.buildDeleteSql(entity, nameConverter, values));
//		verifyParameters(indexOfSql, values.toArray());
//	}

	private static void verifyParameters(int indexOfSql, Object... values){
		ExecutedSqlInfo executedSql = MockResultContext.getExecutedSqlInfoList().get(indexOfSql);
		Object[] params = executedSql.getParams();

		assertEquals(values.length, params.length);

		for(int i=0; i < values.length; i++){
			assertEquals(values[i], params[i]);
		}
	}

	private static String normalizeSql(String sql){
		sql = sql.replaceAll("--.*", " ");
		sql = sql.replaceAll("\r\n", "\n");
		sql = sql.replaceAll("\r", "\n");
		sql = sql.replaceAll("\n", " ");
		sql = sql.replaceAll("/\\*.*\\*/", " ");
		sql = sql.replaceAll("[ \t]+", " ");
		sql = sql.toUpperCase().trim();

		return sql;
	}
}
