package com.navigps.models;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
	public static final String USER_KEY = "user_key";
	private int userId;
	private String userName;
	private String userPassword;
	private boolean isAdmin = false;

	public User() {

	}

	public User(Parcel parcel) {
		readFromParcel(parcel);
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setAdmin(boolean admin) {
		isAdmin = admin;
	}

	public int getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel parcel, int i) {
		parcel.writeInt(userId);
		parcel.writeString(userName);
		parcel.writeString(userPassword);
		parcel.writeString(isAdmin ? "T" : "F");

	}

	private void readFromParcel(Parcel parcel) {
		userId = parcel.readInt();
		userName = parcel.readString();
		userPassword = parcel.readString();
		isAdmin = parcel.readString().equals("T") ? true : false;

	}

	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

}
