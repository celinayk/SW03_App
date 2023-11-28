package com.example.sw03_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;

public class WritePost extends AppCompatActivity {
    private Uri uri;
    Button done_button;
    ImageButton add_image_button;
    ImageView imageView;
    FloatingActionButton out_button;

    private String[] categoryTexts;
    private int[] categoryNumbers;
    private int selectedCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_post);

        done_button = (Button) findViewById(R.id.done_button);
        add_image_button = (ImageButton) findViewById(R.id.add_image_button);
        imageView = (ImageView) findViewById(R.id.imageView);
        out_button = (FloatingActionButton) findViewById(R.id.outButton);
        Spinner category = findViewById(R.id.category);

        categoryTexts = getResources().getStringArray(R.array.category_texts);//스피너의 내용
        categoryNumbers = getResources().getIntArray(R.array.category_numbers);//스피너의 숫자값


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.category_texts,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(adapter);

        //스피너에서 클릭하면 작동
        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCategory = categoryNumbers[position];
                //selectedCategory가 카테고리의 데이터(1=자유게시판, 2=분실물,3=건의사항)
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택되지 않았을 때 호출됩니다.
                // 필요에 따라 처리할 내용을 추가할 수 있습니다.
            }
        });


        //X버튼 클릭->view_community로
        out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // MainActivity로 이동할 때 선택된 아이템을 전달
                intent.putExtra("selectedItemId", R.id.board);
                startActivity(intent);
                finish();
            }
        });

        //완료 버튼 클릭->글올라가기
        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //제목,카테고리,내용,작성자,작성일시간 등의 정보가 서버에 전달되고 데이터에 저장되어야함.
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                // MainActivity로 이동할 때 선택된 아이템을 전달
                intent.putExtra("selectedItemId", R.id.board);
                startActivity(intent);
                finish();
            }
        });



        add_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);


            }
        });
    }

    // 갤러리에서 이미지를 선택한 결과를 받기 위한 메서드
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            // 선택한 이미지의 URI 가져오기
            Uri selectedImageUri = data.getData();

            // 이미지뷰에 선택한 이미지 표시
            imageView.setImageURI(selectedImageUri);
        }
    }


}
