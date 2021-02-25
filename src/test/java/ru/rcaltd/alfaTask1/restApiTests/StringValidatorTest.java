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

package ru.rcaltd.alfaTask1.restApiTests;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rcaltd.alfaTask1.utils.StringValidator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@SpringBootTest
public class StringValidatorTest {

    /*  Return TRUE, if Currency Code = USD */
    @Test
    public void givenCorrectCurrencyCodeUSD_whenStringValidatorRetrieve_thenTrueReceived() {

        // Given
        String correctCurrencyCode = "USD";

        // When
        boolean answer = StringValidator.validate(correctCurrencyCode);
        // Then
        assertThat(
                answer,
                equalTo(true));
    }

    /*  Return TRUE, if Currency Code = GBP */
    @Test
    public void givenCorrectCurrencyCodeGBP_whenStringValidatorRetrieve_thenTrueReceived() {

        // Given
        String correctCurrencyCode = "gbp";

        // When
        boolean answer = StringValidator.validate(correctCurrencyCode);
        // Then
        assertThat(
                answer,
                equalTo(true));
    }

    /*  Return FALSE, if Currency Code 4 symbols */
    @Test
    public void givenIncorrectCurrencyCode4Symbols_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "USDT";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code 5 symbols */
    @Test
    public void givenIncorrectCurrencyCode5Symbols_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "USDTR";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code contains number */
    @Test
    public void givenIncorrectCurrencyCodeNumber_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "1";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code contains 3 numbers */
    @Test
    public void givenIncorrectCurrencyCode3Numbers_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "123";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code contains 5 numbers */
    @Test
    public void givenIncorrectCurrencyCode5Numbers_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "12233";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code contains "_" */
    @Test
    public void givenIncorrectCurrencyCode__whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "_";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }

    /*  Return FALSE, if Currency Code contains "#" */
    @Test
    public void givenIncorrectCurrencyCodeDies_whenStringValidatorRetrieve_thenFalseReceived() {

        // Given
        String incorrectCurrencyCode = "#";

        // When
        boolean isItCorrectAnswer = StringValidator.validate(incorrectCurrencyCode);
        // Then
        assertThat(
                isItCorrectAnswer,
                equalTo(false));
    }
}
