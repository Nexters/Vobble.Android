package com.nexters.vobble.record;

import java.io.*;

import android.media.*;

public class RecordManager {
	private MediaRecorder recorder = null;
	private MediaRecorder mRecorder = null;
	private String filePath, fileName = "";
	private boolean isRecorded = false;

	private String path = "";
	public RecordManager() {
	}

	public void start() {
		isRecorded = true;
		if (recorder == null)
			recorder = new MediaRecorder();
		recorder.reset();
		recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
		recorder.setOutputFile(filePath + fileName);
		try {
			recorder.prepare();
		} catch (Exception e) {
			e.printStackTrace();
			isRecorded = false;
		}
		recorder.start();
	}

	public void startRecord(String path) {
		this.path = path;
		
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
		} else {
			mRecorder.reset();
		}
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setOutputFile(path);
		try {
			mRecorder.prepare();
		} catch (IllegalStateException e) {
		} catch (IOException e) {
		}
		mRecorder.start();
	}

	public void stop() {
		if (recorder == null)
			return;
		try {
			recorder.stop();
		} catch (Exception e) {
		} finally {
			recorder.release();
			recorder = null;
			isRecorded = false;
		}
	}

	public void stopRecord() {
		if (mRecorder == null)
			return;
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	public void playRecord(String path) {
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(path);
			player.prepare();
			player.start();
		} catch (Exception e) {
		}
	}

	public boolean getRecorded() {
		return isRecorded;
	}

	public void setRecorded(boolean recorded) {
		recorded = recorded;
	}
}

