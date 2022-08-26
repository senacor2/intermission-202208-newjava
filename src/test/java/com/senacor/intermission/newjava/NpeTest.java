package com.senacor.intermission.newjava;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;

public class NpeTest {

    @Test
    public void unboxing() {
        Integer index = null;
        Throwable npe = catchThrowable(() -> {
            int i = index;
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot invoke "java.lang.Integer.intValue()" because "index" is null""");

        npe = catchThrowable(() -> {
            var b = new char[]{'a', 'b', 'c'}[index];
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot invoke "java.lang.Integer.intValue()" because "index" is null""");
    }

    @Test
    public void nullReference() {
        Person person = null;
        Throwable npe = catchThrowable(() -> {
            person.name = "Fun Guy";
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot assign field "name" because "person" is null""");

        npe = catchThrowable(() -> {
            String name = person.name;
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot read field "name" because "person" is null""");

        npe = catchThrowable(() -> person.getName());
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot invoke "com.senacor.intermission.newjava.NpeTest$Person.getName()" because "person" is null""");
    }

    @Test
    public void arrayOperations() {
        char[] chars = null;

        Throwable npe = catchThrowable(() -> {
            chars[2] = 'b';
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot store to char array because "chars" is null""");

        npe = catchThrowable(() -> {
            var b = chars[2];
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot load from char array because "chars" is null""");

        npe = catchThrowable(() -> {
            int length = chars.length;
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot read the array length because "chars" is null""");
    }

    @Test
    public void throwExceptions() {
        Exception ex = null;

        Throwable npe = catchThrowable(() -> {
            throw ex;
        });
        assertThat(npe).isInstanceOf(NullPointerException.class);
        assertThat(npe).hasMessage("""
            Cannot throw exception because "ex" is null""");
    }

    private static class Person {
        private String name;

        public Person(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
