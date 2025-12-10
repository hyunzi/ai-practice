package com.example.devdocs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "openai.api-key=dummy-key",
        "chroma.enabled=false"
})
class DevDocsApplicationTests {

    @Test
    void contextLoads() {
    }
}
