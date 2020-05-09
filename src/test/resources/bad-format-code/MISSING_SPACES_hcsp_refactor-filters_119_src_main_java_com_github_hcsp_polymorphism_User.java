package com.github.hcsp.polymorphism;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class User {
    /** 用户ID，数据库主键，全局唯一 */
    private final Integer id;

    /** 用户名 */
    private final String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    // 过滤ID为偶数的用户
    /*public static List<User> filterUsersWithEvenId(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.id % 2 == 0) {
                results.add(user);
            }
        }
        return results;
    }*/

    public static class filterUsersWithEvenId implements Predicate<User>{

        @Override
        public boolean test(User user) {
            return (user.getId())==0;
        }
    }


    // 过滤姓张的用户
    /*public static List<User> filterZhangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("张")) {
                results.add(user);
            }
        }
        return results;
    }*/

    public static class filterZhangUsers implements Predicate<User>{

        @Override
        public boolean test(User user) {
            return (user.getName().startsWith("张"));
        }
    }


    // 过滤姓王的用户
    /*public static List<User> filterWangUsers(List<User> users) {
        List<User> results = new ArrayList<>();
        for (User user : users) {
            if (user.name.startsWith("王")) {
                results.add(user);
            }
        }
        return results;
    }*/

    public static class filterWangUsers implements Predicate<User>{

        @Override
        public boolean test(User user) {
            return (user.getName().startsWith("王"));
        }
    }


    // 你可以发现，在上面三个函数中包含大量的重复代码。
    // 请尝试通过Predicate接口将上述代码抽取成一个公用的过滤器函数
    // 并简化上面三个函数
    public static List<User> filter(List<User> users, Predicate<User> predicate) {
        List<User> list = new ArrayList<>();
        for (User user : users) {
            if(predicate.test(user)){
                list.add(user);
            }
        }
        return list;
    }
}
