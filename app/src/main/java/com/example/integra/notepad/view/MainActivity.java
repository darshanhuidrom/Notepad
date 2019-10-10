package com.example.integra.notepad.view;

import android.os.Bundle;
import android.util.Log;

import com.example.integra.notepad.MyApplication;
import com.example.integra.notepad.R;
import com.example.integra.notepad.model.User;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      //  insertTest();
     //   displayUsers();
     //   testUserById(3);
      //  testUserById(10);
      //  testUserByName("donald");
       // testUserByName("Modi");
       // testUserByName("Ram");
        testDeleteById(1);
        testDeleteById(1);
        displayUsers();






    }

    private void insertTest(){
        User user =new User("Nirav","modi",
                "Califonia","donald@gmail.com",
                "909909090");
        MyApplication.getDatabaseHelper().addUser(user);
    }

    private void displayUsers(){
        List<User> users=MyApplication.getDatabaseHelper().getUsers();
        for (User user:users){
            Log.d(">>>>>>>>",user.toString());
        }
    }

    private void testUserById(int id){
        String value=MyApplication.getDatabaseHelper().getUserById(id)==null?"No User with this id ="+id+"  ": MyApplication.getDatabaseHelper().getUserById(id).toString();
        Log.d(">>>>>>>>",value);
    }

    private void testUserByName(String name){
        List<User> users=MyApplication.getDatabaseHelper().getUserByName(name);
        if(users.size()!=0){
            for (User user:users){
                Log.d(">>>>>>>>",user.toString());
            }
        }
        else {
            Log.d(">>>>>>>>","No user found.");
        }
    }

    private void testDeleteById(int id){
        String msg=MyApplication.getDatabaseHelper().deleteById(id)?
                "User with "+id+" id is deleted successfully":"No user with "+id+" id found";
        Log.d(">>>>>>>>",msg);
    }


}
