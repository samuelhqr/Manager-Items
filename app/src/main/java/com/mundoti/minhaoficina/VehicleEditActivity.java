package com.mundoti.minhaoficina;

import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VehicleEditActivity extends AppCompatActivity {
    private EditText editTextModel;
    private EditText editTextBrand;
    private EditText editTextYear;
    private EditText editTextPlate;
    private EditText editTextColor;
    private EditText editTextOwner;
    private EditText editTextObservation;
    private DatabaseReference databaseReference;
    private String vehicleId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_edit);

        editTextModel = findViewById(R.id.editTextModel);
        editTextBrand = findViewById(R.id.editTextBrand);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPlate = findViewById(R.id.editTextPlate);
        editTextColor = findViewById(R.id.editTextColor);
        editTextOwner = findViewById(R.id.editTextOwner);
        editTextObservation = findViewById(R.id.editTextObservation);
        Button buttonSave = findViewById(R.id.buttonSave);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Se o usuário não estiver autenticado, exiba uma mensagem e encerre a atividade
            Toast.makeText(this, "Usuário não autenticado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("veiculos");

        // Recupere o ID do veículo passado da atividade anterior
        vehicleId = getIntent().getStringExtra("vehicleId");
        if (vehicleId == null || vehicleId.isEmpty()) {
            // Se o ID do veículo não for válido, exiba uma mensagem e encerre a atividade
            Toast.makeText(this, "ID do veículo inválido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadVehicleDetails();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateVehicle();
            }
        });
    }



    private void updateVehicle() {
        String modelo = editTextModel.getText().toString().trim();
        String marca = editTextBrand.getText().toString().trim();
        String ano = editTextYear.getText().toString().trim();
        String placa = editTextPlate.getText().toString().trim();
        String cor = editTextColor.getText().toString().trim();
        String dono = editTextOwner.getText().toString().trim();
        String observacoes = editTextObservation.getText().toString().trim();

        if (modelo.isEmpty() || marca.isEmpty() || ano.isEmpty() || placa.isEmpty() || cor.isEmpty() || dono.isEmpty() || observacoes.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        Vehicle vehicle = new Vehicle(vehicleId, modelo, marca, ano, placa, cor, dono, observacoes);

        // Atualizar o veículo no banco de dados Firebase
        databaseReference.child(vehicleId).setValue(vehicle)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(VehicleEditActivity.this, "Veículo atualizado com sucesso.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(VehicleEditActivity.this, "Erro ao atualizar veículo. Por favor, tente novamente.", Toast.LENGTH_SHORT).show();
                    }

                });
    }
    private void loadVehicleDetails() {
        // Verifique se o DatabaseReference não é nulo antes de chamar o método child()
        if (databaseReference != null) {
            databaseReference.child(vehicleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Verifique se o snapshot contém dados
                    if (dataSnapshot.exists()) {
                        Vehicle vehicle = dataSnapshot.getValue(Vehicle.class);
                        if (vehicle != null) {
                            // Carregar os detalhes do veículo nos campos de edição
                            editTextModel.setText(vehicle.getModel());
                            editTextBrand.setText(vehicle.getBrand());
                            editTextYear.setText(vehicle.getYear());
                            editTextPlate.setText(vehicle.getPlate());
                            editTextColor.setText(vehicle.getColor());
                            editTextOwner.setText(vehicle.getOwner());
                            editTextObservation.setText(vehicle.getObservation());
                        }
                    } else {
                        // Se não houver dados para o ID do veículo, exiba uma mensagem
                        Toast.makeText(VehicleEditActivity.this, "Nenhum dado encontrado para este veículo", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Em caso de erro, exiba uma mensagem de erro
                    Toast.makeText(VehicleEditActivity.this, "Erro ao carregar detalhes do veículo: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Se o DatabaseReference for nulo, exiba uma mensagem de erro
            Toast.makeText(this, "Erro ao carregar detalhes do veículo: DatabaseReference é nulo", Toast.LENGTH_SHORT).show();
        }
    }

}
