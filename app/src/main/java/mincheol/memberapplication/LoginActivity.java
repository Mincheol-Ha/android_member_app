package mincheol.memberapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mincheol.memberapplication.helper.DatabasehHelper;

public class LoginActivity extends AppCompatActivity {

    // 변수 선언
    private EditText editTextUserid, editTextPasswd;
    private Button buttonLogin;
    private DatabasehHelper databasehHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 변수 초기화
            editTextUserid = findViewById(R.id.editTextUserid);
            editTextPasswd = findViewById(R.id.editTextPasswd);
            buttonLogin = findViewById(R.id.buttonLogin);
            databasehHelper = new DatabasehHelper(this);
            sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);


            // 로그인 이벤트 처리
            buttonLogin.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //입력한 아이디/비밀번호를 가져온
                            String userid = editTextUserid.getText().toString();
                            String passwd = editTextPasswd.getText().toString();
                            // 로그인 성공 여부에 따른 처리
                            if (databasehHelper.loginUser(userid, passwd)) {
                                // 로그인 성공시  sharedPreferences에 세션 데이터 저장
                                // SharedPreferences 수정을 위한 초기화
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.apply(); // 추가한 내용 저장

                                Intent intent = new Intent(LoginActivity.this, UserlistAcivity.class);
                                startActivity(intent);

                            } else {
                                Toast.makeText(LoginActivity.this, "로그인 실패! 아이디/비밀번호를 확인하세요!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );

        }
    }
