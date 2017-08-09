package cas.lib.arclib.store;

import cas.lib.arclib.domain.DatedObject;
import cas.lib.arclib.domain.IndexedDatedObject;
import cas.lib.arclib.rest.DataAdapter;
import com.querydsl.core.types.dsl.EntityPathBase;
import cas.lib.arclib.rest.Params;
import cas.lib.arclib.rest.Result;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.inject.Inject;
import java.util.Collection;

@Getter
public class IndexedDatedStore<T extends DatedObject, Q extends EntityPathBase<T>, U extends IndexedDatedObject>
        extends DatedStore<T, Q> implements IndexedStore<T, U>, DataAdapter<T> {
    protected ElasticsearchTemplate template;

    private Class<U> uType;

    public IndexedDatedStore(Class<T> type, Class<Q> qType, Class<U> uType) {
        super(type, qType);
        this.uType = uType;
    }

    /**
     * Converts a JPA instance to an Elasticsearch instance.
     *
     * <p>
     *     Subclasses should call super to reuse the provided mapping for {@link DatedObject}
     * </p>
     * @param obj JPA instance
     * @return Elasticsearch instance
     */
    @SneakyThrows
    public U toIndexObject(T obj) {
        U u = getUType().newInstance();

        u.setId(obj.getId());
        u.setCreated(obj.getCreated());
        u.setUpdated(obj.getUpdated());

        return u;
    }

    @Override
    public T save(T entity) {
        entity = super.save(entity);
        return IndexedStore.super.save(entity);
    }

    @Override
    public void save(Collection<? extends T> entities) {
        super.save(entities);
        IndexedStore.super.save(entities);
    }

    @Override
    public void delete(T entity) {
        super.delete(entity);
        IndexedStore.super.delete(entity);
    }

    @Override
    public Result<T> findAll(Params params) {
        return IndexedStore.super.findAll(params);
    }

    @Inject
    public void setTemplate(ElasticsearchTemplate template) {
        this.template = template;
    }
}
