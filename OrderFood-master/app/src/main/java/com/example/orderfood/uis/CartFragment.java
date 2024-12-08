package com.example.orderfood.uis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.orderfood.R;
import com.example.orderfood.models.User;
import com.example.orderfood.models.database.DBHelper;
import com.example.orderfood.models.database.PrefManager;
import com.example.orderfood.models.database.UserRepository;
import com.example.orderfood.adapters.CartAdapter;
import com.example.orderfood.models.Cart;
import com.example.orderfood.models.database.CartRepository;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    DBHelper dbHelper;
    CartRepository cartRepository;
    UserRepository userRepository;
    List<Cart> list;
    TextView tvTotal,tvSubtotal, tvDiaChiDatHang;
    ImageView btnBack2, btnChangeAddress;
    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    Button btnCheckOut;
    User user;
    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvDiaChiDatHang=view.findViewById(R.id.edtDiaChiDatHang);
        btnChangeAddress=view.findViewById(R.id.btnChangeAddress);
        recyclerView=view.findViewById(R.id.rcvCart);
        btnCheckOut=view.findViewById(R.id.btnUpdateAddress);
        btnBack2=view.findViewById(R.id.btnBack3);
        tvTotal=view.findViewById(R.id.tvTotal);
        tvSubtotal=view.findViewById(R.id.tvSubtotal);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this.getContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        dbHelper=new DBHelper(getContext());
        cartRepository=new CartRepository(dbHelper,getContext());
        list=cartRepository.getFoodsInCartByUserId(PrefManager.getUserId(getContext(),"username"));
        cartAdapter=new CartAdapter(list,getContext(), this);
        recyclerView.setAdapter(cartAdapter);
        userRepository=new UserRepository(dbHelper);
        user=userRepository.getUserByUsername(PrefManager.getString(getContext(),"username"));
        tvDiaChiDatHang.setText(user.getAddress());
        btnCheckOut.setOnClickListener(view1 -> {
            NavController navController = NavHostFragment.findNavController(CartFragment.this);
            Bundle bundle = new Bundle();
            bundle.putInt("money", cartRepository.money());
            navController.navigate(R.id.action_cartFragment_to_payFragment, bundle);
        });
        btnChangeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_cartFragment_to_addressUpdateFragment);
            }
        });
        updateTotalPrices();
        setVariable();
    }
    public void updateTotalPrices() {
        int subtotal = cartRepository.money();
        tvSubtotal.setText(subtotal + " VNĐ");
        tvTotal.setText((subtotal + 20000) + " VNĐ");
    }
    private void setVariable() {
        btnBack2.setOnClickListener(v -> requireActivity().finish());
    }
}