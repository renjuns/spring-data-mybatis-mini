/*
 * Copyright 2017-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.data.mybatis.mini.jdbc.repository.support;

import com.vonchange.jdbc.abstractjdbc.core.JdbcRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;

import org.springframework.data.mybatis.mini.relational.core.conversion.RelationalConverter;
import org.springframework.data.mybatis.mini.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.core.support.TransactionalRepositoryFactoryBeanSupport;
import org.springframework.util.Assert;

import java.io.Serializable;

//import org.springframework.data.jdbc.core.DefaultDataAccessStrategy;

/**
 * Special adapter for Springs {@link org.springframework.beans.factory.FactoryBean} interface to allow easy setup of
 * repository factories via Spring configuration.
 *
 * @author Jens Schauder
 * @author Greg Turnquist
 * @author Christoph Strobl
 * @author Oliver Gierke
 * @author Mark Paluch
 */
public class JdbcRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> //
		extends TransactionalRepositoryFactoryBeanSupport<T, S, ID> implements ApplicationEventPublisherAware {

	private RelationalMappingContext mappingContext;
	private RelationalConverter converter;
	private JdbcRepository operations;

	/**
	 * Creates a new {@link JdbcRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	JdbcRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
		super(repositoryInterface);
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#setApplicationEventPublisher(org.springframework.context.ApplicationEventPublisher)
	 */
	@Override
	public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {

		super.setApplicationEventPublisher(publisher);
	}

	/**
	 * Creates the actual {@link RepositoryFactorySupport} instance.
	 */
	@Override
	protected RepositoryFactorySupport doCreateRepositoryFactory() {

		JdbcRepositoryFactory jdbcRepositoryFactory = new JdbcRepositoryFactory( mappingContext,
				operations);
		return jdbcRepositoryFactory;
	}

	@Autowired
	protected void setMappingContext(RelationalMappingContext mappingContext) {

		super.setMappingContext(mappingContext);
		this.mappingContext = mappingContext;
	}



	@Autowired
	public void setJdbcOperations(JdbcRepository operations) {
		this.operations = operations;
	}

	@Autowired
	public void setConverter(RelationalConverter converter) {
		this.converter = converter;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() {

		Assert.state(this.mappingContext != null, "MappingContext is required and must not be null!");
		Assert.state(this.converter != null, "RelationalConverter is required and must not be null!");


		super.afterPropertiesSet();
	}
}