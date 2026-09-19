package ru.xpendence.topjavagraduation

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
abstract class AbstractTest {

    @Autowired
    protected lateinit var dataBuilder: DataBuilder
}
