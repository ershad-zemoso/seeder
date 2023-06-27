package com.zemoso.seeder.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionTest {

    @Test
    void testNotFoundException() {
        NotFoundException ex1 = new NotFoundException("Test single arg constructor");
        NotFoundException ex2 = new NotFoundException("Test two arg constructor", new RuntimeException());

        Assertions.assertEquals("Test single arg constructor", ex1.getMessage());
        Assertions.assertEquals("Test two arg constructor", ex2.getMessage());
    }
}
