package com.trannguyentanthuan2903.followshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.R;
import com.trannguyentanthuan2903.followshop.dto.SanPham;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 3/16/2017.
 */

public class AdapterSanPham extends BaseAdapter {

    private Context context;
    private int layout;
    private List<SanPham> arraySanPham;
    private ArrayList<SanPham> arrayList;
    public AdapterSanPham(Context context, int layout, List<SanPham> arraySanPham) {
        this.context = context;
        this.layout = layout;
        this.arraySanPham = arraySanPham;

        this.arrayList = new ArrayList<SanPham>();
        this.arrayList.addAll(arraySanPham);
    }

    @Override
    public int getCount() {
        return arraySanPham.size();
    }

    @Override
    public Object getItem(int i) {
        return arraySanPham.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    private class ViewHolder{
        TextView txtTen, txtGia;
        ImageView imgHinh;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView =view;
        ViewHolder holder=new ViewHolder();
        if (rowView==null){
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater.inflate(layout,null);
            holder.txtTen=(TextView) rowView.findViewById(R.id.textviewTenSanPham);
            holder.txtGia=(TextView) rowView.findViewById(R.id.textviewGiaSanPham);
            holder.imgHinh=(ImageView) rowView.findViewById(R.id.imageviewHinhSanPham);
            rowView.setTag(holder);
        }else {
            holder= (ViewHolder) rowView.getTag();
        }

        SanPham sp=arraySanPham.get(i);

        holder.txtTen.setText(sp.TenSP);
        holder.txtGia.setText(sp.GiaSP);
        Picasso.with(context).load(sp.HinhSP).placeholder(R.drawable.product_icon).error(R.drawable.error).into(holder.imgHinh);
        return rowView;
    }

    //search
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        arraySanPham.clear();
        if (charText.length() == 0) {// offline tìm ra dc, còn online thi dợi add lại
            arraySanPham.addAll(arrayList);
        } else {
            for (SanPham sanPham : arrayList) {
                if (sanPham.TenSP.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arraySanPham.add(sanPham);
                }
            }
        }
        notifyDataSetChanged();
    }
}
