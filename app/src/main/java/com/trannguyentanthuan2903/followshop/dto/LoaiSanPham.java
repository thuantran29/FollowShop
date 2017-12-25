package com.trannguyentanthuan2903.followshop.dto;

/**
 * Created by Administrator on 3/16/2017.
 */

public class LoaiSanPham {
    public String MaLoai;
    public String IdLoai;
    public String TenLoai;
    public String HinhLoai;

    public LoaiSanPham() {
    }

    public LoaiSanPham(String maLoai, String idLoai, String tenLoai, String hinhLoai) {
        MaLoai = maLoai;
        IdLoai = idLoai;
        TenLoai = tenLoai;
        HinhLoai = hinhLoai;
    }
}
