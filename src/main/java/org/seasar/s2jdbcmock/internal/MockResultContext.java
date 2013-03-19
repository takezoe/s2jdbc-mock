package org.seasar.s2jdbcmock.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Naoki Takezoe
 */
public class MockResultContext {

	private static Map<Class<?>, Long> idMap = new HashMap<Class<?>, Long>();
	private static List<Object> resultList = new ArrayList<Object>();
	private static List<ExecutedSqlInfo> executedSqlList = new ArrayList<ExecutedSqlInfo>();

	/**
	 * プライベートコンストラクタ。
	 */
	private MockResultContext(){
	}

	/**
	 * MockResultContextをリセットします。
	 * <p>
	 * 設定された期待値、実行結果がクリアされます。
	 * テストメソッドの実行前に必ず呼び出してください。
	 * </p>
	 */
	public static void reset(){
		resultList.clear();
		executedSqlList.clear();
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
		resultList.add(result);
	}

	public static <T> T getNextResult(Class<T> type){
		if(resultList.isEmpty()){
			return null;
		}
		return type.cast(resultList.remove(0));
	}

	public static long getNextId(Class<?> entity){
		Long value = idMap.get(entity.getClass());
		if(value == null){
			value = -1l;
		}
		value = value + 1;
		idMap.put(entity.getClass(), value);
		return value;
	}

	public static List<ExecutedSqlInfo> getExecutedSqlInfoList(){
		return executedSqlList;
	}

	public static void addExecutedSql(ExecutedSqlInfo executedSql){
//		System.out.println(String.format("[SQL] %s", normalizeSql(executedSql.getSql())));
//
//		Object[] params = executedSql.getParams();
//		for(int i=0; i < params.length; i++){
//			System.out.println(String.format("[SQL] params[%d]: %s", i, params[i]));
//		}

		executedSqlList.add(executedSql);
	}

}
