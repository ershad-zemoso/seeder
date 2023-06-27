package com.zemoso.seeder.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CashKickStatusTest {

    @Test
    void testAllValues() {

        Assertions.assertEquals("Pending", CashKickStatus.PENDING.getText());
        Assertions.assertEquals("Approved", CashKickStatus.APPROVED.getText());
    }
}
