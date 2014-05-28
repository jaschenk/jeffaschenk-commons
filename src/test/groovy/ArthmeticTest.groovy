

import org.junit.Test
import static org.junit.Assert.assertEquals

class ArithmeticTest {
  final shouldFail = new groovy.util.GroovyTestCase().&shouldFail

  @Test void additionIsWorking() {
    assertEquals 4, 2 + 2
  }

  @Test void divideByZero() {
    shouldFail(ArithmeticException) {
      println 1 / 0
    }
  }

}
