package org.globsframework.commandline;

import org.globsframework.core.metamodel.GlobType;
import org.globsframework.core.metamodel.GlobTypeLoaderFactory;
import org.globsframework.core.metamodel.annotations.GlobCreateFromAnnotation;
import org.globsframework.core.metamodel.annotations.InitUniqueGlob;
import org.globsframework.core.metamodel.annotations.InitUniqueKey;
import org.globsframework.core.model.Glob;
import org.globsframework.core.model.Key;

public class Mandatory {
    public static GlobType TYPE;

    @InitUniqueKey
    public static Key KEY;

    @InitUniqueGlob
    public static Glob UNIQUE;

    static {
        GlobTypeLoaderFactory.create(Mandatory.class, "Mandatory")
                .register(GlobCreateFromAnnotation.class, annotation -> UNIQUE)
                .load();
    }
}
