package com.nexters.vobble.record;

import java.io.File;
import java.io.IOException;

import android.media.MediaPlayer;
import android.media.MediaRecorder;

public class RecordManager {
    public static final int READY_MODE = 0;
    public static final int RECORDING_MODE = 1;
    public static final int STOP_MODE = 2;
    public static final int PLAYING_MODE = 3;

    private int mCurrentRecordingMode = READY_MODE;

    private MediaRecorder recorder;
    private MediaPlayer player;

	public void startRecording(String path) {
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
            recorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
            setRecordingMode(RecordManager.RECORDING_MODE);
        }
	}

	public void stopRecording() {
		if (recorder == null)
            return;

        try {
            recorder.stop();
            recorder.reset();
            recorder.release();
            recorder = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            setRecordingMode(RecordManager.STOP_MODE);
        }
	}

    public void startPlaying(String path) {
        startPlaying(path, null);
    }

	public void startPlaying(String path, MediaPlayer.OnCompletionListener listener) {
        try {
            player = new MediaPlayer();
            player.setDataSource(path);
			player.prepare();
			player.start();
            if (listener != null)
                player.setOnCompletionListener(listener);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            setRecordingMode(RecordManager.PLAYING_MODE);
        }
	}

    public void stopPlaying() {
        if (player == null)
            return;

        try {
            player.stop();
            player.reset();
            player.release();
            player = null;
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } finally {
            setRecordingMode(RecordManager.STOP_MODE);
        }
    }

    public int getDurationOfCurrentMedia() {
        if (player == null) {
            return 0;
        } else {
            return player.getDuration();
        }
    }

    public void resetRecording() {
        setRecordingMode(RecordManager.READY_MODE);
    }

    public boolean isPlaying() {
        return mCurrentRecordingMode == RecordManager.PLAYING_MODE;
    }

    public boolean isRecording() {
        return mCurrentRecordingMode == RecordManager.RECORDING_MODE;
    }

    public boolean isReadyToRecording() {
        return mCurrentRecordingMode == RecordManager.READY_MODE;
    }

    public boolean isStopRecording() {
        return mCurrentRecordingMode == RecordManager.STOP_MODE;
    }

    private void setRecordingMode(int mode) {
        mCurrentRecordingMode = mode;
    }
}

