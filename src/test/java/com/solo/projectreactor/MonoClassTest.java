package com.solo.projectreactor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MonoClassTest {

    private MonoClass monoClass;

    @BeforeEach
    void setUp() {
        monoClass = new MonoClass();
    }

    @Test
    void test2() {
        monoClass.getMonoFromSupplier().subscribe(res -> System.out.println(res));
    }

    @Test
    void test1() {
     monoClass.getMonoOf().subscribe((res) ->
         System.out.println(res),
         err-> System.out.println(err.getMessage())  ,
             () -> System.out.println("completed")

     );


    }
}