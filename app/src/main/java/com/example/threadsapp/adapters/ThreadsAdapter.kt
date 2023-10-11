package com.example.threadsapp.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.threadsapp.R
import com.example.threadsapp.databinding.MainThreadsBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.util.CalculateTime
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel

class ThreadsAdapter(
    private var threads: List<PostView>,
    private val viewModel: SomeoneProfileViewModel
) : RecyclerView.Adapter<ThreadsAdapter.ViewHolder>() {

    var onClickListener: ListClickListener<PostView>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MainThreadsBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val thread = threads[position]
        holder.bind(thread)
        holder.itemView.setOnClickListener {
            onClickListener?.onClick(thread, position, thread.id)
        }
    }

    override fun getItemCount() = threads.size

    fun updateData(newList: List<PostView>) {
        val diffResult = DiffUtil.calculateDiff(
            ProductDiffCallback(
                threads,
                newList
            )
        )
        threads = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: MainThreadsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var isLiked = false

        @SuppressLint("SetTextI18n")
        fun bind(thread: PostView) {
            viewModel.getUserProfileById(
                if (thread.repost != null && thread.text == null) thread.repost.author else thread.author,
                onSuccess = { userProfile ->
                    with(binding) {
                        //setup cards
                        if (thread.repost != null && thread.text == null) {
                            binding.imageView3.visibility = View.VISIBLE
                            binding.textReposted.visibility = View.VISIBLE
                        } else if(thread.repost != null && thread.text != null) {
                            binding.cardView.visibility = View.VISIBLE
                        } else {
                            binding.cardView.visibility = View.GONE
                        }

                        //setup reposted text
                        viewModel.getUserProfileById(thread.author,
                            onSuccess = {profile ->
                                textReposted.text = "${profile.username} reposted"
                            },
                            onError = {})

                        //setup images
                        if(thread.repost != null && thread.text == null) {
                            if (thread.repost.image != null) {
                                Glide.with(imageView4).load(thread.repost.image).into(imageView4)
                                imageView4.visibility = View.VISIBLE
                            } else {
                                imageView4.isVisible = false
                            }
                        } else if(thread.repost != null && thread.text != null) {
                            if (thread.repost.image != null) {
                                Glide.with(imageHolder).load(thread.repost.image).into(imageHolder)
                                imageHolder.visibility = View.VISIBLE
                            } else {
                                imageHolder.isVisible = false
                            }
                        } else {
                            if (thread.image != null) {
                                Glide.with(imageView4).load(thread.image).into(imageView4)
                                imageView4.visibility = View.VISIBLE
                            } else {
                                imageView4.isVisible = false
                            }
                        }

                        //setup video
                        if(thread.repost != null && thread.text == null) {
                            if (thread.repost.video != null) {
                                val videoUri = Uri.parse(thread.repost.video)
                                videoView.setVideoURI(videoUri)
                                videoView.visibility = View.VISIBLE
                                videoView.start()
                            } else {
                                videoView.isVisible = false
                            }
                        } else if(thread.repost != null && thread.text != null) {
                            if (thread.repost.video != null) {
                                val videoUri = Uri.parse(thread.repost.video)
                                videoHolder.setVideoURI(videoUri)
                                videoHolder.visibility = View.VISIBLE
                                videoHolder.start()
                            } else {
                                videoHolder.isVisible = false
                            }
                        } else {
                            if (thread.video != null) {
                                val videoUri = Uri.parse(thread.video)
                                videoView.setVideoURI(videoUri)
                                videoView.visibility = View.VISIBLE
                                videoView.start()
                            } else {
                                videoView.isVisible = false
                            }
                        }

                        //setup username
                        if (thread.repost != null && thread.text != null) {
                            viewModel.getUserProfileById(thread.repost.author,
                                onSuccess = { profile -> usernameThread.text = profile.username },
                                onError = {})
                            username.text = userProfile.username
                        } else if(thread.repost != null) {
                            username.text = userProfile.username
                        } else {
                            username.text = userProfile.username
                        }

                        //setup user profile photo
                        if (thread.repost != null && thread.text != null) {
                            viewModel.getUserProfileById(thread.repost.author,
                                onSuccess = { profile ->
                                    profile.photo?.let { photoUrl ->
                                        if (photoUrl.isEmpty()) {
                                            Glide.with(binding.root.context).load(R.drawable.profile_photo).circleCrop().into(binding.profilePhotoThread3)
                                        } else {
                                            Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.profilePhotoThread3)
                                        }
                                    }
                                },
                                onError = {})
                            userProfile.photo?.let { photoUrl ->
                                if (photoUrl.isEmpty()) {
                                    Glide.with(binding.root.context).load(R.drawable.profile_photo).circleCrop().into(binding.avatar)
                                } else {
                                    Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.avatar)
                                }
                            }
                        } else if (thread.repost != null) {
                            userProfile.photo?.let { photoUrl ->
                                if (photoUrl.isEmpty()) {
                                    Glide.with(binding.root.context).load(R.drawable.profile_photo).circleCrop().into(binding.avatar)
                                } else {
                                    Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.avatar)
                                }
                            }
                        } else {
                            userProfile.photo?.let { photoUrl ->
                                if (photoUrl.isEmpty()) {
                                    Glide.with(binding.root.context).load(R.drawable.profile_photo).circleCrop().into(binding.avatar)
                                } else {
                                    Glide.with(binding.root.context).load(photoUrl).circleCrop().into(binding.avatar)
                                }
                            }
                        }

                        // setup text
                        if (thread.repost != null && thread.text != null) {
                            textThread.text = thread.repost.text
                            threadText.text = thread.text
                        } else if (thread.repost != null) {
                            threadText.text = thread.repost.text
                        } else {
                            threadText.text = thread.text
                        }

                        // setup time
                        if (thread.repost != null && thread.text != null) {
                            val timeDifference = CalculateTime.calculateTimeDifference(thread.repost.date_posted)
                            val timeDifference2 = CalculateTime.calculateTimeDifference(thread.date_posted)
                            quoteTime.text = timeDifference
                            time.text = timeDifference2
                        } else if(thread.repost != null) {
                            val timeDifference = CalculateTime.calculateTimeDifference(thread.date_posted)
                            time.text = timeDifference
                        } else {
                            val timeDifference = CalculateTime.calculateTimeDifference(thread.date_posted)
                            time.text = timeDifference
                        }

                        if (userProfile.is_followed == "Followed" || userProfile.is_followed == "Mutual Follow") {
                            binding.subscribeBtn.visibility = View.GONE
                        }

                        //setup likes
                        if (thread.repost != null && thread.text != null) {
                            likesQuote.text = "${thread.total_likes} likes"
                            likes.text = "${thread.total_likes} likes"
                        } else if(thread.repost != null) {
                            likes.text = "${thread.total_likes} likes"
                        } else {
                            likes.text = "${thread.total_likes} likes"
                        }

                        isLiked = if(thread.user_like) {
                            likeBtn.setImageResource(R.drawable.like_btn_pressed)
                            true
                        } else {
                            likeBtn.setImageResource(R.drawable.lke_btn)
                            false
                        }

                        binding.likeBtn.setOnClickListener {
                            onClickListener?.onLikeClick(thread, adapterPosition, thread.id)
                            isLiked = !isLiked
                            updateLikeBtn(isLiked, thread)
                        }
                        binding.commentBtn.setOnClickListener {
                            onClickListener?.onCommentClick(thread, adapterPosition, thread.id)
                        }
                        binding.shareBtn.setOnClickListener {
                            onClickListener?.onShareClick(thread, adapterPosition)
                        }
                        binding.repostBtn.setOnClickListener {
                            onClickListener?.onRepostClick(thread, adapterPosition, thread.id)
                        }
                        binding.avatar.setOnClickListener {
                            onClickListener?.onPhotoClick(thread, adapterPosition, thread.id)
                        }
                        binding.subscribeBtn.setOnClickListener {
                            onClickListener?.onFollowClick(thread, adapterPosition, thread.id)
                            binding.subscribeBtn.visibility = View.GONE
                        }
                    }
                },
                onError = {

                }
            )
        }

        private fun updateLikeBtn(isLiked: Boolean, thread: PostView) {
            if (isLiked) {
                binding.likeBtn.setImageResource(R.drawable.like_btn_pressed)
                binding.likes.text = "${thread.total_likes + 1} likes"
            } else {
                binding.likeBtn.setImageResource(R.drawable.lke_btn)
                binding.likes.text = "${thread.total_likes - 1} likes"
            }
        }
    }

    interface ListClickListener<T> {
        fun onClick(data: T, position: Int, id: Int)
        fun onCommentClick(data: T, position: Int, id: Int)
        fun onRepostClick(data: T, position: Int, id: Int)
        fun onShareClick(data: T, position: Int)
        fun onLikeClick(data: T, position: Int, id: Int)
        fun onPhotoClick(data: T, position: Int, id: Int)
        fun onFollowClick(data: T, position: Int, id: Int)
    }

    class ProductDiffCallback(
        private val oldList: List<PostView>,
        private val newList: List<PostView>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}