package android.jcgf.com.wifip2pdemo.activity;

import android.content.Context;
import android.jcgf.com.wifip2pdemo.R;
import android.net.wifi.p2p.WifiP2pDevice;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wenbaohe on 2018/9/20.
 */

public class ItemAdapter extends BaseAdapter {

    private Context context;
    private List<WifiP2pDevice> p2pDevices;

    public ItemAdapter(Context context, List<WifiP2pDevice> p2pDevices) {
        this.context = context;
        this.p2pDevices = p2pDevices;
    }

    @Override
    public int getCount() {
        return p2pDevices.size();
    }

    @Override
    public Object getItem(int position) {
        return p2pDevices.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText("设备名称：" + (TextUtils.isEmpty(p2pDevices.get(position).deviceName) ? "" : p2pDevices.get(position).deviceName));
        viewHolder.info.setText("设备信息：\n" + p2pDevices.get(position).toString());
        return convertView;
    }

    protected class ViewHolder {
        public TextView name;
        public TextView info;

        public ViewHolder(View conview) {
            this.name = conview.findViewById(R.id.tv_name);
            this.info = conview.findViewById(R.id.tv_info);
        }
    }
}
