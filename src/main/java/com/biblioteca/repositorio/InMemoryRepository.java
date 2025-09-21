package com.biblioteca.repositorio;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio genérico en memoria.
 * - Asigna ID automáticamente al guardar (save).
 * - Soporta entidades con id Long o Integer (getId/setId).
 * - Permite findAll, findById, genericFindByField, genericUpdate, genericDelete.
 */
public class InMemoryRepository<T> {

    private final Map<Long, T> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0L);

    /** Crea/actualiza la entidad. Si no tiene id, le asigna uno nuevo. */
    public T save(T entity) {
        Objects.requireNonNull(entity, "entity no puede ser null");
        Long id = readIdAsLong(entity);

        if (id == null || id == 0L) { // Entidad nueva
            id = sequence.incrementAndGet();
            writeIdFromLong(entity, id);
        } else {
            // Evita la captura de variable en lambda
            sequence.accumulateAndGet(id, Math::max);
        }

        store.put(id, entity);
        return entity;
    }


    /** b) Buscar una entidad por ID */
    public Optional<T> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    /** a) Mostrar todas las entidades */
    public List<T> findAll() {
        return new ArrayList<>(store.values());
    }

    /** e) Eliminar por ID */
    public boolean genericDelete(Long id) {
        return store.remove(id) != null;
    }

    /** c) Buscar por nombre de campo (igualdad exacta) */
    public List<T> genericFindByField(String fieldName, Object expectedValue) {
        Objects.requireNonNull(fieldName, "fieldName no puede ser null");
        List<T> result = new ArrayList<>();
        for (T entity : store.values()) {
            Object fieldVal = getFieldValue(entity, fieldName);
            if (Objects.equals(fieldVal, expectedValue)) {
                result.add(entity);
            }
        }
        return result;
    }

    /** d) Actualizar un campo por ID (ej: CUIL) */
    public boolean genericUpdate(Long id, String fieldName, Object newValue) {
        T entity = store.get(id);
        if (entity == null) return false;
        setFieldValue(entity, fieldName, newValue);
        return true;
    }

    // ===================== Helpers de reflexión =====================

    private Long readIdAsLong(T entity) {
        // 1) getter getId()
        try {
            Method m = entity.getClass().getMethod("getId");
            Object value = m.invoke(entity);
            return coerceToLongOrNull(value);
        } catch (Exception ignored) {}

        // 2) campo id
        try {
            Field f = entity.getClass().getDeclaredField("id");
            f.setAccessible(true);
            Object value = f.get(entity);
            return coerceToLongOrNull(value);
        } catch (Exception ignored) {}

        return null;
    }

    private void writeIdFromLong(T entity, Long idValue) {
        // 1) setId(Long)
        try {
            Method m = entity.getClass().getMethod("setId", Long.class);
            m.invoke(entity, idValue);
            return;
        } catch (Exception ignored) {}

        // 2) setId(long)
        try {
            Method m = entity.getClass().getMethod("setId", long.class);
            m.invoke(entity, idValue.longValue());
            return;
        } catch (Exception ignored) {}

        // 3) setId(Integer)
        try {
            Method m = entity.getClass().getMethod("setId", Integer.class);
            m.invoke(entity, idValue.intValue());
            return;
        } catch (Exception ignored) {}

        // 4) setId(int)
        try {
            Method m = entity.getClass().getMethod("setId", int.class);
            m.invoke(entity, idValue.intValue());
            return;
        } catch (Exception ignored) {}

        // 5) Escribir directo en el campo id
        try {
            Field f = entity.getClass().getDeclaredField("id");
            f.setAccessible(true);
            Class<?> t = f.getType();
            if (t.equals(Long.class) || t.equals(long.class)) {
                f.set(entity, idValue);
            } else if (t.equals(Integer.class) || t.equals(int.class)) {
                f.set(entity, idValue.intValue());
            } else if (t.equals(String.class)) {
                f.set(entity, String.valueOf(idValue));
            } else {
                throw new IllegalStateException("Tipo de id no soportado: " + t.getSimpleName());
            }
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo asignar id por reflexión", e);
        }
    }

    private Object getFieldValue(T entity, String fieldName) {
        // 1) getter
        try {
            String getter = "get" + capitalize(fieldName);
            Method m = entity.getClass().getMethod(getter);
            return m.invoke(entity);
        } catch (Exception ignored) {}

        // 2) campo directo
        try {
            Field f = entity.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(entity);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "No existe el campo '" + fieldName + "' en " + entity.getClass().getSimpleName(), e
            );
        }
    }

    private void setFieldValue(T entity, String fieldName, Object newValue) {
        // 1) setter tipado
        try {
            Field f = entity.getClass().getDeclaredField(fieldName);
            Class<?> type = f.getType();
            String setter = "set" + capitalize(fieldName);
            Method m = entity.getClass().getMethod(setter, type);
            m.invoke(entity, convertValue(newValue, type));
            return;
        } catch (NoSuchFieldException | NoSuchMethodException ignored) {
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo setear '" + fieldName + "'", e);
        }

        // 2) campo directo
        try {
            Field f = entity.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(entity, convertValue(newValue, f.getType()));
        } catch (Exception e) {
            throw new IllegalStateException("No se pudo setear '" + fieldName + "'", e);
        }
    }

    private Long coerceToLongOrNull(Object value) {
        if (value == null) return null;
        if (value instanceof Long)    return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        if (value instanceof Short)   return ((Short) value).longValue();
        if (value instanceof String)  return Long.valueOf((String) value);
        return null;
    }

    private Object convertValue(Object value, Class<?> targetType) {
        if (value == null) return null;
        if (targetType.isInstance(value)) return value;

        String s = String.valueOf(value);
        if (targetType.equals(String.class)) return s;
        if (targetType.equals(Long.class) || targetType.equals(long.class)) return Long.valueOf(s);
        if (targetType.equals(Integer.class) || targetType.equals(int.class)) return Integer.valueOf(s);
        if (targetType.equals(Short.class) || targetType.equals(short.class)) return Short.valueOf(s);
        if (targetType.equals(Boolean.class) || targetType.equals(boolean.class)) return Boolean.valueOf(s);
        if (targetType.equals(Double.class) || targetType.equals(double.class)) return Double.valueOf(s);
        if (targetType.equals(Float.class)  || targetType.equals(float.class))  return Float.valueOf(s);
        if (targetType.isEnum()) {
            @SuppressWarnings("unchecked")
            Class<? extends Enum> et = (Class<? extends Enum>) targetType;
            return Enum.valueOf(et, s);
        }
        throw new IllegalArgumentException("No sé convertir '" + s + "' a " + targetType.getSimpleName());
    }

    private String capitalize(String name) {
        if (name == null || name.isEmpty()) return name;
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
