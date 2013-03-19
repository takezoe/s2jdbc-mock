package org.seasar.s2jdbcmock.meta;

import javax.persistence.GeneratedValue;

import org.seasar.extension.jdbc.ColumnMetaFactory;
import org.seasar.extension.jdbc.EntityMeta;
import org.seasar.extension.jdbc.PropertyMeta;
import org.seasar.extension.jdbc.meta.PropertyMetaFactoryImpl;
import org.seasar.framework.container.SingletonS2Container;
import org.seasar.framework.convention.PersistenceConvention;
import org.seasar.s2jdbcmock.id.MockIdentityIdGenerator;
import org.seasar.s2jdbcmock.id.MockPreAllocateIdGenerator;

public class MockPropertyMetaFactory extends PropertyMetaFactoryImpl {

	public MockPropertyMetaFactory(){
		setPersistenceConvention(SingletonS2Container.getComponent(PersistenceConvention.class));
		setColumnMetaFactory(SingletonS2Container.getComponent(ColumnMetaFactory.class));
	}

	@Override
	protected void doIdentityIdGenerator(PropertyMeta propertyMeta,
			EntityMeta entityMeta) {
        propertyMeta.setIdentityIdGenerator(new MockIdentityIdGenerator(entityMeta, propertyMeta));
	}

	@Override
	protected boolean doSequenceIdGenerator(PropertyMeta propertyMeta,
			GeneratedValue generatedValue, EntityMeta entityMeta) {
        propertyMeta.setSequenceIdGenerator(new MockPreAllocateIdGenerator(entityMeta,
                propertyMeta, 1));
        return true;
	}

	@Override
	protected boolean doTableIdGenerator(PropertyMeta propertyMeta,
			GeneratedValue generatedValue, EntityMeta entityMeta) {
        propertyMeta.setTableIdGenerator(new MockPreAllocateIdGenerator(entityMeta,
                propertyMeta, 1));
        return true;
	}

}
