package com.example.logowanie;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.logowanie.services.UsersService;

public class SecondActivity extends Activity 
{
public String lo;
public String ha;
 @Override
 public void onCreate(Bundle savedInstanceState)
 {
   super.onCreate(savedInstanceState);
   Toast dymek = Toast.makeText(this, "Hello " + (UsersService.getInstance().isUserAdmin() ? "beloved Admin":UsersService.getInstance().getUser().getUserName()), Toast.LENGTH_SHORT);
   dymek.show();

 }
}