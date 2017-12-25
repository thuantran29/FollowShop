package com.trannguyentanthuan2903.followshop.dto;

/**
 * Created by Administrator on 3/17/2017.
 */

public class HoaDon  {
    public String IdHoaDon;
    public String NgayLapHoaDon;
    public String TenKHHoaDon;
    public String TenSpHoaDon;
    public String Soluong;
    public String GiaSPHoaDon;
    public String LinkHinh;

    public HoaDon() {
    }

    public HoaDon(String idHoaDon, String ngayLapHoaDon,
                  String tenKHHoaDon, String tenSpHoaDon, String soluong, String giaSPHoaDon, String linkHinh) {
        IdHoaDon = idHoaDon;
        NgayLapHoaDon = ngayLapHoaDon;
        TenKHHoaDon = tenKHHoaDon;
        TenSpHoaDon = tenSpHoaDon;
        Soluong = soluong;
        GiaSPHoaDon = giaSPHoaDon;
        LinkHinh = linkHinh;
    }
}
