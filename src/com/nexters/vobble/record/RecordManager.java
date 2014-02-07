package com.nexters.vobble.record;

import java.io.*;

import android.media.*;
import android.os.*;

public class RecordManager {
	private MediaRecorder recorder = null;
	private String filePath, fileName = "";
	private boolean isRecorded = false;
	private String path = "";

	public void startRecord(String path) {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		this.path = path;
		
		if (recorder == null) {
			recorder = new MediaRecorder();
		} else {
			recorder.reset();
		}
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		recorder.setOutputFile(path);
		try {
			recorder.prepare();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		recorder.start();
	}

	public void stopRecord() {
		if (recorder == null)
			return;
		recorder.stop();
		recorder.release();
		recorder = null;
	}

	public void playRecord(String path) {
		
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

