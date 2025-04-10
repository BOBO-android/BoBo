package android.example.bobo.ui.view.Fragments;

import android.example.bobo.ui.adapters.CurrentOrdersAdapter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.example.bobo.R;
import android.example.bobo.data.model.Order;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

public class CurrentOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private CurrentOrdersAdapter adapter;
    private List<Order> orderList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_current_orders, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        orderList.add(new Order("1","Pepperoni Cheese Pizza", "30 mins", "$24.02", R.drawable.pizza));

        adapter = new CurrentOrdersAdapter(getActivity() ,orderList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
