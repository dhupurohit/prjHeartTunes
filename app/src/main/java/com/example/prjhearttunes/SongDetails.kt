package com.example.prjhearttunes

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import java.lang.reflect.Field
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

data class SongDetails(//var id: String ,
    var title: String,
    var duration: Long = 0,
    //var album: String,
    var artist: String
)
//val path: String,
//val artUri: String)
//var date: String)

fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = (TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            minutes * TimeUnit.SECONDS.convert(1, TimeUnit.MINUTES))
    return String.format("%02d:%02d", minutes, seconds)
}

fun formatSongName(name: String): String {
    var Title = name.replace("_", " ", false)
    Title = Title.capitalize()
    return Title
}

var sngArtistArray = arrayListOf(
    "Kishore Kumar, Lata Mangeshkar",                   //0
    "Md.Rafi, Lata Mangeshkar",                         //1
//"Ankit Tiwari",
    "Nikhil D'Souza",                                   //2
    "Jigar, Priya Panchal and Mahadev Krishnan",        //3
    "Arijit Singh",                                     //4
    "Pritam Chakraborty",                               //5
    "Shreya Ghoshal and Sonu Nigam",                    //6
    "Kailash Kher",                                     //7
    "Shreya Ghoshal and Tochi Raina",                   //8
    "Arijit Singh and Shreya Ghoshal",                  //9
    "Amit Trivedi",                                     //10
    "Atif Aslam"                                        //11
)

lateinit var sngIdArray: ArrayList<Int>
lateinit var sngTitleArray: ArrayList<String>
lateinit var sngDurationArray: ArrayList<String>
lateinit var MediaPlayerArray: ArrayList<MediaPlayer>
lateinit var CurrSongIDArray: ArrayList<Int>
lateinit var CurrSongTitleArray: ArrayList<String>
lateinit var CurrSongDurationArray: ArrayList<String>
lateinit var CurrSongArtistArray: ArrayList<String>
lateinit var favSongIDArray: ArrayList<Int>
lateinit var favList: ArrayList<SongDetails>
lateinit var favIdList: ArrayList<Int>
lateinit var favSongTitleArray: ArrayList<String>
lateinit var favSongDurationArray: ArrayList<String>
lateinit var favSongArtistArray: ArrayList<String>

fun setAllArray(context: Context) {

    sngTitleArray = ArrayList()
    sngDurationArray = ArrayList()
    sngIdArray = ArrayList()
    MediaPlayerArray = ArrayList()
    favList = ArrayList()
    favIdList = ArrayList()
    favSongArtistArray = ArrayList()
    favSongIDArray = ArrayList()
    favSongDurationArray = ArrayList()
    favSongTitleArray = ArrayList()

    val fields: Array<Field> = com.example.prjhearttunes.R.raw::class.java.getFields()
    fields.forEach { field ->

        sngTitleArray.add(field.name)
        sngIdArray.add(field.getInt(field))
        MediaPlayerArray.add(
            MediaPlayer.create(context, field.getInt(field))
        )
    }

    for (i in sngTitleArray.indices) {

        sngDurationArray.add(MediaPlayerArray[i].duration.toString())
    }
}


var currSongPref = "currSongPref"
var favSongPref = "favSongPref"
var isSongPlaying = "IsSongPlaying"
var isSongShuffel = "IsSongShuffel"
var shuffle = false
var sngIndex = 0
var seekTime = 0
var duration: Long = 0
var firstAttempt = true
var diffArray = false
var mp = MediaPlayer()

fun playSong(context: Context, mpId: Int, img: ImageView?, isIt: Boolean): Boolean {

//    Log.e("myError", "playsong Called")
    var isit = isIt

    if (firstAttempt) {
        mp = MediaPlayer.create(context, mpId)
        seekTime = 0

        if (!isit) {
            mp.seekTo(seekTime)
//            Log.e("myError", "fa = before start Seek Time = " + seekTime)
            mp.start()
            isit = true
            firstAttempt = false
            img?.setImageResource(R.drawable.ic_pause)
        } else if (isit) {
            seekTime = mp.currentPosition
//            Log.e("myError", "before pause Seek Time = " + seekTime)
//            Log.e("myError", "before pause cur pos = " + mp.currentPosition)
            mp.pause()
            isit = false
            img?.setImageResource(R.drawable.ic_play_)
        }
    } else {
        mp.pause()
        seekTime = mp.currentPosition

        if (!isit) {
            mp.seekTo(seekTime)
//            Log.e("myError", "before start Seek Time = " + seekTime)
            mp.start()
            isit = true
            firstAttempt = false
            img?.setImageResource(R.drawable.ic_pause)
        } else if (isit) {
            seekTime = mp.currentPosition
//            Log.e("myError", "before pause Seek Time = " + seekTime)
//            Log.e("myError", "before pause cur pos = " + mp.currentPosition)
            mp.pause()
            isit = false
            img?.setImageResource(R.drawable.ic_play_)
        }
    }
    duration = mp.duration.toLong()
    return isit
}

fun setAllTextMA(
    sngpref: SharedPreferences,
    name: TextView,
    artist: TextView,
    dur: TextView?
): Int {

    val mpId = sngpref.getInt("CurrSongId", 0)
    sngIndex = sngpref.getInt("CurrSongIndex", 0)
    val title = sngpref.getString("CurrSongName", "")
    name.text = title?.let { formatSongName(it) }
    artist.text = sngpref.getString("CurrSongArtist", "")
    duration = sngpref.getInt("CurrSongDuration", 0).toLong()
    dur?.text = formatDuration(duration)

    return mpId
}

fun playNextSong(context: Context, mpId: Int, fav: Boolean) {


//    Log.e("myError", "playNext Called")

    mp.reset()
    seekTime = 0
    firstAttempt = true
    val id: Int

    sngIndex += 1
//    Log.e("myError", "sngIndex + 1 = " + sngIndex)

    if (diffArray) {
        val isFav = fav

        if (isFav)
        {
            Log.e("myError", "Inside isFav in PlayNextSong")
            if (sngIndex < favSongIDArray.size) {
                id = favSongIDArray[sngIndex]
            } else {
                id = favSongIDArray[0]
                sngIndex = 0
            }

            val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
            var editor = sp.edit()
            editor.putInt("CurrSongId", favSongIDArray[sngIndex])
            editor.putInt("CurrSongIndex", sngIndex)
            editor.putString("CurrSongName", favSongTitleArray[sngIndex])
            editor.putString("CurrSongArtist", favSongArtistArray[sngIndex])
            editor.putInt("CurrSongDuration", favSongDurationArray[sngIndex].toInt())
            editor.apply()

            var isIt = false

            isIt = playSong(context, id, null, isIt)

            val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
            editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
        }
        else {
//        Log.e("myError", "size = " + CurrSongArtistArray.size)
            if (sngIndex < CurrSongIDArray.size) {
//            ID = mpId + 1
                id = CurrSongIDArray[sngIndex]
//            Log.e("myError", "ID = " + ID)
//            Log.e("myError", "sngIndex = " + sngIndex)
            } else {
                id = CurrSongIDArray[0]
                sngIndex = 0
            }


            val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
            var editor = sp.edit()
            editor.putInt("CurrSongId", CurrSongIDArray[sngIndex])
            editor.putInt("CurrSongIndex", sngIndex)
            editor.putString("CurrSongName", CurrSongTitleArray[sngIndex])
            editor.putString("CurrSongArtist", CurrSongArtistArray[sngIndex])
            editor.putInt("CurrSongDuration", CurrSongDurationArray[sngIndex].toInt())
            editor.apply()
//        editor.commit()

            var isIt = false

            isIt = playSong(context, id, null, isIt)

            val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
            editor = isPlayingShPref.edit()
            editor.putBoolean("IsPlaying", isIt)
            editor.apply()
//        editor.commit()
        }
    }
    else {
        if (sngIndex <= 11) {
            id = mpId + 1
//            Log.e("myError", "ID = " + id)
//            Log.e("myError", "sngIndex = " + sngIndex)
        } else {
            id = sngIdArray[0]
            sngIndex = 0
        }


        val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putInt("CurrSongId", sngIdArray[sngIndex])
        editor.putInt("CurrSongIndex", sngIndex)
        editor.putString("CurrSongName", sngTitleArray[sngIndex])
        editor.putString("CurrSongArtist", sngArtistArray[sngIndex])
        editor.putInt("CurrSongDuration", sngDurationArray[sngIndex].toInt())
        editor.apply()
        editor.commit()

        var isIt = false

        isIt = playSong(context, id, null, isIt)

        val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
        editor = isPlayingShPref.edit()
        editor.putBoolean("IsPlaying", isIt)
        editor.apply()
        editor.commit()
    }
}

fun playPrevSong(context: Context, mpId: Int) {


//    Log.e("myError", "playNext Called")

    mp.reset()
    seekTime = 0
    firstAttempt = true
    val id: Int

    sngIndex -= 1
//    Log.e("myError", "sngIndex - 1 = " + sngIndex)

    if (diffArray) {

//        Log.e("myError", "size = " + CurrSongArtistArray.size)
        if (sngIndex >= 0 && sngIndex < CurrSongArtistArray.size) {
//            ID = mpId + 1
            id = CurrSongIDArray[sngIndex]
//            Log.e("myError", "ID = " + ID)
//            Log.e("myError", "sngIndex = " + sngIndex)
        } else {
            id = CurrSongIDArray[CurrSongArtistArray.size - 1]
            sngIndex = CurrSongArtistArray.size - 1
        }


        val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putInt("CurrSongId", CurrSongIDArray[sngIndex])
        editor.putInt("CurrSongIndex", sngIndex)
        editor.putString("CurrSongName", CurrSongTitleArray[sngIndex])
        editor.putString("CurrSongArtist", CurrSongArtistArray[sngIndex])
        editor.putInt("CurrSongDuration", CurrSongDurationArray[sngIndex].toInt())
        editor.apply()
//        editor.commit()

        var isIt = false

        isIt = playSong(context, id, null, isIt)

        val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
        editor = isPlayingShPref.edit()
        editor.putBoolean("IsPlaying", isIt)
//        editor.commit()
        editor.apply()
    }
    else {

        if (sngIndex >= 0) {
            id = mpId - 1
//            Log.e("myError", "ID = " + ID)
//            Log.e("myError", "sngIndex = " + sngIndex)
        } else {
            id = sngIdArray[11]
            sngIndex = 11
        }


        val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
        var editor = sp.edit()
        editor.putInt("CurrSongId", sngIdArray[sngIndex])
        editor.putInt("CurrSongIndex", sngIndex)
        editor.putString("CurrSongName", sngTitleArray[sngIndex])
        editor.putString("CurrSongArtist", sngArtistArray[sngIndex])
        editor.putInt("CurrSongDuration", sngDurationArray[sngIndex].toInt())
        editor.apply()
        editor.commit()

        var isIt = false

        isIt = playSong(context, id, null, isIt)

        val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
        editor = isPlayingShPref.edit()
        editor.putBoolean("IsPlaying", isIt)
        editor.apply()
        editor.commit()
    }
}

fun initializeSeekBar(sb: SeekBar, handler: android.os.Handler) {

//    var runnable = Runnable {
//        sb.progress = mp.currentPosition
//        handler.postDelayed(runnable, 1000)
//    }

    sb.progress = 0

    handler.postDelayed(object : Runnable {

        override fun run() {
            try {
                seekTime = mp.currentPosition
                sb.progress = seekTime
                sb.max = duration.toInt()
                handler.postDelayed(this, 100)
            } catch (e: Exception) {
                seekTime = 0
                sb.progress = seekTime
            }
        }
    }, 0)


}

fun shuffelSongs(context: Context, mpId: Int) {


//    Log.e("myError", "shuffel Called")

    mp.reset()
    seekTime = 0
    firstAttempt = true
    val id: Int

    sngIndex += 2
//    Log.e("myError", "sngIndex + 1 = " + sngIndex)

    if (sngIndex <= 11) {
        id = mpId + 2
//        Log.e("myError", "ID = " + ID)
//        Log.e("myError", "sngIndex = " + sngIndex)
    } else {
        id = sngIdArray[0]
        sngIndex = 0
    }


    val sp = context.getSharedPreferences(currSongPref, Context.MODE_PRIVATE)
    var editor = sp.edit()
    editor.putInt("CurrSongId", sngIdArray[sngIndex])
    editor.putInt("CurrSongIndex", sngIndex)
    editor.putString("CurrSongName", sngTitleArray[sngIndex])
    editor.putString("CurrSongArtist", sngArtistArray[sngIndex])
    editor.putInt("CurrSongDuration", sngDurationArray[sngIndex].toInt())
    editor.apply()
    editor.commit()

    var isIt = false

    isIt = playSong(context, id, null, isIt)

    val isPlayingShPref = context.getSharedPreferences(isSongPlaying, Context.MODE_PRIVATE)
    editor = isPlayingShPref.edit()
    editor.putBoolean("IsPlaying", isIt)
    editor.apply()
    editor.commit()

}


fun repeat(img: ImageView) {
    val islp = mp.isLooping

    if (islp) {
        mp.isLooping = false
        img.setImageResource(R.drawable.ic_repeat)
    } else {
        mp.isLooping = true
        img.setImageResource(R.drawable.ic_repeat_one)
    }
}

fun fav(img: ImageView) {
    val islp = mp.isLooping

    if (islp) {

        img.setImageResource(R.drawable.ic_favorite_borde)
    } else {

        img.setImageResource(R.drawable.ic_favorite_fill)
    }
}


var currThemePref = "CurrentThemeIs"
var currTheme = 0

fun setTheme(activity: Activity){

    when(currTheme)
    {
        0 -> {
            activity.setTheme(R.style.AppTheme_Purple)
        }

        1 -> {
            activity.setTheme(R.style.AppTheme_DarkBlue)
        }

        2 -> {
            activity.setTheme(R.style.AppTheme_Green)
        }

        3 -> {
            activity.setTheme(R.style.AppTheme_Brown)
        }
    }

}

//data class SongDetails(val id:String, val title:String, val album:String ,val artist:String, val duration: Long = 0, val path: String)
