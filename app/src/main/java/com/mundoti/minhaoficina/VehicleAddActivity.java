package com.mundoti.minhaoficina;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VehicleAddActivity extends AppCompatActivity {
    private EditText editTextModel, editTextBrand, editTextYear, editTextPlate, editTextColor, editTextOwner, editTextObservation;
    private Button buttonAddVehicle;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_add);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Se o usuário não estiver autenticado, redireciona para a tela de login
            startActivity(new Intent(VehicleAddActivity.this, LoginActivity.class));
            finish(); // Finaliza a activity atual
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("veiculos");

        editTextModel = findViewById(R.id.editTextModel);
        editTextBrand = findViewById(R.id.editTextBrand);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPlate = findViewById(R.id.editTextPlate);
        editTextColor = findViewById(R.id.editTextColor);
        editTextOwner = findViewById(R.id.editTextOwner);
        editTextObservation = findViewById(R.id.editTextObservation);
        buttonAddVehicle = findViewById(R.id.buttonAddVehicle);

        buttonAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVehicle();
            }
        });
    }

    private void addVehicle() {
        String modelo = editTextModel.getText().toString().trim();
        String marca = editTextBrand.getText().toString().trim();
        String ano = editTextYear.getText().toString().trim();
        String placa = editTextPlate.getText().toString().trim();
        String cor = editTextColor.getText().toString().trim();
        String dono = editTextOwner.getText().toString().trim();
        String observacao = editTextObservation.getText().toString().trim();

        // Verifica se algum campo está vazio
        if (modelo.isEmpty() || marca.isEmpty() || ano.isEmpty() || placa.isEmpty() || cor.isEmpty() || dono.isEmpty() || observacao.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtém uma referência para um novo nó "vehicles" e define um novo ID para o veículo
        String vehicleId = databaseReference.push().getKey();

        // Cria um novo objeto Vehicle com os dados inseridos pelo usuário, incluindo o ID
        Vehicle vehicle = new Vehicle(vehicleId, modelo, marca, ano, placa, cor, dono, observacao);

        // Adiciona o veículo ao banco de dados Firebase
        databaseReference.child(vehicleId).setValue(vehicle)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Limpa os campos após a adição bem-sucedida
                        editTextModel.setText("");
                        editTextBrand.setText("");
                        editTextYear.setText("");
                        editTextPlate.setText("");
                        editTextColor.setText("");
                        editTextOwner.setText("");
                        editTextObservation.setText("");
                        Toast.makeText(VehicleAddActivity.this, "Veículo adicionado com sucesso.", Toast.LENGTH_SHORT).show();

                        // Log para verificar se os dados foram gravados com sucesso
                        Log.d("Firebase", "Dados gravados com sucesso: " + vehicle.toString());

                        // Redireciona para a tela de lista de veículos
                        startActivity(new Intent(VehicleAddActivity.this, VehicleListActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VehicleAddActivity.this, "Erro ao adicionar veículo. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                        // Log para verificar o erro ao gravar os dados
                        Log.e("Firebase", "Erro ao gravar dados: " + e.getMessage());
                    }
                });
    }
}
