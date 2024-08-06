package com.example.notesappmvvm.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.notesappmvvm.MainViewModel
import com.example.notesappmvvm.R
import com.example.notesappmvvm.navigation.NavRoute
import com.example.notesappmvvm.ui.theme.Black
import com.example.notesappmvvm.ui.theme.BlueGray
import com.example.notesappmvvm.ui.theme.LightBlueWhite
import com.example.notesappmvvm.ui.theme.focusedTextFielText
import com.example.notesappmvvm.ui.theme.textFieldContainer
import com.example.notesappmvvm.ui.theme.unfocusedTextFielText
import com.example.notesappmvvm.utils.Constants
import com.example.notesappmvvm.utils.DB_TYPE
import com.example.notesappmvvm.utils.LOGIN
import com.example.notesappmvvm.utils.PASSSWORD
import com.example.notesappmvvm.utils.TYPE_FIREBASE
import com.example.notesappmvvm.utils.TYPE_ROOM


@Composable
fun LoginScreen(navController: NavHostController, viewModel: MainViewModel) {

    var login by remember { mutableStateOf(Constants.Keys.EMPTY) }
    var password by remember { mutableStateOf(Constants.Keys.EMPTY) }

    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            val uiColor = if (isSystemInDarkTheme()) Color.White else Color.Black
            TopSection(uiColor)
            Spacer(modifier = Modifier.height(36.dp))
            LoginSection(
                login,
                password,
                uiColor,
                onLoginChanged = { login = it },
                onPasswordChanged = { password = it },
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun LoginSection(
    login: String,
    password: String,
    uiColor: Color,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    navController: NavHostController,
    viewModel: MainViewModel
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 30.dp)
    ) {
        LoginTextField(login, uiColor, onValueChange = onLoginChanged)
        Spacer(modifier = Modifier.height(15.dp))
        PasswordTextField(password, uiColor, onValueChange = onPasswordChanged)
        Spacer(modifier = Modifier.height(20.dp))
        LogInBtn(login, password, viewModel, navController)
        Spacer(modifier = Modifier.height(30.dp))
        GuestSection(viewModel, navController)
    }
}

@Composable
private fun GuestSection(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Column(
        verticalArrangement = Arrangement.Bottom
    ) {
        Text(
            modifier = Modifier.align(alignment = Alignment.CenterHorizontally),
            text = Constants.Keys.CONTINUE_AS_GUEST,
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .guest(),
            onClick = {
                viewModel.initDatabase(TYPE_ROOM) {
                    DB_TYPE.value = TYPE_ROOM
                    navController.navigate(route = NavRoute.Main.route)
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isSystemInDarkTheme()) Black else Color(0xFF64748B),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(size = 4.dp)
        ) {
            Text(
                text = Constants.Keys.LOG_IN_GUEST,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
            )
        }
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            textAlign = TextAlign.Center,
            text = Constants.Keys.DONT_HAVE_ACC,
            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF64748B))
        )
    }
}

@Composable
private fun LogInBtn(
    login: String,
    password: String,
    viewModel: MainViewModel,
    navController: NavHostController
) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        onClick = {
            LOGIN = login
            PASSSWORD = password
            viewModel.initDatabase(TYPE_FIREBASE) {
                DB_TYPE.value = TYPE_FIREBASE
                navController.navigate(NavRoute.Main.route)
            }
        },
        enabled = login.isNotEmpty() && password.isNotEmpty(),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSystemInDarkTheme()) BlueGray else Black,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(size = 4.dp)
    ) {
        Text(
            text = Constants.Keys.LOG_IN,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun LoginTextField(
    login: String,
    uiColor: Color,
    onValueChange: (String) -> Unit
) {
    val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    var isValidEmail by remember { mutableStateOf(true) }

    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = login,
        onValueChange = {
            onValueChange(it)
            isValidEmail = emailRegex.matches(it)
        },
        label = {
            Text(
                text = Constants.Keys.LOGIN_TEXT,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFielText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFielText,
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            autoCorrect = false,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (!isValidEmail && login.isNotEmpty()) {
                Text(
                    text = Constants.Keys.INVALID_EMAIL,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 10.dp, end = 8.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                null
            }
        }
    )
}

@Composable
private fun PasswordTextField(
    password: String,
    uiColor: Color,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    val isValidPassword = remember { mutableStateOf(false) }

    LaunchedEffect(password) {
        if (password.isNotEmpty()) {
            isValidPassword.value = password.length >= 8 && password.contains(Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$"))
        } else {
            isValidPassword.value = false
        }
    }


    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = onValueChange,
        label = {
            Text(
                text = Constants.Keys.PASSWORD_TEXT,
                style = MaterialTheme.typography.labelMedium,
                color = uiColor
            )
        },
        colors = TextFieldDefaults.colors(
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.unfocusedTextFielText,
            focusedPlaceholderColor = MaterialTheme.colorScheme.focusedTextFielText,
            unfocusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
            focusedContainerColor = MaterialTheme.colorScheme.textFieldContainer,
        ),
        trailingIcon = {
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                if (!isValidPassword.value && password.isNotEmpty()) {
                    Text(
                        text = Constants.Keys.INVALID_PASSWORD,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp, end = 8.dp),
                        style = MaterialTheme.typography.bodySmall
                    )
                } else {
                    null
                }
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) painterResource(R.drawable.ic_visibility) else painterResource(
                            R.drawable.ic_visibility_off
                        ),
                        contentDescription = if (passwordVisible) Constants.Keys.HIDE_PASSWORD else Constants.Keys.SHOW_PASSWORD
                    )
                }
            }
        },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            autoCorrect = false,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun TopSection(uiColor: Color) {
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.46f),
            painter = painterResource(id = R.drawable.shape),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier.padding(top = 70.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(
                    id = R.string.app_logo
                ),
                tint = uiColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(id = R.string.theNotes),
                    style = MaterialTheme.typography.headlineMedium,
                    color = uiColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(id = R.string.write_what_u_think),
                    style = MaterialTheme.typography.titleMedium,
                    color = uiColor,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .align(alignment = Alignment.BottomCenter),
            text = Constants.Keys.LOG_IN,
            style = MaterialTheme.typography.headlineLarge,
            color = uiColor
        )
    }
}

fun Modifier.guest(): Modifier = composed {
    if (isSystemInDarkTheme()) {
        this
            .background(Color.Transparent)
            .border(
                width = 1.dp,
                color = BlueGray,
                shape = RoundedCornerShape(4.dp)
            )
    } else {
        this.background(LightBlueWhite)
    }
}
