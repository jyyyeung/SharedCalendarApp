package com.example.sharedcalendar.feature.editcalendar

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.sharedcalendar.FirebaseViewModel
import com.example.sharedcalendar.models.Calendar

//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    EditCalendarRoute(onBackClicked = {}, navigateToCalendarConfirm = {})
//}

@Composable
fun EditCalendarRoute(
    modifier: Modifier = Modifier, calendar: Calendar, firebaseViewModel: FirebaseViewModel,
    onBackButtonClicked: () -> Unit

) {
    EditCalendarScreen(modifier, calendar, firebaseViewModel, onBackButtonClicked)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCalendarScreen(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    firebaseViewModel: FirebaseViewModel,
    onBackButtonClicked: () -> Unit
) {

    val context = LocalContext.current

    fun editCalendar(editedFields: HashMap<String, Any>) {
        for (field in editedFields) {
            when (field.key) {
                "name" -> if ((editedFields["name"]!! as String).isBlank()) {
                    editedFields.remove("name")
                }

                "description" -> if ((editedFields["description"] as String).isBlank()) {
                    editedFields.remove("description")
                }
            }
        }
        // Modify values in db
        calendar.id?.let {
            firebaseViewModel.editCalendar(it, editedFields)
            // Go back
            onBackButtonClicked()
        }
        Log.i("Edit Cal", "${calendar.id} $editedFields ")

    }

    val editedFields = hashMapOf<String, Any>()

    var calendarName by remember { mutableStateOf(calendar.name) }
    var calendarDescription by remember { mutableStateOf(calendar.description) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Edit Calendar Information", style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = calendarName,
            readOnly = calendar.isDefault,
            onValueChange = { calendarName = it },
            placeholder = {
                Text(
                    text = "Enter the name of calendar"
                )
            },
        )

        Spacer(modifier = Modifier.padding(4.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = calendarDescription,
            onValueChange = { calendarDescription = it },
            placeholder = {
                Text(
                    text = "Enter the description for this calendar"
                )
            }
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Button(onClick = {
            if (calendarName != calendar.name) editedFields["name"] = calendarName
            if (calendarDescription != calendar.description)
                editedFields["description"] = calendarDescription
            editCalendar(editedFields)
        }) {
            Icon(imageVector = Icons.Filled.Done, contentDescription = "save")
            Text("Save")
        }
    }
}

