package com.nexters.vobble.record;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class RecordManager {
	private MediaRecorder recorder = null;
    private MediaPlayer player = null;

    public RecordManager() {
        recorder = new MediaRecorder();
        player = new MediaPlayer();
    }

	public void startRecord(String path) {
		File file = new File(path);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

        recorder = new MediaRecorder();
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

    public void startPlay(String path) {
        startPlay(path, null);
    }

	public void startPlay(String path, MediaPlayer.OnCompletionListener listener) {
        try {
            player = new MediaPlayer();
            player.setDataSource(path);
			player.prepare();
			player.start();
            if (listener != null) {
                player.setOnCompletionListener(listener);
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public int getDurationOfCurrentMedia() {
        if (player == null) {
            return 0;
        } else {
            return player.getDuration();
        }
    }

    public void stopPlay() {
        if (player == null) {
            return;
        } else if (!player.isPlaying()) {
            return;
        }

        player.stop();
        player.release();
        player = null;
    }
}

