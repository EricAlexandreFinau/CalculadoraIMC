package com.example.calculadoraimc // Verifique seu nome de pacote

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

sealed class AuthResult {
    data class Success(val user: FirebaseUser) : AuthResult()
    data class Error(val message: String) : AuthResult()
}
class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    val authResult = MutableLiveData<AuthResult>()

    val statusMessage = MutableLiveData<String>()

    fun login(email: String, senha: String) {
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener { userCredential ->
                authResult.value = AuthResult.Success(userCredential.user!!)
            }
            .addOnFailureListener { exception ->
                authResult.value = AuthResult.Error(exception.message ?: "Erro no login")
            }
    }
    fun cadastrar(
        email: String,
        senha: String,
        nome: String,
        idade: String,
        altura: String,
        genero: String,
        peso: String
    ) {
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener { userCredential ->
                val user = userCredential.user!!

                val dadosUsuario = hashMapOf(
                    "nome" to nome,
                    "idade" to idade,
                    "altura" to altura,
                    "genero" to genero,
                    "peso" to peso
                )

                db.collection("users").document(user.uid)
                    .set(dadosUsuario)
                    .addOnSuccessListener {
                        authResult.value = AuthResult.Success(user)
                    }
                    .addOnFailureListener { exception ->
                        authResult.value = AuthResult.Error(exception.message ?: "Erro ao salvar dados")
                    }
            }
            .addOnFailureListener { exception ->
                authResult.value = AuthResult.Error(exception.message ?: "Erro no cadastro")
            }
    }

    fun recuperarSenha(email: String) {
        if (email.isBlank()) {
            statusMessage.value = "Por favor, digite seu e-mail"
            return
        }

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {

                statusMessage.value = "E-mail de recuperação enviado para $email"
            }
            .addOnFailureListener { exception ->

                statusMessage.value = exception.message ?: "Erro ao enviar e-mail"
            }
    }
}