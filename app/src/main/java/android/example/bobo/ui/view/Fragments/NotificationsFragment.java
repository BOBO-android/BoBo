package android.example.bobo.ui.view.Fragments;

import android.example.bobo.R;
import android.example.bobo.data.model.NotificationItem;
import android.example.bobo.ui.adapters.NotificationAdapter;
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

public class NotificationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationItem> notificationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tạo danh sách thông báo
        notificationList = new ArrayList<>();
        notificationList.add(new NotificationItem(R.drawable.ic_bobo, "Order Out for Delivery!", "Your food is on the move!", "5 mins ago"));
        notificationList.add(new NotificationItem(R.drawable.ic_bobo, "Your Order is Confirmed!", "Thanks for ordering! Your meal is being prepared.", "22 mins ago"));

        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
