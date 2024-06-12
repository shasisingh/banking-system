package com.nightcrowler.spring.banking;

import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class BankOperationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BankOperationApplication.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .web(WebApplicationType.SERVLET)
                .headless(true)
                .registerShutdownHook(true)
                .run(args);
    }


}
