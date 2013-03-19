package org.seasar.s2jdbcmock.id;

import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.SqlLogger;
import org.seasar.extension.jdbc.id.AbstractPreAllocateIdGenerator;
import org.seasar.extension.jdbc.manager.JdbcManagerImplementor;
import org.seasar.s2jdbcmock.internal.MockResultContext;

/**
 * AbstractPreAllocateIdGenerator を継承した IdGenerator のモックです。
 * TableIdGenerator と SequenceIdGenerator の代わりに使用されます。
 * INSERT文の実行前に {@link MockResultContext#getNextId(Class)} で取得したIDをエンティティに設定します。
 *
 * @author Naoki Takezoe
 */
public class MockPreAllocateIdGenerator extends AbstractPreAllocateIdGenerator {

	public MockPreAllocateIdGenerator(EntityMeta entityMeta, PropertyMeta propertyMeta, long allocationSize) {
		super(entityMeta, propertyMeta, allocationSize);
	}

	@Override
	protected long getNewInitialValue(JdbcManagerImplementor jdbcManager,
			SqlLogger sqlLogger) {
		return MockResultContext.getNextId(entityMeta.getEntityClass());
	}

}
