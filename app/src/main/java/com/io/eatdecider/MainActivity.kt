package com.io.eatdecider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.io.eatdecider.ui.PlaceToEatPickerScreen
import com.io.eatdecider.ui.theme.EatDeciderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatDeciderTheme {
                EatDeciderApp()
            }
        }
    }
}

@Composable
private fun EatDeciderApp() {
    PlaceToEatPickerScreen()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    EatDeciderTheme {
        EatDeciderApp()
    }
}
