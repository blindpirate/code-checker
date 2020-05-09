package com.github.hcsp.polymorphism;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;

public class User implements Comparable<User> {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User person = (User) o;

        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    /** 老板说让我按照用户名排序 */
    @Override
    public int compareTo(User o) {
        if (id.compareTo(o.getId()) == 0)
            return 0;
        else if (this.getName().compareTo(o.getName()) == 0)
            return  getId().compareTo(o.getId());
        else if (this.getName().compareTo(o.getName()) > 0)
            return 1;
        else
            return -1;
    }

    public static void main(String[] args) {
        List<User> users =
                Arrays.asList(
                        new User(100, "b"),
                        new User(10, "z"),
                        new User(1, "a"),
                        new User(2000, "a"));
        TreeSet<User> treeSet = new TreeSet<>(users);
        // 为什么这里的输出是3？试着修复其中的bug
        System.out.println(treeSet.size());
    }
}
