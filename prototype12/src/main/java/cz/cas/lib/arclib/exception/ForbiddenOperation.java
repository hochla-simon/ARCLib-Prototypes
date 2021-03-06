package cz.cas.lib.arclib.exception;

import cz.cas.lib.arclib.domain.general.DomainObject;

public class ForbiddenOperation extends GeneralException {
    private Object object;

    public ForbiddenOperation(Object object) {
        super();
        this.object = object;
    }

    public ForbiddenOperation(Class<?> clazz, String objectId) {
        super();
        try {
            this.object = clazz.newInstance();

            if (DomainObject.class.isAssignableFrom(clazz)) {
                ((DomainObject)this.object).setId(objectId);
            }

        } catch (Exception e) {
            // ignore
        }
    }

    @Override
    public String toString() {
        return "ForbiddenOperation{" +
                "object=" + object +
                '}';
    }

    public Object getObject() {
        return object;
    }
}
