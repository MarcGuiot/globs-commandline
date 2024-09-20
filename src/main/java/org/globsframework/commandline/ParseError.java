package org.globsframework.commandline;

import org.globsframework.core.utils.exceptions.GlobsException;

public class ParseError extends GlobsException {
    public ParseError(String message) {
        super(message);
    }
}
