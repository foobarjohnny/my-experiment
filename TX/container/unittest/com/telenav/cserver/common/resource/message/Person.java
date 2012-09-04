package com.telenav.cserver.common.resource.message;

import java.util.ArrayList;
import java.util.List;

public class Person
{
    private List names = new ArrayList();
    
    private int age;
    
    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public List getNames()
    {
        return names;
    }

    public void setNames(List names)
    {
        this.names = names;
    }
}
