package org.globsframework.commandline;

import org.globsframework.metamodel.Field;
import org.globsframework.metamodel.GlobType;
import org.globsframework.model.Glob;
import org.globsframework.model.MutableGlob;

import java.util.Locale;
import java.util.Map;

public class ParseEnvironment {

    public static Glob parseEnv(String prefix, GlobType type) throws EnvironmentVariableNotSetException {
        return parse(prefix, type, System.getenv());
    }

    public static Glob parse(String prefix, GlobType type, Map<String, String> envVars) throws EnvironmentVariableNotSetException {

        MutableGlob instantiate = type.instantiate();
        Field[] fields = type.getFields();
        for (Field field : fields){
            String envVar = convertToEnvVar(prefix, type, field);
            String val = envVars.get(envVar);
            if(val != null) {
                instantiate.setValue(field, val);
            }
            if(!instantiate.isSet(field) && field.getDefaultValue() != null){
                instantiate.setValue(field, field.getDefaultValue());
            }

        }

        for (Field field : type.getFields()) {
            if (!instantiate.isSet(field) && field.hasAnnotation(Mandatory.KEY)) {
                throw new EnvironmentVariableNotSetException("Required environment Variable is not set:" + convertToEnvVar(prefix, type, field));
            }
        }

        return instantiate;
    }

    private static String convertToEnvVar(String prefix, GlobType type, Field field) {
        return prefix.toUpperCase(Locale.ROOT)+ "_" + type.getName().toUpperCase(Locale.ROOT) + "_" + camelToSnake(field.getName()).toUpperCase(Locale.ROOT);
    }

    private static String camelToSnake(String str) {

        // Empty String
        String result = "";

        // Append first character(in lower case)
        // to result string
        char c = str.charAt(0);
        result = result + Character.toLowerCase(c);

        // Traverse the string from
        // ist index to last index
        for (int i = 1; i < str.length(); i++) {

            char ch = str.charAt(i);

            // Check if the character is upper case
            // then append '_' and such character
            // (in lower case) to result string
            if (Character.isUpperCase(ch)) {
                result = result + '_';
                result
                        = result
                        + Character.toLowerCase(ch);
            }

            // If the character is lower case then
            // add such character into result string
            else {
                result = result + ch;
            }
        }

        // return the result
        return result;
    }
}
