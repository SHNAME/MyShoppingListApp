package com.example.myshoppinglistapp

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int,
                        var name:String,
                        var quantity:Int,
                        var isEditing:Boolean = false)


@Composable
fun ShoppingListApp()
{
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement =  Arrangement.Center

    ) {
        Button(onClick = { showDialog = true }
            , modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {

            Text("Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            items(sItems){
                ShoppingListItem(it,{},{})
            }

        }

    }
    if(showDialog)
    {
        AlertDialog(onDismissRequest = { showDialog = false },
            confirmButton = {
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween)
                            {
                                //add  버튼을 클릭했을 때
                                Button(onClick = {
                                    if(itemName.isNotBlank())// itemName 칸이 채워져있을 경우
                                    {
                                        val newItem = ShoppingItem(
                                            id = sItems.size + 1,
                                            name = itemName,
                                            quantity = itemQuantity.toInt()
                                        )
                                        sItems = sItems + newItem
                                        showDialog = false // alertDialog를 닫아줌
                                        itemName = "" // 해당 객체를 넣고 난 후 이름을 초기화 한다.
                                    }


                                }) {
                                    Text("Add")

                                }
                                Button(onClick = { showDialog = false }) {
                                    Text("Cancel")

                                }

                            }
            },
            title = { Text("Add Shopping Item")},
        text = {
            Column {
                OutlinedTextField(value = itemName ,
                    onValueChange ={itemName = it},
                    singleLine =  true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .border(width = 5.dp, color = Color.Blue, shape = CircleShape)

                )
                OutlinedTextField(value = itemQuantity ,
                    onValueChange ={itemQuantity = it},
                    singleLine =  true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)

                )

            }

            }
        )

    }

}
@Composable
//추가한 항목에 대한 수정 버튼을 눌렀을 때 호출할 함수를 구현
fun ShoppingItemEditor(item: ShoppingItem,
                       onEditComplete:(String,Int) -> Unit)
{
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember{
        mutableStateOf(item.isEditing)
    }

    Row(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly)
    {
        Column {

            BasicTextField(value =editedName , onValueChange = {editedName = it},
                singleLine = true
                ,modifier = Modifier.wrapContentSize().padding(8.dp)
            )//wrapContentSize는 Field 크기를 필요한 만큼만 차지한다.
            //위의 경우 text가 5글자면 5글자의 크기 만큼만 공간을 차지한다.
            BasicTextField(value =editedQuantity , onValueChange = {editedQuantity = it},
                singleLine = true
                ,modifier = Modifier.wrapContentSize().padding(8.dp)
            )

        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
        } ){
            Text("Save")

    }

}



@Composable
fun ShoppingListItem(item:ShoppingItem,
                     onEditClick: () -> Unit,
                     onDeleteClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(2.dp, Color(0xFF018786)),
                shape = RoundedCornerShape(20)
            )
    )
    {
        Text(text = item.name,modifier =Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}",
            modifier= Modifier.padding(8.dp))

        Row(modifier = Modifier.padding(8.dp)){
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit,contentDescription = null)
            }

            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete,contentDescription = null)
            }


        }


        }
    }




}

