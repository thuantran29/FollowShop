package com.trannguyentanthuan2903.followshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.trannguyentanthuan2903.followshop.dto.HoaDon;
import com.trannguyentanthuan2903.followshop.R;

import java.util.List;

/**
 * Created by Administrator on 3/20/2017.
 */

public class AdapterHoaDon extends BaseAdapter {

    private Context context;
    private int layout;
    private List<HoaDon> arrayHoaDon;

    public AdapterHoaDon(Context context, int layout, List<HoaDon> arrayHoaDon) {
        this.context = context;
        this.layout = layout;
        this.arrayHoaDon = arrayHoaDon;
    }

    @Override
    public int getCount() {
        return arrayHoaDon.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayHoaDon.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        TextView txtNgayHD,txtTenKHHD,txtTenSPHD;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView= view;
        ViewHolder holder= new ViewHolder();
        if (rowView==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater.inflate(layout,null);

            holder.txtNgayHD=(TextView) rowView.findViewById(R.id.textViewNgayHoaDon);
            holder.txtTenKHHD=(TextView)rowView.findViewById(R.id.textViewTenKhHoaDon);
            holder.txtTenSPHD=(TextView) rowView.findViewById(R.id.textViewTenSPHoaDon);
            rowView.setTag(holder);
        }else {
            holder=(ViewHolder) rowView.getTag();
        }

        HoaDon hd = arrayHoaDon.get(i);
        holder.txtNgayHD.setText("Ngày lập hóa đơn: "+hd.NgayLapHoaDon);
        holder.txtTenSPHD.setText("Tên sản phẩm: "+hd.TenSpHoaDon);
        holder.txtTenKHHD.setText("Tên khách hàng: "+hd.TenKHHoaDon);

        return rowView;
    }
}
