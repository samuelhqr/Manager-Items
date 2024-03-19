package com.mundoti.minhaoficina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VehicleListActivity extends AppCompatActivity implements VehicleAdapter.OnDeleteClickListener, VehicleAdapter.OnEditClickListener {
    private RecyclerView recyclerView;
    private VehicleAdapter vehicleAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Button btnLogout; // Botão de logout
    private Button btnAdd; // Botão de adicionar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_list);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // Se o usuário não estiver autenticado, redireciona para a tela de login
            startActivity(new Intent(VehicleListActivity.this, LoginActivity.class));
            finish(); // Finaliza a activity atual
            return;
        }

        String userId = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userId).child("veiculos");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        vehicleAdapter = new VehicleAdapter();
        recyclerView.setAdapter(vehicleAdapter);
        vehicleAdapter.setOnDeleteClickListener(this);
        vehicleAdapter.setOnEditClickListener(this);
        btnLogout = findViewById(R.id.btnLogout); // Encontra o botão de logout
        btnAdd = findViewById(R.id.btnAdd); // Encontre o botão de adicionar

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abra a tela para adicionar um novo veículo
                startActivity(new Intent(VehicleListActivity.this, VehicleAddActivity.class));
            }
        });

        //botao de logout
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Realiza logout do usuário
                firebaseAuth.signOut();
                // Redireciona para a tela de login
                startActivity(new Intent(VehicleListActivity.this, LoginActivity.class));
                finish(); // Finaliza a activity atual
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                vehicleAdapter.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vehicle vehicle = snapshot.getValue(Vehicle.class);
                    vehicleAdapter.addVehicle(vehicle);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
    @Override
    public void onDeleteClick(int position) {
        if (position >= 0 && position < vehicleAdapter.getItemCount()) {
            Vehicle vehicleToDelete = vehicleAdapter.getVehicle(position);
            if (vehicleToDelete != null) {
                String vehicleId = vehicleToDelete.getId();
                if (vehicleId != null && !vehicleId.isEmpty()) {
                    // Atualize a referência do banco de dados para incluir o caminho completo até o veículo
                    DatabaseReference vehicleRef = databaseReference.child(vehicleId);

                    // Remove o veículo do banco de dados
                    vehicleRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Remova o veículo da lista exibida
                                    vehicleAdapter.removeVehicle(position);

                                    // Exiba uma mensagem de sucesso
                                    Toast.makeText(VehicleListActivity.this, "Veículo excluído com sucesso", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Exiba uma mensagem de erro caso a exclusão falhe
                                    Toast.makeText(VehicleListActivity.this, "Erro ao excluir veículo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(VehicleListActivity.this, "ID do veículo inválido", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(VehicleListActivity.this, "Veículo inválido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(VehicleListActivity.this, "Posição inválida", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onEditClick(int position) {
        Vehicle vehicleToEdit = vehicleAdapter.getVehicle(position);
        if (vehicleToEdit != null) {
            // Aqui você pode passar os dados do veículo para a Activity de edição
            Intent editIntent = new Intent(VehicleListActivity.this, VehicleEditActivity.class);
            editIntent.putExtra("vehicleId", vehicleToEdit.getId());
            startActivity(editIntent);
        }
    }
}
