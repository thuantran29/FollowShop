package com.trannguyentanthuan2903.followshop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.trannguyentanthuan2903.followshop.dto.LoaiSanPham;
import com.trannguyentanthuan2903.followshop.R;

import java.util.List;

/**
 * Created by Administrator on 3/16/2017.
 */

public class AdapterLoaiSanPham extends BaseAdapter {

    private Context context;
    private int layout;
    private List<LoaiSanPham> loaiSanPhams;

    public AdapterLoaiSanPham(Context context, int layout, List<LoaiSanPham> loaiSanPhams) {
        this.context = context;
        this.layout = layout;
        this.loaiSanPhams = loaiSanPhams;
    }

    @Override
    public int getCount() {
        return loaiSanPhams.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder {
        ImageView imgHinh;
        TextView txtTen;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            holder.txtTen = (TextView) view.findViewById(R.id.textviewLoaiSanPham);
            holder.imgHinh = (ImageView) view.findViewById(R.id.imageviewHinhLoaiSP);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        LoaiSanPham loaiSanPham = loaiSanPhams.get(i);

        holder.txtTen.setText(loaiSanPham.TenLoai);
        Picasso.with(context)
                .load(loaiSanPham.HinhLoai)
                .placeholder(R.drawable.product_icon)
                .error(R.drawable.product_icon)
                .into(holder.imgHinh);

        return view;
    }
}
