package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.*;
import org.globsframework.metamodel.type.DataType;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;
import org.globsframework.utils.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ParseCommandLine {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParseCommandLine.class);

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
                    args.add("--" + field.getName());
                    args.addAll(Arrays.asList(values));
                } else if (field instanceof BooleanField) {
                    args.add("--" + field.getName());
                }
                else {
                    throw new RuntimeException("for " + field.getDataType() + " not managed for " + field.getName());
                }
            }
        }
        return args.toArray(new String[0]);
    }

    public static Glob parse(GlobType type, String[] line) {
        return parse(type, new ArrayList<>(Arrays.asList(line)), false);
    }

    public static Glob parse(GlobType type, List<String> line, boolean ignoreUnknown) {
        LOGGER.info("parse: " + line);
        MutableGlob instantiate = type.instantiate();
        Field[] fields = type.getFields();
        for (Field field : fields) {
            if (field.getDefaultValue() != null) {
                instantiate.setValue(field, field.getDefaultValue());
            }
        }
        Field lastField = null;
        for (Iterator<String> iterator = line.iterator(); iterator.hasNext(); ) {
            String s = iterator.next();
            if (s.startsWith("--")) {
                String name = s.substring(2);
                lastField = type.findField(name);
                if (lastField != null) {
                    iterator.remove();
                    if (lastField.getDataType() == DataType.Boolean) {
                        instantiate.setValue(lastField, Boolean.TRUE);
                    } else {
                        if (!iterator.hasNext()) {
                            throw new ParseError("Missing parameter for " + s);
                        }
                        StringConverter.FromStringConverter converter = StringConverter.createConverter(lastField,
                                lastField.findOptAnnotation(ArraySeparator.KEY).map(glob -> glob.get(ArraySeparator.SEPARATOR)).orElse(","));
                        converter.convert(instantiate, iterator.next());
                        iterator.remove();
                    }
                } else if (!ignoreUnknown) {
                    throw new ParseError("Unknown parameter " + name);
                }
            }
            else if (lastField != null && lastField.getDataType().isArray()) {
                StringConverter.FromStringConverter converter = StringConverter.createConverter(lastField,
                        lastField.findOptAnnotation(ArraySeparator.KEY).map(glob -> glob.get(ArraySeparator.SEPARATOR)).orElse(","));
                converter.convert(instantiate, s);
                iterator.remove();
            } else if (!ignoreUnknown) {
                throw new ParseError("Unknown parameter " + s);
            } else {
                lastField = null;
            }
        }
        for (Field field : type.getFields()) {
            if (!instantiate.isSet(field) && field.hasAnnotation(Mandatory.KEY)) {
                throw new ParseError("Missing argument " + field);
            }
        }
        LOGGER.info("Return : " + instantiate.toString());
        return instantiate;
    }

    //add void usage(GlobType type, Printer printer);
}
