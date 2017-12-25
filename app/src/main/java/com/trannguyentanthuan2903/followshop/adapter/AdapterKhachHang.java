package com.trannguyentanthuan2903.followshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.KhachHang;
import com.trannguyentanthuan2903.followshop.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 3/15/2017.
 */

public class AdapterKhachHang extends BaseAdapter {

    private Context context;
    private int layout;
    private List<KhachHang> arrayKhachHang;
    private ArrayList<KhachHang> arrayList;

    public AdapterKhachHang(Context context, int layout, List<KhachHang> arrayKhachHang) {
        this.context = context;
        this.layout = layout;
        this.arrayKhachHang = arrayKhachHang;

        this.arrayList = new ArrayList<KhachHang>();
        this.arrayList.addAll(arrayKhachHang);
    }

    @Override
    public int getCount() {
        return arrayKhachHang.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayKhachHang.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public class ViewHolder{
        TextView txtHotenKh,txtSdtKh;
        ImageView imgHinhKh;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView=view;
        ViewHolder holder= new ViewHolder();

        if (rowView==null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater.inflate(layout,null);
            holder.txtHotenKh=(TextView) rowView.findViewById(R.id.textViewTenKh);
            holder.txtSdtKh=(TextView) rowView.findViewById(R.id.textViewSdtKh);
            holder.imgHinhKh=(ImageView) rowView.findViewById(R.id.imageViewHinhKh);
            rowView.setTag(holder);
        }else {
            holder=(ViewHolder) rowView.getTag();
        }

        KhachHang khachHang=arrayKhachHang.get(i);

        holder.txtHotenKh.setText(khachHang.HoTenKH);
        holder.txtSdtKh.setText(khachHang.SDTKH);
        Picasso.with(context)
                .load(khachHang.HinhAnhKH)
                .placeholder(R.drawable.product_icon)
                .into(holder.imgHinhKh);

        return rowView;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arrayKhachHang.clear();
        if (charText.length() == 0) {// offline tìm ra dc, còn online thi dợi add lại
            arrayKhachHang.addAll(arrayList);
        } else {
            for (KhachHang khachHang : arrayList) {
                if (khachHang.HoTenKH.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayKhachHang.add(khachHang);
                }
            }
        }
        notifyDataSetChanged();
    }
}
