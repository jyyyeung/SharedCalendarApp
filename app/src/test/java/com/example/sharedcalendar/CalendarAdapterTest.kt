import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.CalendarAdapter
import com.example.sharedcalendar.CalendarViewHolder
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowColor
import java.util.ArrayList
import com.example.sharedcalendar.R
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.ArgumentMatchers.anyInt

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29]) // Use the API level you're targeting
class CalendarAdapterTest : BaseTest() {

    @MockK
    private lateinit var mockOnItemListener: CalendarAdapter.OnItemListener

    private lateinit var adapter: CalendarAdapter

    private lateinit var appContext: Context

    override fun extendedSetup() {
        appContext = buildActivity(Activity::class.java).get().applicationContext
        val daysOfMonth = ArrayList<String>()
        daysOfMonth.add("1")
        daysOfMonth.add("2")
        daysOfMonth.add("3")
        mockOnItemListener = mockk(relaxed = true)
        adapter = CalendarAdapter(daysOfMonth, mockOnItemListener)

    }


    @Test
    fun onBindViewHolder_ShouldSetDayOfMonthText() {
        val mockView = mockk<View>(relaxed = true)
        every { mockView.findViewById<TextView>(any<Int>()) } returns mockk(relaxed = true)
        val mockViewHolder = CalendarViewHolder(mockView, mockOnItemListener)
        // Arrange
//        val mockViewHolder = CalendarViewHolder(View(appContext), mockOnItemListener)

        // Act
        adapter.onBindViewHolder(mockViewHolder, 0)

        // Assert
        verify { mockViewHolder.dayOfMonth.text = "1" }
    }

    @Test
    fun getItemCount_ShouldReturnSizeOfDaysOfMonth() {
        // Act
        val itemCount = adapter.getItemCount()

        // Assert
        assertEquals(3, itemCount)
    }
}
