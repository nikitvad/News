package com.example;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {
    private static final Class<?>[] classes = new Class[] {
            Source.class
    };
    public static void main(String[] args) throws Exception {
        writeConfigFile("ormlite_config.txt", classes);
    }
}
