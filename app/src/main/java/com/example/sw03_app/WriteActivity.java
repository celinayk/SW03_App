package com.example.sw03_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;



public class WriteActivity extends AppCompatActivity {

    private TextView categoryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);

        // XML 파일에서 정의한 EditText를 찾음
        categoryView = findViewById(R.id.categoryView);
        registerForContextMenu(categoryView);

        // EditText에 LongClickListener 설정



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.community_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("MenuTest", "아이디값: " + item.getItemId());
        return super.onOptionsItemSelected(item);
    }


}
