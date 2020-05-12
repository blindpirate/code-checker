package com.github.hcsp.polymorphism;

import java.util.Arrays;
import java.util.List;

public class World {
    // 现在有若干种对象，请尝试使用接口和抽象类将它们建造成类型体系
    // 以最大限度的复用、简化代码
    public static List<Object> objects =
            Arrays.asList(new 麻雀(), new 喜鹊(), new 蝴蝶(), new 飞机(), new 救护车(), new 猫(), new 狗());

    // 在建造成类型体系后，请尝试化简这个啰嗦的方法，体会多态带来的好处
    public static void 会飞的东西飞() {
        for (Object obj : objects) {
            if (obj instanceof 会飞的东西) {
                ((会飞的东西) obj).飞();
            }
        }
    }

    // 在建造成类型体系后，请尝试化简这个啰嗦的方法，体会多态带来的好处
    public static void 会叫的东西叫() {
        for (Object obj : objects) {
            if (obj instanceof 会叫的东西) {
                ((会叫的东西) obj).叫();
            }
        }
    }

    static class Bird extends 动物 implements 会飞的东西, 会叫的东西 {
        @Override
        public void 飞() {
            System.out.println("鸟儿飞");
        }

        @Override
        public void 叫() {
            System.out.println("叽叽喳喳");
        }
    }

    // 在建造成类型体系后，请尝试化简这个啰嗦的方法，体会多态带来的好处
    public static void 动物都能新陈代谢() {
        for (Object obj : objects) {
            if (obj instanceof 动物) {
                ((动物) obj).新陈代谢();
            }
        }
    }

    static class 麻雀 extends Bird {
    }

    static class 喜鹊 extends Bird {
    }

    static class 蝴蝶 extends 动物 implements 会飞的东西 {
        @Override
        public void 飞() {
            System.out.println("蝴蝶飞");
        }
    }

    static class 飞机 implements 会飞的东西 {
        @Override
        public void 飞() {
            System.out.println("飞机飞");
        }
    }

    static class 救护车 implements 会叫的东西 {
        @Override
        public void 叫() {
            System.out.println("哇呜哇呜");
        }
    }

    static class 猫 extends 动物 implements 会叫的东西 {
        @Override
        public void 叫() {
            System.out.println("喵喵喵");
        }
    }

    static class 狗 extends 动物 implements 会叫的东西 {
        @Override
        public void 叫() {
            System.out.println("汪汪汪");
        }
    }

    static class 动物 {
        public void 新陈代谢() {
            System.out.println("新陈代谢");
        }
    }

    interface 会飞的东西 {
        void 飞();
    }

    interface 会叫的东西 {
        void 叫();
    }
}
