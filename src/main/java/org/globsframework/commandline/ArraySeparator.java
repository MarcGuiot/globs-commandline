package org.globsframework.commandline;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeBuilder;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.metamodel.fields.StringField;
import org.globsframework.core.metamodel.impl.DefaultGlobTypeBuilder;
import org.globsframework.core.model.Key;
import org.globsframework.core.model.KeyBuilder;

public class ArraySeparator {
    public static final GlobType TYPE;

    public static final StringField SEPARATOR;

    @InitUniqueKey
    public static final Key KEY;

    static {
        GlobTypeBuilder typeBuilder = new DefaultGlobTypeBuilder("ArraySeparator");
        TYPE = typeBuilder.unCompleteType();
        SEPARATOR = typeBuilder.declareStringField("separator");
        typeBuilder.complete();
        KEY = KeyBuilder.newEmptyKey(TYPE);
        typeBuilder.register(GlobCreateFromAnnotation.class, annotation -> ArraySeparator.TYPE.instantiate()
                .set(SEPARATOR, String.valueOf(((ArraySeparator_) annotation).value())));
//        GlobTypeLoaderFactory.create(ArraySeparator.class, "ArraySeparator")
//                .register(GlobCreateFromAnnotation.class, annotation -> ArraySeparator.TYPE.instantiate()
//                        .set(SEPARATOR, new String(new char[]{((ArraySeparator_) annotation).value()})))
//                .load();
    }
}
