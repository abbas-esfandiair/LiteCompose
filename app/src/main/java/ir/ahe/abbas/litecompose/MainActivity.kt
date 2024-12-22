package ir.ahe.abbas.litecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ir.ahe.abbas.library.litecolumn.HorizontalAlignment
import ir.ahe.abbas.library.litecolumn.LiteColumn
import ir.ahe.abbas.library.literow.LiteRow
import ir.ahe.abbas.library.literow.VerticalAlignment
import ir.ahe.abbas.litecompose.ui.theme.LiteComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LiteComposeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Test(modifier = Modifier.fillMaxSize().padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Test(modifier: Modifier) {
    LazyColumn (modifier){

        items(100) {

            LiteRow(modifier = Modifier.fillMaxWidth(), spaceBetweenItem = 8.dp) {

                Icon(
                    modifier = Modifier.align(VerticalAlignment.Center),
                    painter = painterResource(R.drawable.ic_launcher_background),
                    contentDescription = "icon"
                )


                LiteColumn (horizontalAlignment = HorizontalAlignment.Center){
                    Text("title")
                    Text("subtitle")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LiteComposeTheme {
        Test(Modifier.fillMaxSize())
    }
}