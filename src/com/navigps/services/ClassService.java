package com.navigps.services;


public class ClassService {
    private static final String REGISTER = " registered";
    private static final String UNREGISTER = " unregistered";
    public static String getName(Class cClass)
    {
        return cClass.getName();
    }
    public static String getRegisterText(Class cClass)
    {
        return cClass.getName() + REGISTER;
    }
    public static String getUnregisterName(Class cClass)
    {
        return cClass.getName() + UNREGISTER;
    }
}
