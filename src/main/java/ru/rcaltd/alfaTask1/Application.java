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

package ru.rcaltd.alfaTask1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
