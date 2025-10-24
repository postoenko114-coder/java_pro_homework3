package org.example;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

class Z {
    int length;
    int height;

    Z(int length, int height) {
        this.length = length;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}


class Person {
    int name = 111;
    String surname = "Hello";
    int[] numbers = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
    Z z = new Z(1, 2);
}

public class App2 {
    public static void main(String[] args) throws IllegalAccessException {
        Person person = new Person();

        Class<?> cls = Person.class;
        StringBuilder sb = new StringBuilder();

        Field[] fields = cls.getDeclaredFields();

        sb.append("{");

        for (Field field : fields) {
            field.setAccessible(true);
            Class<?> type = field.getType();

            boolean isObject = !type.isPrimitive();
            boolean isCustomObject =
                    isObject &&
                            !type.getPackageName().startsWith("java.") &&
                            !type.isArray();
            if (field.getType().isArray()) {

                sb.append('"')
                        .append(field.getName())
                        .append('"')
                        .append(": ")
                        .append('[');
                Object value = field.get(person);
                int length = Array.getLength(value);
                for (int i = 0; i < length; i++) {
                    Object element = Array.get(value, i);
                    sb.append('"')
                            .append(element)
                            .append('"')
                            .append(",");

                }
                if (sb.length() > 1) {
                    sb.setLength(sb.length() - 1);
                }
                sb.append(']');
            } else if (isCustomObject) {
                Object nestedObj = field.get(person);

                Class<?> cls1 = nestedObj.getClass();

                sb.append('"')
                        .append(cls1.getName())
                        .append('"')
                        .append(": ")
                        .append('{');

                Field[] fields1 = cls1.getDeclaredFields();
                for (Field field1 : fields1) {
                    field1.setAccessible(true);

                    if (field1.getType().isArray()) {

                        sb.append('"')
                                .append(field1.getName())
                                .append('"')
                                .append(": ")
                                .append('[');
                        Object value = field.get(person);
                        int length = Array.getLength(value);
                        for (int i = 0; i < length; i++) {
                            Object element = Array.get(value, i);
                            sb.append('"')
                                    .append(element)
                                    .append('"')
                                    .append(",");

                        }
                        if (sb.length() > 1) {
                            sb.setLength(sb.length() - 1);
                        }
                        sb.append(']');
                    } else {
                        Object value = field1.get(nestedObj);

                        sb.append('"')
                                .append(field1.getName())
                                .append('"')
                                .append(": ")
                                .append('"')
                                .append(value)
                                .append('"').append(',');

                    }
                }
                if (sb.length() > 1) {
                    sb.setLength(sb.length() - 1);
                }
                    sb.append('}');
                }else{

                    Object value = field.get(person);

                    sb.append('"')
                            .append(field.getName())
                            .append('"')
                            .append(": ")
                            .append('"')
                            .append(value)
                            .append('"').append(',');
                }
            sb.append(',');



        }
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 1);
        }

        sb.append("}");

        System.out.println(sb.toString());
    }

}
