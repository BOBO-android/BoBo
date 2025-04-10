package android.example.bobo.ui.view.Fragments;

import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.example.bobo.ui.adapters.PreviousOrdersAdapter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PreviousOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private PreviousOrdersAdapter adapter;
    private List<Order> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_previous_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        orderList.add(new Order("2","Pudding x1", "26 October", "$16.98", R.drawable.pudding));
        orderList.add(new Order("3","Honey Bee Pineapple Cake x1", "20 October", "$26.99", R.drawable.cake));
        orderList.add(new Order("4","Classic Beef Burger x1", "16 October", "$18.97", R.drawable.burger));

        adapter = new PreviousOrdersAdapter(orderList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
