package com.example.sharedcalendar.feature.editcalendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier,
    calendar: Calendar,
    firebaseViewModel: FirebaseViewModel
) {
    EditCalendarScreen(modifier, calendar, firebaseViewModel)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCalendarScreen(
    modifier: Modifier = Modifier,
    calendar: Calendar,
    firebaseViewModel: FirebaseViewModel
) {

    val context = LocalContext.current

    fun editCalendar(editedFields: HashMap<String, Any>) {
        // TODO: Modify values in db
        for (field in editedFields) {
            when (field.key) {
                "name" -> (editedFields["name"]!! as String).isBlank()
                    .run { editedFields.remove("name") }

            }
        }
    }

    val editedFields = hashMapOf<String, Any>()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Text(
            text = "Edit Calendar Information",
            style = MaterialTheme.typography.bodyLarge
        )
        TextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = calendar.name,
            onValueChange = { editedFields["name"] = it },
            placeholder = {
                Text(
                    text = "Enter the name of calendar"
                )
            },
        )

        Spacer(modifier = Modifier.padding(4.dp))



        Button(
            onClick = { editCalendar(editedFields) }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "save")
            Text("Save")
        }
    }
}

