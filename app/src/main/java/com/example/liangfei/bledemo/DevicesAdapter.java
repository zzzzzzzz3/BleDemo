package com.example.liangfei.bledemo;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.DeviceHolder> {

    private List<BluetoothDevice> deviceList;
    private Context context;
    private OnConnectListener onConnectListener;

    public DevicesAdapter(Context context) {
        deviceList = new ArrayList<>();
        this.context = context;
    }

    public void setOnConnectListener(OnConnectListener onConnectListener) {
        this.onConnectListener = onConnectListener;
    }

    public void addDevice(BluetoothDevice device){
        if(!deviceList.contains(device)){
            deviceList.add(device);
            notifyDataSetChanged();
        }
    }

    public void clearDevices(){
        deviceList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DeviceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.device_item,parent,false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceHolder holder, int position) {
        final BluetoothDevice device = deviceList.get(position);
        holder.name.setText(device.getName()==null?"unknow":device.getName());
        holder.uuid.setText(device.getAddress());
        holder.connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConnectListener.onConnect(device);
            }
        });
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView uuid;
        Button connect;

        DeviceHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.device_name);
            uuid = itemView.findViewById(R.id.device_uuid);
            connect = itemView.findViewById(R.id.connect_device);
        }
    }

    public interface OnConnectListener{
        void onConnect(BluetoothDevice device);
    }
}
