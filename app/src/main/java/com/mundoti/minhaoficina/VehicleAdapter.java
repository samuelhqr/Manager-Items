package com.mundoti.minhaoficina;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<Vehicle> vehicleList;
    private static OnDeleteClickListener mListener;
    private static OnEditClickListener mEditListener;

    public void removeVehicle(int position) {
        if (vehicleList != null && position >= 0 && position < vehicleList.size()) {
            vehicleList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void updateVehicle(Vehicle vehicle) {
        for (int i = 0; i < vehicleList.size(); i++) {
            if (vehicleList.get(i).getId().equals(vehicle.getId())) {
                vehicleList.set(i, vehicle);
                notifyItemChanged(i);
                return;
            }
        }
    }

    public int findVehiclePosition(Vehicle removedVehicle) {
        for (int i = 0; i < vehicleList.size(); i++) {
            Vehicle vehicle = vehicleList.get(i);
            if (vehicle.getId().equals(removedVehicle.getId())) {
                return i;
            }
        }
        return -1;
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        mListener = listener;
    }

    public interface OnEditClickListener {
        void onEditClick(int position);
    }

    public void setOnEditClickListener(OnEditClickListener listener) {
        mEditListener = listener;
    }

    public VehicleAdapter() {
        vehicleList = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicleList.add(vehicle);
        notifyDataSetChanged();
    }

    public void clear() {
        vehicleList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vehicle_item, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public Vehicle getVehicle(int position) {
        return vehicleList.get(position);
    }

    static class VehicleViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewModel;
        private TextView textViewBrand;
        private TextView textViewYear;
        private TextView textViewOwner;
        private TextView textViewObservation;
        private Button btnDelete;

        VehicleViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewModel = itemView.findViewById(R.id.txtModelo);
            textViewBrand = itemView.findViewById(R.id.txtMarca);
            textViewYear = itemView.findViewById(R.id.txtAno);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            textViewOwner = itemView.findViewById(R.id.txtDono);
            textViewObservation = itemView.findViewById(R.id.txtObservacao);

            btnDelete.setOnClickListener(view -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onDeleteClick(position);
                    }
                }
            });

            itemView.setOnClickListener(view -> {
                if (mEditListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mEditListener.onEditClick(position);
                    }
                }
            });
        }

        void bind(Vehicle vehicle) {
            textViewModel.setText(vehicle.getModel());
            textViewBrand.setText(vehicle.getBrand());
            textViewYear.setText(vehicle.getYear());
            textViewOwner.setText(vehicle.getOwner());
            textViewObservation.setText(vehicle.getObservation());
        }
    }
}
