package com.hiendao.eduschedule.ui.notification

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.core.net.toUri
import com.hiendao.eduschedule.R


class MusicControl(private val context: Context) {
    private var mMediaPlayer: MediaPlayer? = null

    fun playMusic(uri: String?) {
        if(uri != null){
            mMediaPlayer = MediaPlayer.create(context, uri.toUri())
        } else {
            mMediaPlayer = MediaPlayer.create(context, R.raw.alarm)
        }
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val volume = (audioManager.getStreamVolume(AudioManager.STREAM_ALARM)).toFloat()
        mMediaPlayer!!.setVolume(volume, volume)
        mMediaPlayer!!.start()
    }

    fun stopMusic() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.seekTo(0)
        }
    }

    companion object {
        private var sInstance: MusicControl? = null
        fun getInstance(context: Context): MusicControl {
            if (sInstance == null) {
                sInstance = MusicControl(context)
            }
            return sInstance!!
        }
    }
}