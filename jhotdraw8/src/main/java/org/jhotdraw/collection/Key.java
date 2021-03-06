/*
 * @(#)Key.java
 * Copyright (c) 2015 by the authors and contributors of JHotDraw.
 * You may only use this file in compliance with the accompanying license terms.
 */
package org.jhotdraw.collection;

import java.util.Map;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.MapExpression;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableMap;

/**
 * An <em>name</em> which provides typesafe access to a map entry.
 * <p>
 * A Key has a name, a type and a default value.
 * <p>
 * The following code example shows how to set and get a value from a map.
 * <pre>
 * {@code
 * String value = "Werner";
 * Key<String> stringKey = new Key("name",String.class,null);
 * Map<Key<?>,Object> map = new HashMap<>();
 * stringKey.put(map, value);
 * }
 * </pre>
 * <p>
 * Note that {@code Key} is not a value type. Thus using two distinct instances
 * of a Key will result in two distinct entries in the hash map, even if both
 * keys have the same name.
 *
 * @author Werner Randelshofer
 * @version $Id$
 * @param <T> The value type.
 */
public interface Key<T> extends MapAccessor<T> {

    final static long serialVersionUID = 1L;

    public String getFullValueType();

    /**
     * Gets the value of the attribute denoted by this Key from a Map.
     *
     * @param a A Map.
     * @return The value of the attribute.
     */
    @Override
    default T get(Map<? super Key<?>, Object> a) {
        @SuppressWarnings("unchecked")
        T value = a.containsKey(this) ? (T) a.get(this) : getDefaultValue();
        //assert isAssignable(value) : value + " is not assignable to " + getValueType();
        return value;
    }

    /**
     * Gets the value of the attribute denoted by this Key from a Map.
     *
     * @param a A Map.
     * @return The value of the attribute.
     */
    default ObjectProperty<T> getValueProperty(Map<? super Key<?>, ObjectProperty<?>> a) {
        if (!a.containsKey(this)) {
            a.put(this, new SimpleObjectProperty<T>(getDefaultValue()));
        }
        @SuppressWarnings("unchecked")
        SimpleObjectProperty<T> value = (SimpleObjectProperty<T>) a.get(this);
        return value;
    }

    /**
     * Gets the value of the attribute denoted by this Key from a Map.
     *
     * @param a A Map.
     * @return The value of the attribute.
     */
    default T getValue(Map<? super Key<?>, ObjectProperty<?>> a) {
        if (!a.containsKey(this)) {
            a.put(this, new SimpleObjectProperty<T>(getDefaultValue()));
        }
        @SuppressWarnings("unchecked")
        SimpleObjectProperty<T> value = (SimpleObjectProperty<T>) a.get(this);
        return value.get();
    }

    /**
     * Use this method to perform a type-safe put operation of an attribute into
     * a Map.
     *
     * @param a An attribute map.
     * @param value The new value.
     * @return The old value.
     */
    @Override
    default T put(Map<? super Key<?>, Object> a, T value) {
        if (!isAssignable(value)) {
            throw new IllegalArgumentException("Value is not assignable to key. key="
                    + this + ", value=" + value);
        }
        @SuppressWarnings("unchecked")
        T oldValue = (T) a.put(this, value);
        return oldValue;
    }

    /**
     * Use this method to perform a type-safe remove operation of an attribute
     * on a Map.
     *
     * @param a An attribute map.
     * @return The old value.
     */
    @Override
    default T remove(Map<? super Key<?>, Object> a) {
        @SuppressWarnings("unchecked")
        T oldValue = (T) a.remove(this);
        return oldValue;
    }

    /**
     * Use this method to perform a type-safe put operation of an attribute into
     * a Map.
     *
     * @param a An attribute map.
     * @param value The new value.
     * @return The old value.
     */
    default T putValue(Map<? super Key<?>, ObjectProperty<?>> a, T value) {
        if (!isAssignable(value)) {
            throw new IllegalArgumentException("Value is not assignable to key. key="
                    + this + ", value=" + value);
        }
        if (a.containsKey(this)) {
            @SuppressWarnings("unchecked")
            ObjectProperty<T> p = (ObjectProperty<T>) a.get(this);
            T oldValue = p.get();
            p.set(value);
            return oldValue;
        } else {
            a.put(this, new SimpleObjectProperty<>(value));
            return null;
        }
    }

    /**
     * Whether the value may be set to null.
     *
     * @return true if nullable
     */
    boolean isNullable();

    /**
     * Returns true if the specified value is assignable with this key.
     *
     * @param value The object to be verified for assignability.
     * @return True if assignable.
     */
    default boolean isAssignable(Object value) {
        return value == null && isNullable() || getValueType().isInstance(value);
    }

    /**
     * Returns true if the specified value is the default value of this key.
     *
     * @param value The object to be verified for assignability.
     * @return True if assignable.
     */
    default boolean isDefault(Object value) {
        return (getDefaultValue() == null)
                ? value == null : getDefaultValue().equals(value);
    }

    /**
     * Creates a new binding for the map entry specified by this key.
     *
     * @param map a map
     * @return a binding for the map entry
     */
    default Binding<T> valueAt(ObservableMap<Key<?>, Object> map) {
        ObjectBinding<Object> value = Bindings.valueAt(map, this);
        @SuppressWarnings("unchecked")
        Binding<T> binding = (ObjectBinding<T>) value;
        return binding;
    }

    /**
     * Creates a new property for the map entry specified by this key.
     *
     * @param map a map
     * @return a property for the map entry
     */
    default Property<T> propertyAt(final ObservableMap<Key<?>, Object> map) {
        ObjectBinding<Object> value = Bindings.valueAt(map, this);
        return new KeyMapEntryProperty<>(map, this);
    }

    /**
     * Creates a new read-only property for the map entry specified by this key.
     *
     * @param map a map
     * @return a property for the map entry
     */
    default ReadOnlyProperty<T> readOnlyPropertyAt(final MapExpression<Key<?>, Object> map) {
        ObjectBinding<Object> value = map.valueAt(this);
        return new KeyMapEntryProperty<>(map, this);
    }

}
