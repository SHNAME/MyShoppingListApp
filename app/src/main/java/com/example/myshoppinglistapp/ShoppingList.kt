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
        , horizontalArrangement = Arrangement.SpaceBetween

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
//수직 정렬
    ) {
        Button(onClick = { showDialog = true }
            , modifier = Modifier.align(Alignment.CenterHorizontally)//수평 정렬
        ) {

            Text("Add Item")
        }
        //lazyColumn은 스크롤 가능한 단일 컬럼 목록을 만들기 위해 사용된다.
        /*쉽게 생각하면 인스타를 스크롤 할 때 필요한 것만 보이게 하고 나머지는 안보이게 해서
        메모리를 사용을 줄여준다. 때문에 대량의  아이템 목록을 효율적으로 처리 가능한다.
        * */
        LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            items(sItems){
                item ->
                if(item.isEditing){
                    ShoppingItemEditor(item = item, onEditComplete = {
                        editedName,editedQuantity ->
                        sItems = sItems.map{it.copy(isEditing = false)}
                        val editedItem = sItems.find{it.id == item.id}
                        //find는 리스트에서 람다식 내부에 조건을 참으로 만드는 첫번재 요소를 반환한다.
                        //만약 조건을 만족하지 못 할 경우(false인 경우)null을 반환한다.
                        editedItem?.let{
                            //editedItem은 객체를 담고 있다
                            //?는 안전호출 연산자로 객체가 NULL이 아닌 경우에만 메서드를 호출한다.
                            //let은 editedItem의 객체를 매개변수에 전달하고 람다 블록 내 식을 실행시킨다.
                            it.name  = editedName
                            it.quantity =editedQuantity
                        }
                    })
                }
                else
                    ShoppingListItem(item =item ,
                        onEditClick = {
                        sItems =  sItems.map{it.copy(isEditing = it.id == item.id)}
                            /*
                            map 함수는 리스트의 각 요소에 대해 람다식을 적용하여 새로운 리스트를 생성해 반환해주는 함수이다
                            위의 경우 sItems는  Data 객체 리스트로 리스트 각 요소 안을 다 돌면서 각 요소(it)의 id가
                            item의 id와 같으면
                            isEditing을 true로 변환하여 요소의 멤버 변수 상태를 바꾸고
                            새로운 리스트를 반환하여  Sitems에 복사해준다.

                             */
                    },onDeleteClick = {sItems =sItems - item}) //

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
                                        itemQuantity = ""
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

            //BasicTextField의 경우 단순한 텍스 입력 필드로 외곽선이나 배경을 가지고 있지 않다. 텍스트 필드를 커스텀할 때 많이 사용한다.
            BasicTextField(value =editedName , onValueChange = {editedName = it},
                singleLine = true
                ,modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )//wrapContentSize는 Field 크기를 필요한 만큼만 차지한다.
            //위의 경우 text가 5글자면 5글자의 크기 만큼만 공간을 차지한다.
            BasicTextField(value =editedQuantity , onValueChange = {editedQuantity = it},
                singleLine = true
                ,modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
            )

        }
        Button(onClick = {
            isEditing = false
            onEditComplete(editedName,editedQuantity.toIntOrNull()?:1)
        } ){
            Text("Save")

    }

}







}

