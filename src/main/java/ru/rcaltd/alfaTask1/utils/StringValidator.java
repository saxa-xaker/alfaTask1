/*
 * *
 *  * Copyright 2009-2021 The Rcaltd
 *  *
 *  * Licensed under theGNU LESSER GENERAL PUBLIC LICENSE Version 2.1 (the "License");
 *  * you may not use this file except
 *  * in compliance with the License. You may obtain a copy of the License at
 *  *
 *  * https://www.gnu.org/licenses/old-licenses/lgpl-2.1.ru.html
 *  *
 *  * Unless required by applicable law or agreed to in writing, software distributed under the License
 *  * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  * or implied. See the License for the specific language governing permissions and limitations under
 *  * the License.
 *
 */

package ru.rcaltd.alfaTask1.utils;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class StringValidator {

    public static boolean validate(String currencyCode) {

        // Reject, if has 3 symbols, but not (letters, digits, _)
        Pattern patternLD = Pattern.compile("\\w{3}");
        Matcher matcherLD = patternLD.matcher(currencyCode);

        // Reject, if found digit
        Pattern patternLO = Pattern.compile("\\D{3}");
        Matcher matcherLO = patternLO.matcher(currencyCode);

        return matcherLD.matches() && matcherLO.matches();
    }
}
