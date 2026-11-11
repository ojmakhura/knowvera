package bw.co.knowvera;

import java.util.List;
import java.util.StringJoiner;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.support.PageableExecutionUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Selection;

/**
 * Builds list DTOs from just the columns they show, instead of loading entities and mapping them.
 * <p>
 * Loading entities for a list pulls in every eager association row by row (members bring statuses, professions,
 * documents, durations, CPD; individuals bring their member); a projection reads a page in one query, plus a
 * count when the page alone does not show the total. The existing search {@link Specification}s are reused
 * unchanged, so filtering and sorting stay exactly as with the entity queries.
 *
 * @param <E> entity queried
 * @param <D> list DTO produced
 */
public abstract class ListProjection<E, D> {

    protected final EntityManager entityManager;
    private final Class<E> entityType;

    protected ListProjection(EntityManager entityManager, Class<E> entityType) {
        this.entityManager = entityManager;
        this.entityType = entityType;
    }

    /** The columns to read; every selection needs an alias, which {@link #toDto(Tuple)} reads back. */
    protected abstract List<Selection<?>> columns(Root<E> root, CriteriaBuilder cb);

    protected abstract D toDto(Tuple row);

    /** Fills in data that needs its own query (e.g. collections), once for the whole page. */
    protected List<D> complete(List<D> rows) {
        return rows;
    }

    public List<D> list(Specification<E> spec, Sort sort) {
        return complete(query(spec, sort).getResultList().stream().map(this::toDto).toList());
    }

    public Page<D> page(Specification<E> spec, Pageable pageable) {

        TypedQuery<Tuple> query = query(spec, pageable.getSort());
        if (pageable.isPaged()) {
            query.setFirstResult((int) pageable.getOffset());
            query.setMaxResults(pageable.getPageSize());
        }
        List<D> content = complete(query.getResultList().stream().map(this::toDto).toList());

        // Skips the count when the page itself shows the total (first page not full, or last page)
        return PageableExecutionUtils.getPage(content, pageable, () -> count(spec));
    }

    private TypedQuery<Tuple> query(Specification<E> spec, Sort sort) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> query = cb.createTupleQuery();
        Root<E> root = query.from(entityType);
        query.multiselect(columns(root, cb));

        Predicate predicate = spec == null ? null : spec.toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }
        if (sort != null && sort.isSorted()) {
            query.orderBy(QueryUtils.toOrders(sort, root, cb));
        }
        return entityManager.createQuery(query);
    }

    private long count(Specification<E> spec) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<E> root = query.from(entityType);

        Predicate predicate = spec == null ? null : spec.toPredicate(root, query, cb);
        if (predicate != null) {
            query.where(predicate);
        }
        // A specification may ask for distinct results
        query.select(query.isDistinct() ? cb.countDistinct(root) : cb.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }

    /** The non-blank name parts, space separated (as the mappers' name helpers do). */
    public static String fullName(String firstName, String middleName, String surname) {
        StringJoiner name = new StringJoiner(" ");
        for (String part : new String[] { firstName, middleName, surname }) {
            if (StringUtils.isNotBlank(part)) {
                name.add(part);
            }
        }
        return name.toString();
    }

    protected static String idOf(Tuple row, String alias) {
        Object id = row.get(alias);
        return id == null ? null : id.toString();
    }
}
