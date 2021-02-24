package ru.rcaltd.alfaTask1.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringValidator {

    public boolean validate(String currencyCode) {

        // Reject, if has 3 symbols, but not (letters, digits, _)
        Pattern patternLD = Pattern.compile("\\w{3}");
        Matcher matcherLD = patternLD.matcher(currencyCode);

        // Reject, if found digit
        Pattern patternLO = Pattern.compile("\\D{3}");
        Matcher matcherLO = patternLO.matcher(currencyCode);

        return matcherLD.matches() && matcherLO.matches();
    }
}
