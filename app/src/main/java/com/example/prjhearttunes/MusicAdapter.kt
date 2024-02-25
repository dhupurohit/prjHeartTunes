package com.example.prjhearttunes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MusicAdapter(private val context : Activity, private val arraylist : ArrayList<SongDetails> ) : ArrayAdapter<SongDetails>
    (context, R.layout.music_view, arraylist)
{
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val inflater : LayoutInflater = LayoutInflater.from(context)
        val view : View = inflater.inflate(R.layout.music_view,null)


        val sngName : TextView = view.findViewById(R.id.songName)
        val sngArtist : TextView = view.findViewById(R.id.songArtist)
        val sngDuration : TextView = view.findViewById(R.id.songDuration)


        sngName.text = formatSongName(arraylist[position].title)
        sngDuration.text = formatDuration(arraylist[position].duration)
        sngArtist.text = arraylist[position].artist

        return view
    }


}