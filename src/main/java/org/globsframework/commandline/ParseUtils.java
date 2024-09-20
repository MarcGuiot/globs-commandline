package org.globsframework.commandline;

import org.globsframework.core.metamodel.fields.Field;
import org.globsframework.core.metamodel.type.DataType;

public class ParseUtils {

    private ParseUtils() {
    }

    public static boolean fieldHasDefaultValue(Field field) {
        return field.getDefaultValue() != null;
    }

    public static boolean fieldIsABoolean(Field field) {
        return field.getDataType() == DataType.Boolean;
    }

}
