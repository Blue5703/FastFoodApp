package com.example.orderfood.uis;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import android.Manifest;
import com.example.orderfood.R;
import com.example.orderfood.models.Cart;
import com.example.orderfood.models.Food;
import com.example.orderfood.models.User;
import com.example.orderfood.models.database.CartRepository;
import com.example.orderfood.models.database.DBHelper;
import com.example.orderfood.models.database.FavouriteRepository;
import com.example.orderfood.models.database.FoodRepository;
import com.example.orderfood.models.database.PrefManager;
import com.example.orderfood.models.database.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ImageView imgFoodDetail, favicon, btnBack, btnCall, btnChat, btnToCart;
    TextView tvNameDetail,tvPriceDetail,tvQuantityDetail1, imgCongDetail1,imgTruDetail1;
    int foodId;
    Button btnAddToCartDetail1;
    FoodRepository foodRepository;
    CartRepository cartRepository;
    DBHelper dbHelper;


    public DetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailsFragment newInstance(String param1, String param2) {
        DetailsFragment fragment = new DetailsFragment();
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
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        foodId=getArguments().getInt("food_id");
        initView(view);
        List<Integer> list=new ArrayList<>();
        setVariable();
        btnChat.setOnClickListener(v -> smsIntent());
        btnCall.setOnClickListener(v -> callIntent());
        int quantity1=Integer.parseInt(tvQuantityDetail1.getText().toString());
        imgCongDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvQuantityDetail1.setText(String.valueOf(Integer.parseInt(tvQuantityDetail1.getText().toString())+1));
            }
        });
        imgTruDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tvQuantityDetail1.getText().toString())>0){
                    tvQuantityDetail1.setText(String.valueOf(Integer.parseInt(tvQuantityDetail1.getText().toString())-1));
                }
            }
        });
        btnAddToCartDetail1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean check=cartRepository.checkCart(PrefManager.getUserId(getContext(),"username"),foodId);
                if(check==true && Integer.parseInt(tvQuantityDetail1.getText().toString()) >0 ){
                    Cart cart=cartRepository.getCartByFoodIdUserId(PrefManager.getUserId(getContext(),"username"),foodId);
                    cart.setQuantity(cart.getQuantity()+Integer.parseInt(tvQuantityDetail1.getText().toString()));
                    cartRepository.updateCart(cart,foodId);
                    Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                }else{
                    if(Integer.parseInt(tvQuantityDetail1.getText().toString())>0){
                        Cart cart=new Cart(PrefManager.getUserId(getContext(),"username"),foodId,Integer.parseInt(tvQuantityDetail1.getText().toString()));
                        cartRepository.addCart(cart);
                        Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_detailsFragment_to_cartFragment);
            }
        });
    }

    private void setVariable() {
        btnBack.setOnClickListener(v -> requireActivity().finish());
    }
    private void smsIntent() {
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        smsIntent.setData(Uri.parse("smsto:0392383590"));
        startActivity(smsIntent);
    }
    private void callIntent() {
        String phoneNumber = "0392383590";
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));

        // Kiểm tra quyền trước khi thực hiện cuộc gọi
        if (getContext() != null &&
                ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        } else {
            // Yêu cầu quyền nếu chưa được cấp
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Quyền đã được cấp, thực hiện cuộc gọi
                callIntent();
            } else {
                // Quyền bị từ chối
                Toast.makeText(getContext(), "Quyền gọi điện thoại đã bị từ chối!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void initView(View view) {
        btnBack=view.findViewById(R.id.btnBack);
        btnChat=view.findViewById(R.id.btnChat);
        btnCall=view.findViewById(R.id.btnCall);
        btnToCart=view.findViewById(R.id.btnToCart);
        favicon=view.findViewById(R.id.favicon);
        btnAddToCartDetail1=view.findViewById(R.id.btnAddToCartDetail1);
        imgCongDetail1=view.findViewById(R.id.imgCongDetail1);
        imgTruDetail1=view.findViewById(R.id.imgTruDetail1);
        tvQuantityDetail1=view.findViewById(R.id.tvQuantityDetail1);
        imgFoodDetail=view.findViewById(R.id.imgFoodDetail);
        tvNameDetail=view.findViewById(R.id.tvNameDetail);
        tvPriceDetail=view.findViewById(R.id.tvPriceDetail);
        dbHelper=new DBHelper(getContext());
        foodRepository=new FoodRepository(dbHelper);
        cartRepository=new CartRepository(dbHelper,getContext());
        Food food=foodRepository.getFoodByFoodId(foodId);

        //Yêu thích

        Glide.with(getContext()).load(food.getImage()).into(imgFoodDetail);
        tvNameDetail.setText(food.getName());
        tvPriceDetail.setText(food.getPrice());
        tvQuantityDetail1.setText("0");
    }
}