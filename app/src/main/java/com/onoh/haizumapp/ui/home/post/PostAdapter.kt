package com.onoh.haizumapp.ui.home.post

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onoh.haizumapp.R
import com.onoh.haizumapp.data.model.Post
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple
import kotlinx.android.synthetic.main.item_post.view.*

class PostAdapter(val context: Context):RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    private var listPost = ArrayList<Post?>()

    fun setPost(posts: List<Post?>){
        listPost.clear()
        listPost.addAll(posts)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = listPost[position]
        if(post!= null) {
            when (post.viewType) {
                "1" -> holder.textBind(post)
                "2" -> holder.imageBind(post)
                "3" -> holder.videoBind(post)
            }
        }
    }

    override fun getItemCount(): Int = listPost.size

    class PostViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        fun textBind(post:Post){
            with(itemView) {
                layout_text.visibility = View.VISIBLE
                layout_image.visibility = View.GONE
                layout_video.visibility = View.GONE


                Glide.with(context)
                    .load(post.photoProfile)
                    .placeholder(R.drawable.ic_account_circle_black)
                    .into(img_item_post_profile)
                tv_item_post_text.text = post.text
                tv_item_post_name.text = post.name
                tv_item_post_time.text = post.time
            }
        }

        fun imageBind(post:Post){
            with(itemView){
                layout_text.visibility = View.GONE
                layout_image.visibility = View.VISIBLE
                layout_video.visibility = View.GONE

                tv_item_post_text_image.text = post.text
                tv_item_post_name.text = post.name
                tv_item_post_time.text = post.time
                Glide.with(context)
                    .load(post.imgUrl)
                    .placeholder(R.drawable.ic_img_thumbnail)
                    .into(img_post)
            }
        }

        fun videoBind(post:Post){
            with(itemView){
                layout_text.visibility = View.GONE
                layout_image.visibility = View.GONE
                layout_video.visibility = View.VISIBLE

                tv_item_post_text_video.text = post.text
                tv_item_post_name.text = post.name
                tv_item_post_time.text = post.time
                video_post.setUp(post.videoUrl,JCVideoPlayerSimple.SCREEN_LAYOUT_NORMAL,"")
                val mediaController = MediaController(context)
                mediaController.setAnchorView(video_post)
            }
        }

    }


}