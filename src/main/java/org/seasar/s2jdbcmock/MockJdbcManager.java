package org.seasar.s2jdbcmock;

import java.util.Arrays;
import java.util.List;

import org.seasar.extension.jdbc.AutoBatchDelete;
import org.seasar.extension.jdbc.AutoBatchInsert;
import org.seasar.extension.jdbc.AutoBatchUpdate;
import org.seasar.extension.jdbc.AutoDelete;
import org.seasar.extension.jdbc.AutoFunctionCall;
import org.seasar.extension.jdbc.AutoInsert;
import org.seasar.extension.jdbc.AutoProcedureCall;
import org.seasar.extension.jdbc.AutoSelect;
import org.seasar.extension.jdbc.AutoUpdate;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.EntityMetaFactory;
import org.seasar.extension.jdbc.SqlFileSelect;
import org.seasar.extension.jdbc.SqlSelect;
import org.seasar.extension.jdbc.exception.NoIdPropertyRuntimeException;
import org.seasar.extension.jdbc.manager.JdbcManagerImpl;
import org.seasar.framework.exception.EmptyRuntimeException;
import org.seasar.s2jdbcmock.meta.MockEntityMetaFactory;
import org.seasar.s2jdbcmock.query.MockAutoBatchDelete;
import org.seasar.s2jdbcmock.query.MockAutoBatchInsert;
import org.seasar.s2jdbcmock.query.MockAutoBatchUpdate;
import org.seasar.s2jdbcmock.query.MockAutoDelete;
import org.seasar.s2jdbcmock.query.MockAutoFunctionCall;
import org.seasar.s2jdbcmock.query.MockAutoInsert;
import org.seasar.s2jdbcmock.query.MockAutoProcedureCall;
import org.seasar.s2jdbcmock.query.MockAutoSelect;
import org.seasar.s2jdbcmock.query.MockAutoUpdate;
import org.seasar.s2jdbcmock.query.MockSqlFileSelect;
import org.seasar.s2jdbcmock.query.MockSqlSelect;

public class MockJdbcManager extends JdbcManagerImpl {

	@Override
	public EntityMetaFactory getEntityMetaFactory() {
		return new MockEntityMetaFactory();
	}

    @Override
	public <T> AutoSelect<T> from(Class<T> baseClass) {
		return new MockAutoSelect<T>(this, baseClass).maxRows(maxRows)
				.fetchSize(fetchSize)
				.queryTimeout(queryTimeout);
	}

    @Override
	public <T> SqlSelect<T> selectBySql(Class<T> baseClass, String sql,
			Object... params) {
		return new MockSqlSelect<T>(this, baseClass, sql, params)
				.maxRows(maxRows)
				.fetchSize(fetchSize)
				.queryTimeout(queryTimeout);
	}

    @Override
	public <T> AutoInsert<T> insert(final T entity) {
		return new MockAutoInsert<T>(this, entity).queryTimeout(queryTimeout);
	}

	@Override
	public <T> AutoUpdate<T> update(T entity) {
		final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entity.getClass());
		if (entityMeta.getIdPropertyMetaList().isEmpty()) {
			throw new NoIdPropertyRuntimeException("ESSR0761", entityMeta.getName());
		}
		return new MockAutoUpdate<T>(this, entity).queryTimeout(queryTimeout);
	}

	@Override
	public <T> AutoDelete<T> delete(final T entity) {
		final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entity.getClass());
		if (entityMeta.getIdPropertyMetaList().isEmpty()) {
			throw new NoIdPropertyRuntimeException("ESSR0762", entityMeta.getName());
		}
		return new MockAutoDelete<T>(this, entity).queryTimeout(queryTimeout);
	}

	@Override
	public <T> SqlFileSelect<T> selectBySqlFile(Class<T> baseClass, String path, Object parameter) {
        return new MockSqlFileSelect<T>(this, baseClass, path, parameter)
                .maxRows(maxRows)
                .fetchSize(fetchSize)
                .queryTimeout(queryTimeout);
	}

    @Override
    public <T> AutoBatchInsert<T> insertBatch(final T... entities) {
        return new MockAutoBatchInsert<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    @Override
    public <T> AutoBatchInsert<T> insertBatch(final List<T> entities) {
        return new MockAutoBatchInsert<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    @Override
    public <T> AutoBatchUpdate<T> updateBatch(final T... entities) {
        if (entities == null) {
            throw new NullPointerException("entities");
        }
        if (entities.length == 0) {
            throw new EmptyRuntimeException("entities");
        }
        final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entities[0].getClass());
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            throw new NoIdPropertyRuntimeException("ESSR0761", entityMeta.getName());
        }
        return new MockAutoBatchUpdate<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    @Override
    public <T> AutoBatchUpdate<T> updateBatch(final List<T> entities) {
        if (entities == null) {
            throw new NullPointerException("entities");
        }
        if (entities.isEmpty()) {
            throw new EmptyRuntimeException("entities");
        }
        final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entities.get(0).getClass());
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            throw new NoIdPropertyRuntimeException("ESSR0761", entityMeta.getName());
        }
        return new MockAutoBatchUpdate<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    @Override
    public <T> AutoBatchDelete<T> deleteBatch(final T... entities) {
        if (entities == null) {
            throw new NullPointerException("entities");
        }
        if (entities.length == 0) {
            throw new EmptyRuntimeException("entities");
        }
        final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entities[0].getClass());
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            throw new NoIdPropertyRuntimeException("ESSR0762", entityMeta.getName());
        }
        return new MockAutoBatchDelete<T>(this, Arrays.asList(entities))
                .queryTimeout(queryTimeout);
    }

    @Override
    public <T> AutoBatchDelete<T> deleteBatch(final List<T> entities) {
        if (entities == null) {
            throw new NullPointerException("entities");
        }
        if (entities.isEmpty()) {
            throw new EmptyRuntimeException("entities");
        }
        final EntityMeta entityMeta = entityMetaFactory.getEntityMeta(entities.get(0).getClass());
        if (entityMeta.getIdPropertyMetaList().isEmpty()) {
            throw new NoIdPropertyRuntimeException("ESSR0762", entityMeta.getName());
        }
        return new MockAutoBatchDelete<T>(this, entities)
                .queryTimeout(queryTimeout);
    }

    @Override
    public AutoProcedureCall call(String procedureName, Object parameter) {
        return new MockAutoProcedureCall(this, procedureName, parameter)
                .maxRows(maxRows)
                .fetchSize(fetchSize)
                .queryTimeout(queryTimeout);
    }

	@Override
	public <T> AutoFunctionCall<T> call(Class<T> resultClass,
			String functionName, Object parameter) {
        return new MockAutoFunctionCall<T>(this, resultClass, functionName, parameter)
        	.maxRows(maxRows)
        	.fetchSize(fetchSize)
        	.queryTimeout(queryTimeout);
	}



}
