package org.globsframework.commandline;

public class EnvironmentVariableNotSetException extends Exception {
    public EnvironmentVariableNotSetException(String s) {
        super(s);
    }
}
