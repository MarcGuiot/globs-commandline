package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.type.DataType;

public class ParseUtils {

    private ParseUtils() {}

    public static boolean fieldHasDefaultValue(Field field) {
        return field.getDefaultValue() != null;
    }

    public static boolean fieldIsABoolean(Field field) {
        return field.getDataType() == DataType.Boolean;
    }

    public static boolean fieldIsAnArray(Field field) {
        return field != null && field.getDataType().isArray();
    }

}
