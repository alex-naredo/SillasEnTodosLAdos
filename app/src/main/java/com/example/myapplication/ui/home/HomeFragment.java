package com.example.myapplication.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Booking;
import com.example.myapplication.BookingActivity2;
import com.example.myapplication.BookingDatabaseHelper;
import com.example.myapplication.MainActivity3;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {
    private BookingDatabaseHelper databaseHelper;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new BookingDatabaseHelper(getActivity());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView tvFecha= view.findViewById(R.id.date);
        Booking booking = databaseHelper.getLatestBooking();
        if (booking != null) {
            tvFecha.setText(booking.getSelectedDate());
        }

        TextView tvHora= view.findViewById(R.id.time);
        if (booking != null) {
            tvHora.setText(booking.getSelectedTime());
        }

        TextView tvAcceso= view.findViewById(R.id.acceso);
        if (booking != null) {
            tvAcceso.setText(booking.getAccess());
        }

        TextView tvUbi= view.findViewById(R.id.location);
        if (booking != null) {
            tvUbi.setText(booking.getLocation());
        }

        ImageView imageView = view.findViewById(R.id.locimage);


        if (booking != null) {
            tvFecha.setText(booking.getSelectedDate());
            tvHora.setText(booking.getSelectedTime());
            tvAcceso.setText(booking.getAccess());
            tvUbi.setText(booking.getLocation());

            if (booking.getAccess().equals("Rectoría")) {
                imageView.setImageResource(R.drawable.rectoria);
            } else if (booking.getAccess().equals("CIAP")) {
                imageView.setImageResource(R.drawable.img_2);
            } else {
                imageView.setImageResource(R.drawable.img);
            }
        }



    FloatingActionButton fabNewBooking = view.findViewById(R.id.fab_new_booking);
        fabNewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí se ejecuta el código al hacer clic en el FloatingActionButton
                // Abre la actividad BookingActivity2 al hacer clic
                Intent intent = new Intent(getActivity(), MainActivity3.class);
                startActivity(intent);
            }
        });

        return view;
    }
}