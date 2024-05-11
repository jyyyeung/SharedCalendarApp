import android.content.Context
import android.graphics.Color.parseColor
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.Color
import com.example.sharedcalendar.SpinnerAdapter
import io.mockk.mockk
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import com.example.sharedcalendar.R
import io.mockk.mockkStatic
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

@RunWith(RobolectricTestRunner::class)
class SpinnerAdapterTest : BaseTest() {
    private lateinit var context: Context

    override fun extendedSetup() {
        context = mockApplicationContext
        mockkStatic(Color::class)
    }

    // Adapter correctly inflates view with color name and code
    @Test
    fun test_inflateViewWithColorNameAndCode() {
        // Arrange
        val items = listOf(Color("Red", "#FF0000"))
        val adapter = SpinnerAdapter(context, items)

        // Act
        val view = adapter.getView(0, null, mockk())

        // Assert
        assertNotNull(view)
        val colorText = view.findViewById<TextView>(R.id.colorText)
        assertNotNull(colorText)
        assertEquals("Red", colorText.text.toString())
        val colorView = view.findViewById<View>(R.id.colorView)
        assertNotNull(colorView)
        assertEquals(parseColor("#FF0000"), colorView.backgroundTintList?.defaultColor)
    }


    // Adapter correctly sets background color of colorView based on color code
    @Test
    fun test_setBackgroundColorBasedOnColorCode() {
        // Arrange
        val items = listOf(Color("Red", "#FF0000"))
        val adapter = SpinnerAdapter(context, items)
        val view = adapter.getView(0, null, mockk())

        // Act
        val colorView = view.findViewById<View>(R.id.colorView)

        // Assert
        assertNotNull(colorView)
        assertEquals(parseColor("#FF0000"), colorView.backgroundTintList?.defaultColor)
    }

    // Adapter correctly sets text of colorText based on color name
    @Test
    fun test_setTextBasedOnColorName() {
        // Arrange
        val items = listOf(Color("Red", "#FF0000"))
        val adapter = spyk(SpinnerAdapter(context, items))
        val view = adapter.getView(0, null, mockk())

        // Act
        val colorText = view.findViewById<TextView>(R.id.colorText)

        // Assert
        assertNotNull(colorText)
        assertEquals("Red", colorText.text.toString())
    }

    // Adapter correctly returns view in getView and getDropDownView
    @Test
    fun test_returnViewInGetViewAndGetDropDownView() {
        // Arrange
        val items = listOf(Color("Red", "#FF0000"))
        val adapter = SpinnerAdapter(context, items)

        // Act
        val view1 = adapter.getView(0, null, mockk())
        val view2 = adapter.getDropDownView(0, null, mockk())

        // Assert
        assertNotNull(view1)
        assertNotNull(view2)
    }

    // Adapter correctly handles items list with duplicate colors
    @Test
    fun test_handleItemsListWithDuplicateColors() {
        // Arrange
        val items = listOf(Color("Red", "#FF0000"), Color("Red", "#FF0000"))
        val adapter = SpinnerAdapter(context, items)

        // Act
        val view = adapter.getView(0, null, mockk())

        // Assert
        assertNotNull(view)
    }

}
