package ru.studenetskiy.code;

public class Mail {
	String date;
	String adress;
	String text;
	
	Mail(String date,String adress,String text){
		this.date=date;
		this.adress=adress;
		this.text=text;
	}
	
	String formatForSend() {
		return date+" / "+text+ "\n";
	}
	
}
