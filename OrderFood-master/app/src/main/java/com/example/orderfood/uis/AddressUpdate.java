package com.example.orderfood.uis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.orderfood.R;
import com.example.orderfood.models.User;
import com.example.orderfood.models.database.DBHelper;
import com.example.orderfood.models.database.PrefManager;
import com.example.orderfood.models.database.UserRepository;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddressUpdate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressUpdate extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    UserRepository userRepository;
    DBHelper dbHelper;
    ImageView btnBack3;
    NavController navController;
    EditText edtDiaChiDatHang;
    Button btnUpdateAddress;
    User user;
    public AddressUpdate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressUpdate.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressUpdate newInstance(String param1, String param2) {
        AddressUpdate fragment = new AddressUpdate();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_address_update, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setVariable();
        btnUpdateAddress.setOnClickListener(view1 -> {
            String address=edtDiaChiDatHang.getText().toString().trim();
            if(address.length() <= 0 ){
                Toast.makeText(getContext(), "Please enter complete information!", Toast.LENGTH_SHORT).show();
            }
            else {
                user.setAddress(address);
                userRepository.updateUser(user,user.getId());
                Toast.makeText(getContext(), "Update successful!", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.action_addressUpdateFragment_to_cartFragment);
            }
        });
    }
    private void initView(View view) {
        edtDiaChiDatHang=view.findViewById(R.id.edtDiaChiDatHang);
        btnUpdateAddress=view.findViewById(R.id.btnUpdateAddress);
        btnBack3=view.findViewById(R.id.btnBack3);
        dbHelper=new DBHelper(getContext());
        userRepository=new UserRepository(dbHelper);
        user=userRepository.getUserByUsername(PrefManager.getString(getContext(),"username"));
        edtDiaChiDatHang.setText(user.getAddress());
        navController= NavHostFragment.findNavController(AddressUpdate.this);
    }
    private void setVariable() {
        btnBack3.setOnClickListener(v -> requireActivity().finish());
    }
}