package com.example.sharedcalendar.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
class ManageCalendarsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {

                Column {
                    ListItem(text = { Text("One line list item with no icon") })
                    Divider()
                    ListItem(text = { Text("One line list item with 24x24 icon") }, icon = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = null
                        )
                    })
                    Divider()
                    ListItem(text = { Text("One line list item with 40x40 icon") }, icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    })
                    Divider()
                    ListItem(text = { Text("One line list item with 56x56 icon") }, icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    })
                    Divider()
                    ListItem(text = { Text("One line clickable list item") }, icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(56.dp)
                        )
                    }, modifier = Modifier.clickable { })
                    Divider()
                    ListItem(text = { Text("One line list item with trailing icon") }, trailing = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = "Localized Description"
                        )
                    })
                    Divider()
                    ListItem(text = { Text("One line list item") }, icon = {
                        Icon(
                            Icons.Filled.Favorite,
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                    }, trailing = {
                        Icon(
                            Icons.Filled.Favorite, contentDescription = "Localized description"
                        )
                    })
                    Divider()
                }
            }

        }
    }
}

