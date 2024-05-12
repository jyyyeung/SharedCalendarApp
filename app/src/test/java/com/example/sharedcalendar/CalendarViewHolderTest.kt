import android.os.Looper
import android.view.View
import android.widget.TextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.sharedcalendar.BaseTest
import com.example.sharedcalendar.CalendarAdapter
import com.example.sharedcalendar.CalendarViewHolder
import com.example.sharedcalendar.R
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows

@RunWith(RobolectricTestRunner::class)
class CalendarViewHolderTest : BaseTest() {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var mockView: View

    @RelaxedMockK
    private lateinit var mockTextView: TextView

    @RelaxedMockK
    private lateinit var mockOnItemClickListener: CalendarAdapter.OnItemListener

    private lateinit var viewHolder: CalendarViewHolder

    override fun extendedSetup() {
        MockKAnnotations.init(this, relaxed = true)
        every { mockView.findViewById<TextView>(R.id.dayOfMonth) } returns mockTextView
        every { mockTextView.text } returns "Some Text"
        viewHolder = CalendarViewHolder(mockView, mockOnItemClickListener)

    }

    @Test
    fun `when DayOfMonth TextView is clicked then onItemClick should be called`() {
        // Arrange
        Shadows.shadowOf(Looper.getMainLooper()).idle()

        assert(viewHolder.dayOfMonth.text == "Some Text")
        assert(viewHolder.dayOfMonth == mockTextView)
        assert(viewHolder.itemView == mockView)
        mockView.performClick()

        // Act
//        mockTextView.performClick().apply {

        // Assert
        verify { mockOnItemClickListener.onItemClick(any(), "Some Text") }
//        }
    }
}
