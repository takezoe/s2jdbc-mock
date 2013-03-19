package org.seasar.s2jdbcmock.id;

import java.sql.Statement;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.id.IdentityIdGenerator;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.s2jdbcmock.internal.MockResultContext;

/**
 * IdentityIdGenerator のモックです。
 * INSERT文の実行後に {@link MockResultContext#getNextId(Class)} で取得したIDをエンティティに設定します。
 *
 * @author Naoki Takezoe
 */
public class MockIdentityIdGenerator extends IdentityIdGenerator{

	public MockIdentityIdGenerator(EntityMeta entityMeta, PropertyMeta propertyMeta) {
		super(entityMeta, propertyMeta);
	}

	public void postInsert(JdbcManagerImplementor jdbcManager, Object entity,
			Statement statement, SqlLogger sqlLogger) {
        final long id = MockResultContext.getNextId(entity.getClass());
        setId(entity, id);
	}

}
