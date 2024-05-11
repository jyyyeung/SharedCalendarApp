import com.example.sharedcalendar.Color
import com.example.sharedcalendar.ColorList
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorListTest {

    private val colorList = ColorList()

    @Test
    fun `default color is Tomato`() {
        val expectedColor = colorList.colors()[0]
        assertEquals(expectedColor.code, colorList.default.code)
    }

    @Test
    fun `colorPosition returns correct position for existing color`() {
        val color = colorList.colors()[1]
        val expectedPosition = 1
        assertEquals(expectedPosition, colorList.colorPosition(color))
    }

    @Test
    fun `colorPosition returns zero for non-existing color`() {
        val color = Color("NonExisting", "#FFFFFF")
        val expectedPosition = 0
        assertEquals(expectedPosition, colorList.colorPosition(color))
    }

//    @Test
//    fun `colors returns correct list of colors`() {
//        val expectedColors = ColorList().colors()
//        expectedColors.forEach() {
//            assertEquals(it.code, colorList.colors()[colorList.colorPosition(it)].code)
//            assertEquals(it.name, colorList.colors()[colorList.colorPosition(it)].name)
//        }
//    }
}