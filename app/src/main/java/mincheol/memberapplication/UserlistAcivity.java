package mincheol.memberapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import mincheol.memberapplication.helper.DatabasehHelper;

public class UserlistAcivity extends AppCompatActivity {

    // 변수 선언
    private ListView listView;
    private DatabasehHelper databasehHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_userlist_acivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 뷰 초기화
        listView = findViewById(R.id.listView);

        //데이터베이스 헬퍼 초기화
        databasehHelper = new DatabasehHelper(this);

        // 회원목록 가져오기
        List<String> userlist = getUsersFromDatabase();

        // listView에 회원목록 표시
        ArrayAdapter<String> apater = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, userlist);

        listView.setAdapter(apater);

    }// onCreate

    // 데이터베이스에서 회원목록 가져오기
    private List<String> getUsersFromDatabase() {
        return databasehHelper.getAllUsers();
    }


} // Activity