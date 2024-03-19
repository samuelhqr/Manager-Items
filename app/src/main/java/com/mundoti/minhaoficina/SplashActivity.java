package com.mundoti.minhaoficina;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inicializa o FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Define um tempo de espera para a tela de splash
        int SPLASH_SCREEN_TIMEOUT = 3000;

        // Cria um handler para executar uma ação após o tempo de espera
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Verifica se o usuário está autenticado
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // Se o usuário estiver autenticado, inicia a tela de lista de veículos
                    startActivity(new Intent(SplashActivity.this, VehicleListActivity.class));
                } else {
                    // Se o usuário não estiver autenticado, inicia a tela de login
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                }
                // Finaliza a SplashActivity para que o usuário não possa retornar para ela após ir para outra atividade
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}
