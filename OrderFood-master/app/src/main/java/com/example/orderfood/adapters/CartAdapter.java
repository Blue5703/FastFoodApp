package com.example.orderfood.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.orderfood.R;
import com.example.orderfood.models.Cart;
import com.example.orderfood.models.database.CartRepository;
import com.example.orderfood.models.database.DBHelper;
import com.example.orderfood.models.database.FoodRepository;
import com.example.orderfood.uis.CartFragment;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<Cart> list;
    Context context;
    DBHelper dbHelper;
    FoodRepository foodRepository;
    CartRepository cartRepository;
    CartFragment cartFragment;

    public CartAdapter(List<Cart> list, Context context) {
        this.list = list;
        this.context = context;
        dbHelper=new DBHelper(context);
        foodRepository=new FoodRepository(dbHelper);
        cartRepository=new CartRepository(dbHelper,context);
    }
    public CartAdapter(List<Cart> list, Context context, CartFragment cartFragment) {
        this.list = list;
        this.context = context;
        this.cartFragment = cartFragment;
        dbHelper=new DBHelper(context);
        foodRepository=new FoodRepository(dbHelper);
        cartRepository=new CartRepository(dbHelper,context);
    }


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_cart,parent,false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Cart cart=list.get(position);
        Glide.with(context).load(foodRepository.getFoodByFoodId(cart.getFoodId()).getImage()).into(holder.imgAnhInCart);
        holder.tvNameInCart.setText(foodRepository.getFoodByFoodId(cart.getFoodId()).getName());
        int a = Integer.parseInt(foodRepository.getFoodByFoodId(cart.getFoodId()).getPrice());
        holder.tvQuantity.setText(String.valueOf(cart.getQuantity()));
        holder.tvPriceInCart.setText(foodRepository.getFoodByFoodId(cart.getFoodId()).getPrice());
        int TotalPrice = a * cart.getQuantity();
        holder.tvPriceInCartTotal.setText(TotalPrice + " VNĐ");
        holder.imgCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newQuantity = Integer.parseInt(holder.tvQuantity.getText().toString()) + 1;
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                cart.setQuantity(newQuantity);
                cartRepository.updateCart(cart,cart.getFoodId());
                int newTotalPrice = a * newQuantity;
                holder.tvPriceInCartTotal.setText(newTotalPrice + " VNĐ");
                cartFragment.updateTotalPrices();
            }
        });
        holder.imgTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(holder.tvQuantity.getText().toString())==1){
                    cartRepository.deleteCart(list.get(position).getFoodId());
                    list.remove(position);
                    notifyDataSetChanged();
                }
                else{
                    int newQuantity = Integer.parseInt(holder.tvQuantity.getText().toString()) - 1;
                    holder.tvQuantity.setText(String.valueOf(newQuantity));
                    cart.setQuantity(newQuantity);
                    cartRepository.updateCart(cart,cart.getFoodId());
                    int newTotalPrice = a*newQuantity;
                    holder.tvPriceInCartTotal.setText(newTotalPrice + " VNĐ");
                }
                cartFragment.updateTotalPrices();
            }
        });

    }

    @Override
    public int getItemCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        ImageView imgAnhInCart,imgBack;
        TextView tvNameInCart,tvPriceInCart, tvPriceInCartTotal;
        TextView tvQuantity, imgCong, imgTru;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAnhInCart=itemView.findViewById(R.id.imgInCart);
            tvPriceInCartTotal=itemView.findViewById(R.id.tvPriceInCartTotal);
            tvNameInCart=itemView.findViewById(R.id.tvNameInCart);
            tvQuantity=itemView.findViewById(R.id.tvQuantity);
            imgCong=itemView.findViewById(R.id.imgCong);
            imgTru=itemView.findViewById(R.id.imgTru);
            tvPriceInCart=itemView.findViewById(R.id.tvPriceInCart);
        }
    }
}
