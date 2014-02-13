package com.nexters.vobble.record;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class RecordManager {
	private MediaRecorder recorder = null;

	public void startRecord(String path) {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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
		if (recorder == null) {
            return;
        }
		recorder.stop();
		recorder.release();
		recorder = null;
	}

	public void startPlay(String path, MediaPlayer.OnCompletionListener listener) {
		MediaPlayer player = new MediaPlayer();
		try {
			player.setDataSource(path);
			player.prepare();
			player.start();
            player.setOnCompletionListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

