package com.trannguyentanthuan2903.followshop.dto;

/**
 * Created by Administrator on 3/16/2017.
 */

public class SanPham  {
    public String IdSP;
    public String IdLoaiSP;
    public String TenSP;
    public String GiaSP;
    public String MoTaSP;
    public String HinhSP;

    public SanPham() {
    }

    public SanPham(String idSP, String idLoaiSP, String tenSP, String giaSP, String moTaSP, String hinhSP) {
        IdSP = idSP;
        IdLoaiSP = idLoaiSP;
        TenSP = tenSP;
        GiaSP = giaSP;
        MoTaSP = moTaSP;
        HinhSP = hinhSP;
    }
}
