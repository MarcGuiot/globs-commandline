package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.fields.StringArrayField;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.junit.Assert;
import org.junit.Test;

public class UnNamedTest {

    @Test
    public void name() {
        Glob option = ParseCommandLine.parse(Arg.TYPE, new String[]{"A1", "A2", "A3"});
        Assert.assertNotNull(option);
        Assert.assertEquals("A1", option.get(Arg.firstArg));
        Assert.assertArrayEquals(new String[]{"A2", "A3"}, option.get(Arg.otherArgs));
    }

    public static class Arg {
        public static GlobType TYPE;

        @UnNamed_
        public static StringField firstArg;

        @UnNamed_
        public static StringArrayField otherArgs;

        static {
            GlobTypeLoaderFactory.create(Arg.class).load();
        }
    }
}
