This library convert a command line arguments to a Glob.
For exemple, for the command line :

```
    "--value", "toto", "titi", "--otherName", "tata", "--value", "A,B,C", "--name", "a name"
```

Using the GlobType :

```    
    public static class Opt1 {
        public static GlobType TYPE;

        public static StringField NAME;

        @FieldNameAnnotation("value")
        @ArraySeparator_(',')
        public static StringArrayField MULTIVALUES;

        @DefaultInteger(123)
        public static IntegerField VAL;
        ...
```

Parsed with

```
        Glob opt = ParseCommandLine.parse(Opt1.TYPE, args, true);
```

Will give the glob :

```
        Assert.assertEquals("a name", opt.get(Opt1.NAME));
        Assert.assertArrayEquals(new String[]{"toto", "titi", "A", "B", "C"}, opt.get(Opt1.MULTIVALUES));
        Assert.assertEquals(123, opt.get(Opt1.VAL).intValue());

```
