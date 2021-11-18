package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.Targets;
import org.globsframework.metamodel.fields.GlobUnionField;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class MultiLevelCommandLineTest {


    @Test
    public void multiLevelTest() {
        Glob options1 = ParseCommandLine.parse(Options.TYPE, new ArrayList<>(Arrays.asList("cmd2", "--arg1", "v1", "--arg2", "v2", "--name", "ZZZ")), true);
        Assert.assertEquals(Cmd2.TYPE, options1.get(Options.cmd).getType());
        Assert.assertEquals("ZZZ", options1.get(Options.name));
        Glob options2 = ParseCommandLine.parse(Options.TYPE, new ArrayList<>(Arrays.asList("--name", "ZZZ", "cmd1", "--arg1", "v1", "--arg2", "v2")), true);
        Assert.assertEquals(Cmd1.TYPE, options2.get(Options.cmd).getType());
        Assert.assertEquals("v1", options2.get(Options.cmd).get(Cmd1.arg1));
        Assert.assertEquals("ZZZ", options2.get(Options.name));
    }

    public static class Options {
        public static GlobType TYPE;

        public static StringField name;

        @Targets({Cmd1.class, Cmd2.class})
        public static GlobUnionField cmd;

        static {
            GlobTypeLoaderFactory.create(Options.class).load();
        }
    }

    public static class Cmd1 {
        public static GlobType TYPE;

        public static StringField arg1;
        public static StringField arg2;

        static {
            GlobTypeLoaderFactory.create(Cmd1.class, "cmd1").load();
        }
    }

    public static class Cmd2 {
        public static GlobType TYPE;
        public static StringField arg1;
        public static StringField arg2;

        static {
            GlobTypeLoaderFactory.create(Cmd2.class, "cmd2").load();
        }
    }
}
