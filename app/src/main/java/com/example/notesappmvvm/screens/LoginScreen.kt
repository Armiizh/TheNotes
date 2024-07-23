package com.example.notesappmvvm.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.utils.Constants
import com.example.notesappmvvm.utils.DB_TYPE
import com.example.notesappmvvm.utils.LOGIN
import com.example.notesappmvvm.utils.PASSSWORD
import com.example.notesappmvvm.utils.TYPE_FIREBASE

@Composable
fun LoginScreen(navController: NavHostController, viewModel: MainViewModel) {

    var login by remember { mutableStateOf(Constants.Keys.EMPTY) }
    var password by remember { mutableStateOf(Constants.Keys.EMPTY) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(PaddingValues(32.dp))
        ) {
            Text(
                text = Constants.Keys.LOG_IN,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            OutlinedTextField(
                value = login,
                onValueChange = { login = it },
                label = { Text(text = Constants.Keys.LOGIN_TEXT) },
                isError = login.isEmpty()
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(text = Constants.Keys.PASSWORD_TEXT) },
                isError = password.isEmpty()
            )
            //Update btn in Sheet
            Button(
                modifier = Modifier.padding(top = 16.dp),
                onClick = {
                    LOGIN = login
                    PASSSWORD = password
                    viewModel.initDatabase(TYPE_FIREBASE) {
                        DB_TYPE.value = TYPE_FIREBASE
                        navController.navigate(NavRoute.Main.route)
                    }
                },
                enabled = login.isNotEmpty() && password.isNotEmpty()
            ) {
                Text(text = Constants.Keys.SIGN_IN)
            }
        }
    }
}