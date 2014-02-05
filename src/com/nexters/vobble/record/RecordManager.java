package com.nexters.vobble.record;

import java.io.IOException;

import com.nexters.vobble.RecordActivity;
import com.nexters.vobble.view.VobbleWidget;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Toast;

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
		// /

		// /
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

	public void vobblePlay(String path, final VobbleWidget widget) {
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource("http://14.63.185.152:3000/download?fileName="
					+ path);
			player.prepare();
			player.start();
			widget.getClickVobbleImageView().setVisibility(View.VISIBLE);
			
		} catch (Exception e) {
		}
		
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer arg0) {
				widget.getClickVobbleImageView().setVisibility(View.INVISIBLE);
				
			}
		});
	}

	public boolean getRecorded() {
		return isRecorded;
	}

	public void setRecorded(boolean recorded) {
		recorded = recorded;
	}
}