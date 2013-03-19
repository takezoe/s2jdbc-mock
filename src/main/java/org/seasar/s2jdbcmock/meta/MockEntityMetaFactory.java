package org.seasar.s2jdbcmock.meta;

import org.seasar.extension.jdbc.TableMetaFactory;
import org.seasar.extension.jdbc.meta.EntityMetaFactoryImpl;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.convention.PersistenceConvention;

public class MockEntityMetaFactory extends EntityMetaFactoryImpl {

	public MockEntityMetaFactory(){
		setPropertyMetaFactory(new MockPropertyMetaFactory());
		setTableMetaFactory(SingletonS2Container.getComponent(TableMetaFactory.class));
		setPersistenceConvention(SingletonS2Container.getComponent(PersistenceConvention.class));
	}

}
