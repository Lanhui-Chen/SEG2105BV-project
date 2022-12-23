package example.com.jianbing.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.jianbing.R;
import example.com.jianbing.adapter.JiQiAdapter;
import example.com.jianbing.adapter.UserAdapter;
import example.com.jianbing.bean.EventBusBean;
import example.com.jianbing.bean.UserBean;
import example.com.jianbing.db.DbSqliteHelper;
import example.com.jianbing.util.MyApplication;
import example.com.jianbing.util.SharedPrefsUtils;

public class UserActivity extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.line_bar)
    LinearLayout lineBar;
    @BindView(R.id.follow_edit)
    EditText followEdit;
    @BindView(R.id.search_image)
    ImageView searchImage;
    @BindView(R.id.line)
    LinearLayout line;
    @BindView(R.id.listview)
    ListView mlistview;
    private List<UserBean> userBeanList;
    private UserAdapter userAdapter;

    /**
     * supervisor获取获取所有user information
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        MyApplication.addActivity(this);
        title.setText("user information");
        if (!EventBus.getDefault().isRegistered(this)) {//加上判断
            EventBus.getDefault().register(this);
        }
        userBeanList = DbSqliteHelper.getInstance(UserActivity.this).getAllUser();
        userAdapter = new UserAdapter(UserActivity.this, userBeanList);
        mlistview.setAdapter(userAdapter);
        userAdapter.notifyDataSetChanged();
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //点击进入页面
                Intent intent = new Intent(UserActivity.this, PersonActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", userBeanList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

        //长按deleteuser
        mlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {

                final String[] items = {"delete"};
                new AlertDialog.Builder(UserActivity.this)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        //确认对话框
                                        new AlertDialog.Builder(UserActivity.this)
                                                .setMessage("delete confirmation？")
                                                .setPositiveButton("confirm", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //delete
                                                        if (SharedPrefsUtils.getString(UserActivity.this,"user").equals(userBeanList.get(position).getNickName())){
                                                            Toast.makeText(UserActivity.this,"supervisor cannot delete themselves",Toast.LENGTH_SHORT).show();
                                                        }else {

                                                            DbSqliteHelper.getInstance(UserActivity.this).deleteUser(userBeanList.get(position).getId());
                                                            //刷新列表
                                                            userBeanList = DbSqliteHelper.getInstance(UserActivity.this).getAllUser();
                                                            userAdapter.updateAll(userBeanList);
                                                        }

                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .show();
                                        break;
                                }
                            }
                        })
                        .show();
                return true;
            }
        });
    }
    @Subscribe
    public void onEventMainThread(EventBusBean event) {

        if (event.getTag().equals("3")) {
            //刷新列表
            userBeanList = DbSqliteHelper.getInstance(UserActivity.this).getAllUser();
            userAdapter.updateAll(userBeanList);

        }
    }
    @OnClick({R.id.back, R.id.search_image})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                //返回
                finish();
                break;
            case R.id.search_image:
                //搜索
                if (TextUtils.isEmpty(followEdit.getText().toString())) {
                    Toast.makeText(UserActivity.this, "请输入user名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //模糊搜索数据
                userBeanList = DbSqliteHelper.getInstance(UserActivity.this).getuser(followEdit.getText().toString());
                userAdapter.updateAll(userBeanList);
                userAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))//加上判断
            EventBus.getDefault().unregister(this);
    }
}
