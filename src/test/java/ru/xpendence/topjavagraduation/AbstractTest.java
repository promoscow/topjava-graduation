package ru.xpendence.topjavagraduation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class AbstractTest {

    @Autowired
    protected DataBuilder dataBuilder;
}
