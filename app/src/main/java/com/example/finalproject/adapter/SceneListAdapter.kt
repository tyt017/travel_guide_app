package com.example.finalproject.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.finalproject.R
import com.example.finalproject.data.Scene
import com.example.finalproject.databinding.FragmentAboutTheSceneBinding
import com.example.finalproject.databinding.LayoutItemBinding
import com.example.finalproject.touchHelper.ITHelperInterface
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import java.net.URI
import java.util.*
import kotlin.coroutines.coroutineContext

class SceneListAdapter (private val onItemClicked: (Scene)->Unit):
    ListAdapter<Scene, SceneListAdapter.SceneViewHolder>(DiffCallback)
    , ITHelperInterface {

    var unAssignList = listOf<Scene>()


    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<Scene>() {
            override fun areItemsTheSame(oldScene: Scene, newScene: Scene): Boolean {
                return oldScene.id == newScene.id
            }

            override fun areContentsTheSame(oldScene: Scene, newScene: Scene): Boolean {
                return oldScene == newScene
            }
        }
    }

    // inner class for holding view items (viewholder)
    class SceneViewHolder(val binding:LayoutItemBinding):
        RecyclerView.ViewHolder(binding.root) {// apply view binding
    // assign the data content to the view holder
    //綁定當地變數與dataModel中的每個值
    fun bind(scene: Scene) {
        //binding.sceneImageView.setImageResource(R.drawable.ic_launcher_foreground)

        //binding.sceneImageView.setImageURI(Uri.parse(scene.imageUrl))
        /*binding.sceneImageView.apply {
            scaleType = ImageView.ScaleType.CENTER_CROP
            load(Uri.parse(scene.imageUrl)) {
                size(20, 20) // in pixels
            }// binding. 的後面要放binding xml 檔案的id
        }*/


//        Log.d(scene.imageUrl)
        binding.sceneTextView.text = scene.locationName//等號右邊要放 item 裡面的參數
        binding.sceneImageView.load(Uri.parse(scene.imageUrl)) {
            size(60, 60)
        }
        }
    }

    //創建一個新的viewholder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SceneViewHolder {

        //負責找尋res/layout 下的佈局檔(xml)，功能類似findViewById(); 即載入項目模板
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutItemBinding.inflate(layoutInflater, parent, false)
        //等號右邊的 LayoutItemBinding 要改成相對應的 listAdapter 詳細設計的 xml
        val viewHolder = SceneViewHolder(binding)
        return viewHolder
    }


    override fun onBindViewHolder(holder: SceneViewHolder, position: Int) {
        //將正確的viewholder連接到正確的positon
        val currentScene = getItem(position)
        //呼叫上面的bind方法來綁定資料
        holder.bind(currentScene)
        //滑動畫面時，將資料位置綁至新的欄位，用新數據將之取代(RecyclerView特色)
        holder.itemView.setOnClickListener { onItemClicked(currentScene) }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        Collections.swap(unAssignList,fromPosition,toPosition)
        notifyItemMoved(fromPosition,toPosition)
    }

    //左右滑刪除的實做
    override fun onItemDissmiss(position: Int) {

       // unAssignList.

        notifyItemRemoved(position)
    }


}