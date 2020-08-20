package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.DoubleField;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.metamodel.fields.StringArrayField;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.metamodel.type.DataType;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.utils.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ParseCommandLine {

    public static String[] toArgs(Glob glob) {
        List<String> args = new ArrayList<>();
        for (Field field : glob.getType().getFields()) {
            if (!glob.isNull(field)) {
                if (field instanceof StringField) {
                    args.add("--" + field.getName());
                    args.add(glob.get((StringField) field));
                } else if (field instanceof DoubleField) {
                    args.add("--" + field.getName());
                    args.add(glob.get((DoubleField) field).toString());
                } else if (field instanceof IntegerField) {
                    args.add("--" + field.getName());
                    args.add(glob.get((IntegerField) field).toString());
                } else if (field instanceof StringArrayField) {
                    String[] values = glob.getOrEmpty((StringArrayField) field);
                    for (String value : values) {
                        args.add("--" + field.getName());
                        args.add(value);
                    }
                }
            }
        }
        return args.toArray(new String[0]);
    }

    public static Glob parse(GlobType type, String[] line) {
        return parse(type, new ArrayList<>(Arrays.asList(line)), false);
    }

    public static Glob parse(GlobType type, List<String> line, boolean ignoreUnknown) {
        MutableGlob instantiate = type.instantiate();
        Field[] fields = type.getFields();
        for (Field field : fields) {
            instantiate.setValue(field, field.getDefaultValue());
        }
        for (Iterator<String> iterator = line.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (s.startsWith("--")) {
                String name = s.substring(2);
                Field field = type.findField(name);
                if (field != null) {
                    iterator.remove();
                    if (field.getDataType() == DataType.Boolean) {
                        instantiate.setValue(field, Boolean.TRUE);
                    } else {
                        if (!iterator.hasNext()) {
                            throw new ParseError("Missing parameter for " + s);
                        }
                        StringConverter.FromStringConverter converter = StringConverter.createConverter(field, null);
                        converter.convert(instantiate, iterator.next());
                        iterator.remove();
                    }
                } else if (!ignoreUnknown) {
                    throw new ParseError("Unknown parameter " + name);
                }
            }
        }
        for (Field field : type.getFields()) {
            if (!instantiate.isSet(field) && field.hasAnnotation(Mandatory.KEY)) {
                throw new ParseError("Missing argument " + field);
            }
        }
        return instantiate;
    }
}
