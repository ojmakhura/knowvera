package bw.co.knowvera;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jspecify.annotations.Nullable;

/**
 * TODO: Model Documentation for SearchObject<T>
 * Class template parameters<parameter1, parameter2, etc>
 * @param T T $param.parameter.getterSetterTypeName
 */
public class SearchObject<T>
    implements Serializable, Comparable<SearchObject>
{
    /** The serial version UID of this class. Needed for serialization. */
    private static final long serialVersionUID = -1115527185418128461L;

    // Class template parameters

    /**
     * TODO: Model Documentation for T
     */
    protected T T;

    /**
     * TODO: Model Documentation for T
     * Get the T Template Parameter
     * @return T T
     */
    public T getT()
    {
        return this.T;
    }

    /**
     * Set the T Template Parameter
     * @param value T
     */
    public void setT(T value)
    {
        this.T = value;
    }

    // Class attributes
    /**
     * TODO: Model Documentation for SearchObject.criteria
     */
    protected T criteria;
    /**
     * TODO: Model Documentation for SearchObject.paged
     */
    @Nullable
    protected Boolean paged = false;
    /**
     * TODO: Model Documentation for SearchObject.pageNumber
     */
    @Nullable
    protected Integer pageNumber;
    /**
     * TODO: Model Documentation for SearchObject.pageSize
     */
    @Nullable
    protected Integer pageSize;
    /**
     * TODO: Model Documentation for SearchObject.sortings
     */
    @Nullable
    protected List<PropertySearchOrder> sortings;

    /** Default Constructor with no properties */
    public SearchObject()
    {
        // Documented empty block - avoid compiler warning - no super constructor
    }

    /**
     * Constructor taking only required properties
     * @param criteriaIn T TODO: Model Documentation for SearchObject.criteria
     */
    public SearchObject(final T criteriaIn)
    {
        this.criteria = criteriaIn;
    }

    /**
     * Constructor with all properties
     * @param criteriaIn T
     * @param pagedIn Boolean
     * @param pageNumberIn Integer
     * @param pageSizeIn Integer
     * @param sortingsIn List<PropertySearchOrder>
     */
    public SearchObject(final T criteriaIn, final Boolean pagedIn, final Integer pageNumberIn, final Integer pageSizeIn, final List<PropertySearchOrder> sortingsIn)
    {
        this.criteria = criteriaIn;
        this.paged = pagedIn;
        this.pageNumber = pageNumberIn;
        this.pageSize = pageSizeIn;
        this.sortings = sortingsIn;
    }

    /**
     * Copies constructor from other SearchObject
     *
     * @param otherBean Cannot be <code>null</code>
     * @throws NullPointerException if the argument is <code>null</code>
     */
    public SearchObject(final SearchObject<T> otherBean)
    {
        this.criteria = otherBean.getCriteria();
        this.paged = otherBean.getPaged();
        this.pageNumber = otherBean.getPageNumber();
        this.pageSize = otherBean.getPageSize();
        this.sortings = otherBean.getSortings();
    }

    /**
     * Copies all properties from the argument value object into this value object.
     * @param otherBean Cannot be <code>null</code>
     */
    public void copy(final SearchObject<T> otherBean)
    {
        if (null != otherBean)
        {
            this.setCriteria(otherBean.getCriteria());
            this.setPaged(otherBean.getPaged());
            this.setPageNumber(otherBean.getPageNumber());
            this.setPageSize(otherBean.getPageSize());
            this.setSortings(otherBean.getSortings());
        }
    }

    /**
     * TODO: Model Documentation for SearchObject.criteria
     * Get the criteria Attribute
     * @return criteria T
     */
    public T getCriteria()
    {
        return this.criteria;
    }

    /**
     * TODO: Model Documentation for SearchObject.criteria
     * @param value T
     */
    public void setCriteria(final T value)
    {
        this.criteria = value;
    }

    /**
     * TODO: Model Documentation for SearchObject.paged
     * Get the paged Attribute
     * @return paged Boolean
     */
    public Boolean getPaged()
    {
        return this.paged;
    }

    /**
     * TODO: Model Documentation for SearchObject.paged
     * @param value Boolean
     */
    public void setPaged(final Boolean value)
    {
        this.paged = value;
    }

    /**
     * TODO: Model Documentation for SearchObject.pageNumber
     * Get the pageNumber Attribute
     * @return pageNumber Integer
     */
    public Integer getPageNumber()
    {
        return this.pageNumber;
    }

    /**
     * TODO: Model Documentation for SearchObject.pageNumber
     * @param value Integer
     */
    public void setPageNumber(final Integer value)
    {
        this.pageNumber = value;
    }

    /**
     * TODO: Model Documentation for SearchObject.pageSize
     * Get the pageSize Attribute
     * @return pageSize Integer
     */
    public Integer getPageSize()
    {
        return this.pageSize;
    }

    /**
     * TODO: Model Documentation for SearchObject.pageSize
     * @param value Integer
     */
    public void setPageSize(final Integer value)
    {
        this.pageSize = value;
    }

    /**
     * TODO: Model Documentation for SearchObject.sortings
     * Get the sortings Attribute
     * @return sortings List<PropertySearchOrder>
     */
    public List<PropertySearchOrder> getSortings()
    {
        if (this.sortings == null)
        {
            this.sortings = new ArrayList<PropertySearchOrder>();
        }
        return this.sortings;
    }

    /**
     * TODO: Model Documentation for SearchObject.sortings
     * @param value List<PropertySearchOrder>
     */
    public void setSortings(final List<PropertySearchOrder> value)
    {
        this.sortings = value;
    }

    /**
     * @param object to compare this object against
     * @return boolean if equal
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(final Object object)
    {
        if (object==null || object.getClass() != this.getClass())
        {
             return false;
        }
        // Check if the same object instance
        if (object==this)
        {
            return true;
        }
        SearchObject<T> rhs = (SearchObject<T>) object;
        return new EqualsBuilder()
            .append(this.getCriteria(), rhs.getCriteria())
            .append(this.getPaged(), rhs.getPaged())
            .append(this.getPageNumber(), rhs.getPageNumber())
            .append(this.getPageSize(), rhs.getPageSize())
            .append(this.getSortings(), rhs.getSortings())
            .isEquals();
    }

    /**
     * @param object to compare this object against
     * @return int if equal
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(final SearchObject object)
    {
        if (object==null)
        {
            return -1;
        }
        // Check if the same object instance
        if (object==this)
        {
            return 0;
        }
        return new CompareToBuilder()
            .append(this.getCriteria(), object.getCriteria())
            .append(this.getPaged(), object.getPaged())
            .append(this.getPageNumber(), object.getPageNumber())
            .append(this.getPageSize(), object.getPageSize())
            .append(this.getSortings(), object.getSortings())
            .toComparison();
    }

    /**
     * @return int hashCode value
     * @see Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(1249046965, -82296885)
            .append(this.getCriteria())
            .append(this.getPaged())
            .append(this.getPageNumber())
            .append(this.getPageSize())
            .append(this.getSortings())
            .toHashCode();
    }

    /**
     * @return String representation of object
     * @see Object#toString()
     */
    @Override
    public String toString()
    {

        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        builder.append("\t\"criteria\" : \"" + this.getCriteria() + "\"");
        builder.append("\t,\"paged\" : \"" + this.getPaged() + "\"");
        builder.append("\t,\"pageNumber\" : \"" + this.getPageNumber() + "\"");
        builder.append("\t,\"pageSize\" : \"" + this.getPageSize() + "\"");
        builder.append("\t,\"sortings\" : \"" + this.getSortings() + "\"");
        builder.append("}");
        return builder.toString();

    }

    /**
     * Compares the properties of this instance to the properties of the argument. This method will return
     * {@code false} as soon as it detects that the argument is {@code null} or not of the same type as
     * (or a sub-type of) this instance's type.
     *
     * <p/>For array, collection or map properties the comparison will be done one level deep, in other words:
     * the elements will be compared using the {@code equals()} operation.
     *
     * <p/>Note that two properties will be considered equal when both values are {@code null}.
     *
     * @param thatObject the object containing the properties to compare against this instance
     * @return this method will return {@code true} in case the argument has the same type as this class, or is a
     *      sub-type of this class and all properties as found on this class have equal values when queried on that
     *      argument instance; in all other cases this method will return {@code false}
     */
    public boolean equalProperties(final Object thatObject)
    {
        if (thatObject == null || !this.getClass().isAssignableFrom(thatObject.getClass()))
        {
            return false;
        }

        final SearchObject<T> that = (SearchObject<T>)thatObject;
        return
            equal(this.getCriteria(), that.getCriteria())
            && equal(this.getPaged(), that.getPaged())
            && equal(this.getPageNumber(), that.getPageNumber())
            && equal(this.getPageSize(), that.getPageSize())
            && equal(this.getSortings(), that.getSortings())
        ;
    }

    /**
     * This is a convenient helper method which is able to detect whether or not two values are equal. Two values
     * are equal when they are both {@code null}, are arrays of the same length with equal elements or are
     * equal objects (this includes {@link java.util.Collection} and {@link java.util.Map} instances).
     *
     * <p/>Note that for array, collection or map instances the comparison runs one level deep.
     *
     * @param first the first object to compare, may be {@code null}
     * @param second the second object to compare, may be {@code null}
     * @return this method will return {@code true} in case both objects are equal as explained above;
     *      in all other cases this method will return {@code false}
     */
    protected static boolean equal(final Object first, final Object second)
    {
        final boolean equal;

        if (first == null)
        {
            equal = (second == null);
        }
        else if (first.getClass().isArray() && (second != null) && second.getClass().isArray())
        {
            equal = Arrays.equals((Object[])first, (Object[])second);
        }
        else // note that the following also covers java.util.Collection and java.util.Map
        {
            equal = first.equals(second);
        }

        return equal;
    }

}