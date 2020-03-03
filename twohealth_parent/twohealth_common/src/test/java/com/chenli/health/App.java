package com.chenli.health;

import com.aliyuncs.exceptions.ClientException;
import com.chenli.health.app.SMSUtils;
import org.junit.Test;

public class App {

    @Test
    public void testapp() throws ClientException {

        SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,"18689482438","1234");
    }
}
