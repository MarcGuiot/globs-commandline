package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.fields.StringField;
import org.globsframework.model.Glob;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class ParseEnvironmentTest {

    @Test
    public void name() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPT1_NAME", "a name");
        Glob opt = ParseEnvironment.parse("PREFIX", Opt1.TYPE, env);
        Assert.assertEquals("a name", opt.get(Opt1.NAME));
    }

    @Test
    public void multipleOptions() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTMULTI_NAME", "a name");
        env.put("PREFIX_OPTMULTI_ANOTHER_NAME", "another name");

        Glob opt = ParseEnvironment.parse("PREFIX", OptMulti.TYPE, env);
        Assert.assertEquals("a name", opt.get(OptMulti.NAME) );
        Assert.assertEquals("another name", opt.get(OptMulti.ANOTHER_NAME));
    }

    public static class Opt1 {
        public static GlobType TYPE;

        public static StringField NAME;

        static {
            GlobTypeLoaderFactory.create(Opt1.class, true).load();
        }
    }

    public static class OptMulti {
        public static GlobType TYPE;

        public static StringField NAME;
        public static StringField ANOTHER_NAME;

        static {
            GlobTypeLoaderFactory.create(OptMulti.class, true).load();
        }
    }

}