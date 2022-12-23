package example.com.jianbing.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.jianbing.R;
import example.com.jianbing.util.MyApplication;
import example.com.jianbing.util.SharedPrefsUtils;

public class UserInfoActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.Title_one)
    TextView TitleOne;
    @BindView(R.id.image_device_one)
    ImageView imageDeviceOne;
    @BindView(R.id.realiste_one)
    RelativeLayout realisteOne;
    @BindView(R.id.Title_three)
    TextView TitleThree;
    @BindView(R.id.image_device_three)
    ImageView imageDeviceThree;
    @BindView(R.id.realiste_three)
    RelativeLayout realisteThree;

    /**
     * 个人信息页面
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        MyApplication.addActivity(this);
    }


    @OnClick({R.id.realiste_one,  R.id.realiste_three})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.realiste_one:
                //个人信息
                startActivity(new Intent(UserInfoActivity.this,PersonActivity.class));
                break;
            case R.id.realiste_three:
                //sign off
                SharedPrefsUtils.remove(UserInfoActivity.this);
                startActivity(new Intent(UserInfoActivity.this,LoginActivity.class));

                MyApplication.finishActivity();

                Toast.makeText(UserInfoActivity.this, "sign off!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
