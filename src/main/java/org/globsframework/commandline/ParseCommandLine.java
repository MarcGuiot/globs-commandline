package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.fields.*;
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

    private ParseCommandLine() {}

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

        setDefaultValues(fields, instantiate);

        Field lastField = null;
        for (Iterator<String> iterator = line.iterator(); iterator.hasNext(); ) {
            String param = iterator.next();
            if (param.startsWith("--")) {
                String name = param.substring(2);
                lastField = type.findField(name);
                if (lastField != null) {
                    iterator.remove();
                    if (ParseUtils.fieldIsABoolean(lastField)) {
                        instantiate.setValue(lastField, Boolean.TRUE);
                    } else {
                        assignValueToField(lastField, iterator, instantiate,param);
                    }
                } else if (!ignoreUnknown) {
                    throw new ParseError("Unknown parameter " + name);
                }
            }
            else if (ParseUtils.fieldIsAnArray(lastField)) {
                StringConverter.FromStringConverter converter = StringConverter.createConverter(lastField,
                        lastField.findOptAnnotation(ArraySeparator.KEY).map(glob -> glob.get(ArraySeparator.SEPARATOR)).orElse(","));
                converter.convert(instantiate, param);
                iterator.remove();
            } else if (!ignoreUnknown) {
                throw new ParseError("Unknown parameter " + param);
            } else {
                lastField = null;
            }
        }

        checkMandatoryFields(type, instantiate);
        LOGGER.info("Return : " + instantiate.toString());
        return instantiate;
    }

    private static void checkMandatoryFields(GlobType type, MutableGlob instantiate) {
        for (Field field : type.getFields()) {
            if (!instantiate.isSet(field) && field.hasAnnotation(Mandatory.KEY)) {
                throw new ParseError("Missing argument " + field);
            }
        }
    }

    private static void assignValueToField(Field lastField, Iterator<String> iterator, MutableGlob instantiate, String s) {
        if (!iterator.hasNext()) {
            throw new ParseError("Missing parameter for " + s);
        }
        StringConverter.FromStringConverter converter = StringConverter.createConverter(lastField,
                lastField.findOptAnnotation(ArraySeparator.KEY).map(glob -> glob.get(ArraySeparator.SEPARATOR)).orElse(","));
        converter.convert(instantiate, iterator.next());
        iterator.remove();
    }

    private static void setDefaultValues(Field[] fields, MutableGlob instantiate) {

        for (Field field : fields) {
            if (ParseUtils.fieldHasDefaultValue(field)) {
                instantiate.setValue(field, field.getDefaultValue());
            }
        }
    }
}
