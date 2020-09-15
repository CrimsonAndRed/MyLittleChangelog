package my.little.changelog

import io.mockk.unmockkAll
import org.junit.jupiter.api.BeforeAll

abstract class BaseTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun unmockPreviousMocks() {
            unmockkAll()
        }
    }
}
