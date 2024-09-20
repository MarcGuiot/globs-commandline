package org.globsframework.commandline;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.model.Key;

public class ArraySeparator {
    public static GlobType TYPE;

    public static StringField SEPARATOR;

    @InitUniqueKey
    public static Key KEY;

    static {
        GlobTypeLoaderFactory.create(ArraySeparator.class)
                .register(GlobCreateFromAnnotation.class, annotation -> ArraySeparator.TYPE.instantiate()
                        .set(SEPARATOR, new String(new char[]{((ArraySeparator_) annotation).value()})))
                .load();
    }
}
