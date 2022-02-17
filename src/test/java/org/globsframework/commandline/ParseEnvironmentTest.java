package org.globsframework.commandline;

import org.globsframework.metamodel.GlobType;
import org.globsframework.metamodel.GlobTypeLoaderFactory;
import org.globsframework.metamodel.annotations.DefaultString;
import org.globsframework.metamodel.annotations.FieldNameAnnotation;
import org.globsframework.metamodel.fields.IntegerField;
import org.globsframework.metamodel.fields.StringArrayField;
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

    @Test
    public void testForDefaultValues() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTDEFAULT_NAME", "a name");

        Glob opt = ParseEnvironment.parse("PREFIX", OptDefault.TYPE, env);
        Assert.assertEquals("a name", opt.get(OptDefault.NAME) );
        Assert.assertEquals("Marc", opt.get(OptDefault.ANOTHER_NAME));
    }

    @Test
    public void testWithIntValues() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTWITHINTS_NAME", "a name");
        env.put("PREFIX_OPTWITHINTS_INT_FIELD", "42");

        Glob opt = ParseEnvironment.parse("PREFIX", OptWithInts.TYPE, env);
        Assert.assertEquals(42, opt.get(OptWithInts.INT_FIELD).intValue());
    }

    @Test(expected = EnvironmentVariableNotSetException.class)
    public void multipleOptionsExceptionTest() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTMULTI_ANOTHER_NAME", "another name");

        Glob opt = ParseEnvironment.parse("PREFIX", OptMulti.TYPE, env);

    }

    @Test
    public void arrayFieldTest() throws Exception{

        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTWITHSTRINGARRAY_ARRAY_FIELD", "another,name");

        Glob opt = ParseEnvironment.parse("PREFIX", OptWithStringArray.TYPE, env);

        Assert.assertEquals(2, opt.get(OptWithStringArray.arrayField).length);
        Assert.assertEquals("another", opt.get(OptWithStringArray.arrayField)[0]);
        Assert.assertEquals("name", opt.get(OptWithStringArray.arrayField)[1]);

    }

    @Test
    public void fieldWithPoint() throws Exception{
        Map<String, String> env = new HashMap<>();
        env.put("PREFIX_OPTWITHPOINT_POINT_FIELD", "test");

        Glob opt = ParseEnvironment.parse("PREFIX", OptWithPoint.TYPE, env);

        Assert.assertEquals("test", opt.get(OptWithPoint.withPoint));
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

        @Mandatory_
        public static StringField NAME;
        public static StringField ANOTHER_NAME;

        static {
            GlobTypeLoaderFactory.create(OptMulti.class, true).load();
        }
    }

    public static class OptDefault {
        public static GlobType TYPE;

        public static StringField NAME;
        @DefaultString("Marc")
        public static StringField ANOTHER_NAME;

        static {
            GlobTypeLoaderFactory.create(OptDefault.class, true).load();
        }
    }

    public static class OptWithInts {
        public static GlobType TYPE;

        public static StringField NAME;
        public static IntegerField INT_FIELD;

        static {
            GlobTypeLoaderFactory.create(OptWithInts.class, true).load();
        }
    }

    public static class OptWithStringArray {
        public static GlobType TYPE;

        public static StringField NAME;
        public static StringArrayField arrayField;

        static {
            GlobTypeLoaderFactory.create(OptWithStringArray.class, true).load();
        }
    }

    public static class OptWithPoint {
        public static GlobType TYPE;

        public static StringField NAME;
        @FieldNameAnnotation("point.field")
        public static StringField withPoint;

        static {
            GlobTypeLoaderFactory.create(OptWithPoint.class, true).load();
        }
    }

}