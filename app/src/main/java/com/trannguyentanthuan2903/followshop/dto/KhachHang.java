package com.trannguyentanthuan2903.followshop.dto;

/**
 * Created by Administrator on 3/15/2017.
 */

public class KhachHang {
    public String MaKH;
    public String HoTenKH;
    public String SDTKH;
    public String DiaChi;
    public String HinhAnhKH;

    public KhachHang() {
    }

    public KhachHang(String maKH, String hoTenKH, String SDTKH, String diaChi, String hinhAnhKH) {
        MaKH = maKH;
        HoTenKH = hoTenKH;
        this.SDTKH = SDTKH;
        DiaChi = diaChi;
        HinhAnhKH = hinhAnhKH;
    }
}
