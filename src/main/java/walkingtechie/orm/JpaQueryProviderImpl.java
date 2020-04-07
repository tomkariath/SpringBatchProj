package walkingtechie.orm;

import javax.persistence.Query;

import org.springframework.batch.item.database.orm.AbstractJpaQueryProvider;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

public class JpaQueryProviderImpl<E> extends AbstractJpaQueryProvider{
	private Class<E> entityClass;
	
	private String query;

	@Override
	public Query createQuery() {
		return getEntityManager().createNamedQuery(query,entityClass);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.isTrue(StringUtils.hasText(getQuery()), "Query cannot be empty");
		Assert.notNull(getEntityClass(), "Entity Class cannot be Null");
	}

	public String getQuery() {
		return query;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}

	public void setQuery(String query) {
		this.query = query;
	}
}
