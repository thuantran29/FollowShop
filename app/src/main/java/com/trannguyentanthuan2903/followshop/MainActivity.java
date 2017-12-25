package com.trannguyentanthuan2903.followshop;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trannguyentanthuan2903.followshop.adapter.AdapterKhachHang;
import com.trannguyentanthuan2903.followshop.fragment.FragmentHonDon;
import com.trannguyentanthuan2903.followshop.fragment.FragmentKhachHang;
import com.trannguyentanthuan2903.followshop.fragment.FragmentSanPham;

public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    AdapterKhachHang adapter;

    TabLayout tabLayout;
    ViewPager viewPager;
    boolean doubleBack = false;

    DatabaseReference mData;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        key = mUser.getUid();

        AnhXa();
        // khai báo adapter
        ViewAdapterFragment adapter = new ViewAdapterFragment(getSupportFragmentManager());

        adapter.addFragment(new FragmentKhachHang(), "Khách hàng");
        adapter.addFragment(new FragmentSanPham(), "Sản Phẩm");
        adapter.addFragment(new FragmentHonDon(), "Hóa Đơn");
        viewPager.setAdapter(adapter);
        //chuyển viewpager qua tablayout
//        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        //set icon cho tablayout
        tabLayout.getTabAt(0).setIcon(R.drawable.icon_khachhang);
        tabLayout.getTabAt(1).setIcon(R.drawable.icon_sanpham);
        tabLayout.getTabAt(2).setIcon(R.drawable.hoadon);

        //set cho toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        //navigation hien hinh anh
        navigationView.setItemIconTintList(null);
        //chọn các id thuc hiện các lệnh
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menuKhachHang:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menuSanPham:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menuHoaDon:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.menuPassword:
                        startActivity(new Intent(MainActivity.this,DoiMatKhauActivity.class));
                        break;
                    case R.id.menuClose:
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        sendKeyNguoiDung();
    }

    private void sendKeyNguoiDung() {
        SharedPreferences.Editor editgv = getSharedPreferences("nguoi_dung", MODE_PRIVATE).edit();
        editgv.putString("key_nguoi_dung", key);
        editgv.commit();
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            super.onBackPressed();
            return;
        }

        this.doubleBack = true;
        Toast.makeText(this, "BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }

    private void AnhXa() {

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolbar = (Toolbar) findViewById(R.id.mToolbar);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.tab_viewpager);
    }
}