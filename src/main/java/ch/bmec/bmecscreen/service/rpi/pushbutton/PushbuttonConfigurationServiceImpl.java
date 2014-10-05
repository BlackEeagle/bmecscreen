/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.bmec.bmecscreen.service.rpi.pushbutton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 *
 * @author Thom
 */
@Component
public class PushbuttonConfigurationServiceImpl implements PushbuttonConfigurationService {

    private final Logger log = LoggerFactory.getLogger(PushbuttonConfigurationServiceImpl.class);

    @Override
    public PushbuttonConfiguration parseMessageFromRPi(String message) {

        PushbuttonConfiguration config = null;

        try {
            Pattern pattern = Pattern.compile("BMECScreen2-pushbuttonPushed-((0|1){6})");
            Matcher matcher = pattern.matcher(message);

            if (matcher.matches()) {

                String binaryConfig = matcher.group(1);
                List<Boolean> boolConfig = new ArrayList<>(6);

                Map<Character, Boolean> binToBooleanMap = new HashMap<>();
                binToBooleanMap.put('0', false);
                binToBooleanMap.put('1', true);

                for (int i = 0; i < 6; i++) {
                    char configChar = binaryConfig.charAt(i);

                    Boolean bool = binToBooleanMap.get(configChar);

                    if (bool != null) {
                        boolConfig.add(bool);
                    } else {
                        throw new PushbuttonParseException("invalid character (" + configChar + ")!");
                    }
                }

                config = new PushbuttonConfiguration(boolConfig.get(0), boolConfig.get(1), boolConfig.get(2), boolConfig.get(3), boolConfig.get(4), boolConfig.get(5));

            } else {
                throw new PushbuttonParseException("invalid message (" + message + ")!");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return config;
    }

    @Override
    public String createMessageForRPi(PushbuttonConfiguration configuration) {

        StringBuilder message = new StringBuilder();

        message.append("BMECScreen1-pushbuttonLight-");

        addToString(message, configuration.isScreen1());
        addToString(message, configuration.isScreen2());
        addToString(message, configuration.isUp());
        addToString(message, configuration.isDown());
        addToString(message, configuration.isLeft());
        addToString(message, configuration.isRight());

        return message.toString();
    }

    private void addToString(StringBuilder message, Boolean bool) {
        if (bool) {
            message.append("1");
        } else {
            message.append("0");
        }
    }

}
