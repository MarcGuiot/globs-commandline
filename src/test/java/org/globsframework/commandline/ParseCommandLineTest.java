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

import java.util.ArrayList;
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

    @Test
    public void multipleOptions() {
        ArrayList<String> args = new ArrayList<>(Arrays.asList("--name", "a name", "--otherName", "titi"));
        Glob opt = ParseCommandLine.parse(Opt1.TYPE, args, true);
        Assert.assertEquals(opt.get(Opt1.NAME), "a name");
        Glob opt2 = ParseCommandLine.parse(Opt2.TYPE, args, false);
        Assert.assertEquals(opt2.get(Opt2.otherName), "titi");
    }

    @Test
    public void withUnknownParam() {
        ArrayList<String> args = new ArrayList<>(Arrays.asList("--name", "a name", "--otherName", "titi"));
        try {
            Glob opt = ParseCommandLine.parse(Opt1.TYPE, args, false);
            Assert.fail("not ignored");
        } catch (Exception exception) {
            Assert.assertTrue(exception instanceof ParseError);
        }
    }

    public static class Opt1 {
        public static GlobType TYPE;

        public static StringField NAME;

        @FieldNameAnnotation("value")
        public static StringArrayField MULTIVALUES;

        @DefaultInteger(123)
        public static IntegerField VAL;

        static {
            GlobTypeLoaderFactory.create(Opt1.class, true).load();
        }
    }

    public static class Opt2 {
        public static GlobType TYPE;

        public static StringField otherName;

        static {
            GlobTypeLoaderFactory.create(Opt2.class, true).load();
        }
    }
}