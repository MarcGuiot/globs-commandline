package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.DefaultInteger;
import org.globsframework.metamodel.annotations.FieldNameAnnotation;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.metamodel.fields.StringArrayField;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.globsframework.utils.Strings;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class ParseCommandLineTest {

    @Test
    public void name() {
        Glob opt = ParseCommandLine.parse(Opt1.TYPE, new String[]{"--name", "a name"});
        Assert.assertEquals("a name", opt.get(Opt1.NAME));
        Assert.assertEquals(123, opt.get(Opt1.VAL).intValue());

        String[] args = {"--name", "a name", "--val", "321"};
        opt = ParseCommandLine.parse(Opt1.TYPE, args);
        Assert.assertEquals("a name", opt.get(Opt1.NAME));
        Assert.assertEquals(321, opt.get(Opt1.VAL).intValue());
        Assert.assertArrayEquals(args, ParseCommandLine.toArgs(opt));

        opt = ParseCommandLine.parse(Opt1.TYPE, new String[]{"--value", "1", "--value", "2"});
        String[] strings = opt.getOrEmpty(Opt1.MULTIVALUES);
        Assert.assertEquals(2, strings.length);
        Assert.assertEquals("1,2", String.join(",", strings));
    }

    public static class Opt1 {
        public static GlobType TYPE;

        public static StringField NAME;

        @FieldNameAnnotation("value")
        public static StringArrayField MULTIVALUES;

        @DefaultInteger(123)
        public static IntegerField VAL;

        static {
            GlobTypeLoaderFactory.create(Opt1.class).load();
        }
    }
}