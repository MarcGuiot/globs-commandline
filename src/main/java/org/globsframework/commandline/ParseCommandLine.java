package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.type.DataType;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.utils.StringConverter;

public class ParseCommandLine {

    public static Glob parse(GlobType type, String[] line) {
        MutableGlob instantiate = type.instantiate();
        for (int i = 0; i < line.length; i++) {
            String s = line[i];
            if (s.startsWith("--")) {
                String name = s.substring(2, s.length());
                Field field = type.getField(name);
                if (field.getDataType() == DataType.Boolean) {
                    instantiate.setValue(field, Boolean.TRUE);
                } else {
                    if (i + 1 > line.length) {
                        throw new RuntimeException("Missing parameter for " + s);
                    }
                    StringConverter.FromStringConverter converter = StringConverter.createConverter(field);
                    converter.convert(instantiate, line[i+1]);
                    i++;
                }
            }
        }
        for (Field field : type.getFields()) {
            if (!instantiate.isSet(field) && field.hasAnnotation(Mandatory.KEY)) {
                throw new RuntimeException("Missing argument " + field);
            }
        }
        return instantiate;
    }
}
