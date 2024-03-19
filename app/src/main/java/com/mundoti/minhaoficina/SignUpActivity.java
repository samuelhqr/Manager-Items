package com.mundoti.minhaoficina;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.mundoti.minhaoficina.VehicleListActivity;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword;
    private Button buttonSignUp, buttonBackToLogin; // Adicionando o botão de voltar
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonBackToLogin = findViewById(R.id.buttonBackToLogin); // Inicializando o botão de voltar

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpUser();
            }
        });

        // Configurando o listener para o botão de voltar
        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Inicia a LoginActivity ao clicar no botão de voltar
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                finish(); // Finaliza a SignUpActivity para que não seja possível retornar a ela usando o botão "Voltar" do dispositivo
            }
        });
    }

    private void signUpUser() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Enter email address");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Enter a valid email address");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Enter password");
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(SignUpActivity.this, VehicleListActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Registration failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
