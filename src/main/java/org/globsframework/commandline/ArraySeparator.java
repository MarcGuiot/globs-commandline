package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.metamodel.annotations.InitUniqueGlob;
import org.globsframework.metamodel.annotations.InitUniqueKey;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.globsframework.model.Key;

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
