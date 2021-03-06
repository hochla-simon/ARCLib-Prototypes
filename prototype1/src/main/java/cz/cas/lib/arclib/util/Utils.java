package cz.cas.lib.arclib.util;

import cz.cas.lib.arclib.exception.GeneralException;
import cz.cas.lib.arclib.domain.DomainObject;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Utils {

    public static <T> T unwrap(T a) {
        if(isProxy(a)) {
            try {
                return (T) ((Advised)a).getTargetSource().getTarget();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return a;
        }
    }

    public static boolean isProxy(Object a) {
        return (AopUtils.isAopProxy(a) && a instanceof Advised);
    }


    public static <T extends RuntimeException> void notNull(Object o, Supplier<T> supplier) {
        if (o == null) {
            throw supplier.get();
        } else if (o instanceof Optional) {
            if(!((Optional)o).isPresent()) {
                throw supplier.get();
            }
        } else if (isProxy(o)) {
            if (unwrap(o) == null) {
                throw supplier.get();
            }
        }
    }
    public static <T extends DomainObject> List<T> sortByIdList(List<String> ids, Iterable<T> objects) {
        Map<String, T> map = StreamSupport.stream(objects.spliterator(), true)
                .collect(Collectors.toMap(DomainObject::getId, o -> o));

        return ids.stream()
                .map(map::get)
                .filter(o -> o != null)
                .collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface Checked {
        void checked() throws Exception;
    }

    public static void checked(Checked method) {
        try {
            method.checked();
        } catch (Exception ex) {
            if (ex instanceof GeneralException) {
                throw (GeneralException)ex;
            } else {
                throw new GeneralException(ex);
            }

        }
    }

    public static <T> Set<T> asSet(T... a) {
        return new HashSet<>(Arrays.asList(a));
    }

    public static <U, T extends RuntimeException> void in(U o1,  Collection<U> o2, Supplier<T> supplier) {
        if (!o2.contains(o1)) {
            throw supplier.get();
        }
    }

    public static <T> Map<String, T> asMap(String key, T value) {
        return Collections.singletonMap(key, value);
    }

    public static <T, U> Map<T, U> asMap(T key1, U value1, T key2, U value2) {
        Map<T, U> map = new LinkedHashMap<>();
        map.put(key1, value1);
        map.put(key2, value2);
        return map;
    }

    public static <T> List<T> asList(Collection<T> a) {
        return a.stream().collect(Collectors.toList());
    }
}
