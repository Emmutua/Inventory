package com.example.pricecalc

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pricecalc.ui.theme.CommodityUiEvent
import com.example.pricecalc.ui.theme.CommodityViewModel
import com.example.pricecalc.ui.theme.PriceCalcTheme
import com.example.pricecalc.ui.theme.UiEvent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PriceCalcTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    InventoryApp()
                }
            }
        }
    }
}

@Composable
fun InventoryApp() {
    val viewModel: CommodityViewModel = viewModel()
    val uiState = viewModel.uiState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(key1 = true){
            viewModel.uiEvent.collect{
                event ->
                when(event){
                    is UiEvent.ShowToastMessage -> {
                        Toast.makeText(context, "${event.msg}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Maize:",
            textAlign = TextAlign.Start,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold
        )
        ContentRow(
            item = "Price",
            price_quantity = " Ksh: ${uiState.value.price}",
            onAdd = { viewModel.handleUiEvent(CommodityUiEvent.OnAddPrice("Price Added")) },
            onReduce = { viewModel.handleUiEvent(CommodityUiEvent.OnReducePrice("Price Reduced")) }
        )
        ContentRow(
            item = "Quantity",
            price_quantity = " ${uiState.value.quantity}",
            onAdd = { viewModel.handleUiEvent(CommodityUiEvent.OnAddQuantity("Quantity Added")) },
            onReduce = { viewModel.handleUiEvent(CommodityUiEvent.OnReduceQuantity("Quantity Reduced")) }
        )
        Text(
            text = "Total: ${uiState.value.total}",
            modifier = Modifier.padding(8.dp),
            fontWeight = FontWeight.ExtraBold,
            fontSize = 30.sp,
            textAlign = TextAlign.Left
        )
    }

}


@Composable
fun ContentRow(
    item: String,
    price_quantity: String,
    onAdd: () -> Unit,
    onReduce: () -> Unit
) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$item:",
            textAlign = TextAlign.Left,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Text(text = "$price_quantity", fontSize = 20.sp, fontWeight = FontWeight.Light)
        IconButton(onClick = {
            onAdd()
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription ="Add")
        }
        IconButton(onClick = {
            onReduce()
        }) {
            Text(text = "-", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        }
    }
}