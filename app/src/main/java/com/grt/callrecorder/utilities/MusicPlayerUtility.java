package com.grt.callrecorder.utilities;


public class MusicPlayerUtility {
	

	public String milliSecondsToTimer(long milliseconds){
		String finalTimer = "";
		String secs = "";
		
		   int hours = (int)( milliseconds / (1000*60*60));
		   int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
		   int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
		
		   if(hours > 0){
			   finalTimer = hours + ":";
		   }
		   
		   if(seconds < 10){ 
			   secs = "0" + seconds;
		   }else{
			   secs = "" + seconds;}
		   
		   finalTimer = finalTimer + minutes + ":" + secs;
		
		return finalTimer;
	}
	
	public int getProgressPercentage(long currentDuration, long totalDuration){
		Double percentage = (double) 0;
		
		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);
		
		percentage =(((double)currentSeconds)/totalSeconds)*100;
		
		return percentage.intValue();
	}

	public int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double)progress) / 100) * totalDuration);
		
		return currentDuration * 1000;
	}
}
