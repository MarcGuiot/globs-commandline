package org.globsframework.commandline;

import org.globsframework.utils.exceptions.GlobsException;

public class ParseError extends GlobsException {
    public ParseError(String message) {
        super(message);
    }
}
