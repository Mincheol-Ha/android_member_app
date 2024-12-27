package mincheol.memberapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import mincheol.memberapplication.helper.DatabasehHelper;
import mincheol.memberapplication.helper.MariaDBHelper;

public class MainActivity extends AppCompatActivity {

    // 변수 선언
    private EditText editTextUserid, editTextPasswd, editTextName, editTextEmail;
    private Button buttonJoin, buttonUserlist;
  //  private DatabasehHelper databasehHelper;
    private MariaDBHelper mariaDBHelper;

    // SharePreferences : 경량 데이터를 저장하기 위한 내부 객체
    // 데이터는 보통 키-값 형태로 앱의 내부 저장소에 저장
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

      // 뷰 초기화
      editTextUserid = findViewById(R.id.editTextUserid);
      editTextPasswd = findViewById(R.id.editTextPassword);
      editTextName = findViewById(R.id.editTextName);
      editTextEmail = findViewById(R.id.editTextEmail);
      buttonJoin = findViewById(R.id.buttonJoin);
      buttonUserlist = findViewById(R.id.buttonUserlist);
      
      // 데이터베이스 헬퍼 초기화
      // MODE_PRIVATE : 특정 앱만 접급 가능하도록 설정
     // databasehHelper = new DatabasehHelper(this);
        mariaDBHelper = new MariaDBHelper();

      // sharePreferences 초기화
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        // 회원가입 이벤트 처리
        //  main액티비티에서 네트워크 작업 시도시
        // NetworkOnMainThreadException 발생!
        // 즉, 메인쓰레드에서 네트워크 작업은 기본적으로 금지되어 있음
        // 특정 액티비티가 네트워크를 독점적으로 점유하는 것을 방지
        // 비동기 처리를 이용해서 토드를 작성해야 함
        // >> AsyncTasl, ExecutorService, Coroutines (코틀린-추천!)
      buttonJoin.setOnClickListener(
              new View.OnClickListener()   {
                  @Override
                  public void onClick(View v) {
                      registerUser();
                  }
              }
      );

      // 회원조회 이벤트 처리
      // 로그인되어 있다면 - UserListActivity로 이동
      // 로그인되지 않았다면 - LoginActivity로 이동
      buttonUserlist.setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  // 로그인 관련 변수 가져오기
                  // getBoolean (키, 기본값)
                  boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

                  if(isLoggedIn) { // 로그인 했다면  UserListActivity를 뷰에 표시
                      Intent intent = new Intent(MainActivity.this, UserlistAcivity.class);
                      startActivity(intent);
                    //  Toast.makeText(MainActivity.this, "UserlistActivity 표시", Toast.LENGTH_SHORT).show();
                  } else {
                      Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                      startActivity(intent);
                 //     Toast.makeText(MainActivity.this, "UserlistActivity 표시", Toast.LENGTH_SHORT).show();
              }
          }
          }
      );
      
    }

    private void registerUser() {
        // 변수 초기화
        String userid = editTextUserid.getText().toString().trim();
        String passwd = editTextPasswd.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String name = editTextName.getText().toString().trim();


        // 입력값 검증
        if(userid.isEmpty() || passwd.isEmpty() || name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "모든 필드를 입력하세요!", Toast.LENGTH_SHORT).show();
            return; //여기서 중지
        }

        // 중복 아이디 확인

      //  if (databasehHelper.useridCheck(userid)) {
        if (mariaDBHelper.useridCheck(userid)) {
            Toast.makeText(this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
            return; //여기서 중지

        }


        // 회원 저장
       // boolean success = databasehHelper.insertMember(userid, passwd, name, email);
        boolean success = mariaDBHelper.insertMember(userid, passwd, name, email);
        if(success) {
            Toast.makeText(this, "✨회원 가입 성공!✨", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "😱회원 가입 실패!! 다시 시도하세요!!😱", Toast.LENGTH_SHORT).show();
        }
    }
    // AsyncTask 처리를 위한 비동기 처리 클래스
    private class RegisterUserTask extends AsyncTask<String, Void, Boolean> {

        // doInBackground
        // 시간이 오래 걸리는 작업 수행에 사용
        // 네트워크, 데이터베이스, 파일처리등의 작업 - UI 스레드가 차단
        @Override
        protected Boolean doInBackground(String... params) {
            String userid = params[0];
            String passwd = params[1];
            String name = params[2];
            String email = params[3];

            try {
                // 중복 아이디 체크
                if (mariaDBHelper.useridCheck(userid)) {
                    return null;
                }
                // 회원 정보 저장
                return mariaDBHelper.insertMember(userid, passwd, name, email);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return false;
        }

        // onPostExecute
        // doInBackground의 결과를 수신해서 UI에 반영할때 사용


        @Override
        protected void onPostExecute(Boolean success) {
            if (success == null) { // 아이디가 중복이면
                Toast.makeText(MainActivity.this, "이미 사용중인 아이디입니다.", Toast.LENGTH_SHORT).show();
            } else if (success) { // 회원정보가 정상적으로 저장되었다면
                Toast.makeText(MainActivity.this, "✨회원 가입 성공!✨", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "😱회원 가입 실패!! 다시 시도하세요!!😱", Toast.LENGTH_SHORT).show();
            }
        }

    }
}













