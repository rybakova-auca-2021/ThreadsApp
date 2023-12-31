package com.example.threadsapp.view.profile

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.example.neobis_android_chapter8.viewModels.AuthViewModel.UserInfoViewModel
import com.example.threadsapp.R
import com.example.threadsapp.adapters.MyPostsAdapter
import com.example.threadsapp.databinding.FragmentProfileBinding
import com.example.threadsapp.model.HomeModel.PostView
import com.example.threadsapp.viewModel.newThreadViewModel.DeleteThreadViewModel
import com.example.threadsapp.viewModel.postViewModel.LikeUnlikeViewModel
import com.example.threadsapp.viewModel.profileViewModel.LogoutViewModel
import com.example.threadsapp.viewModel.profileViewModel.MyPostsViewModel
import com.example.threadsapp.viewModel.profileViewModel.SomeoneProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private val viewModel: UserInfoViewModel by viewModels()
    private val logoutViewModel: LogoutViewModel by viewModels()
    private val postViewModel: MyPostsViewModel by viewModels()
    private val deleteViewModel: DeleteThreadViewModel by viewModels()
    private val likeViewModel: LikeUnlikeViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyPostsAdapter
    var username: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefreshLayout = binding.swipeRefreshLayout

        swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }

        setupNavigation()
        showUserData()
        viewModel.getInfo()

        adapter = MyPostsAdapter(emptyList(), SomeoneProfileViewModel())
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        postViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
        })

        getPosts()
        setupClicks()
    }

    private fun refreshData() {
        showUserData()
        viewModel.getInfo()
        getPosts()
        swipeRefreshLayout.isRefreshing = false
    }

    private fun setupNavigation() {
        binding.editProfileBtn.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)
        }
        binding.logoutBtn.setOnClickListener {
            showLogoutDialog()
        }
        binding.numOfFollowers.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToFollowFragment(username)
            findNavController().navigate(action)
        }
        binding.shareProfileBtn.setOnClickListener {
            showImageOptionsBottomSheet()
        }
    }

    private fun showUserData() {
        viewModel.profileData.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                username = profile.username
                binding.name.text = it.full_name
                binding.username.text = it.username
                profile.photo.let { photoUrl ->
                    if (it.photo.isNullOrEmpty()) {
                        Glide.with(this).load(R.drawable.profile_photo).into(binding.profilePhoto)
                    } else {
                        val photoUrl = it.photo
                        Glide.with(this).load(photoUrl).circleCrop().into(binding.profilePhoto)
                    }
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getPosts() {
        postViewModel.myPosts(
            onSuccess = { results ->
                adapter.updateData(results)
                adapter.notifyDataSetChanged()
            },
            onError = {
                Toast.makeText(requireContext(), "Try Again", Toast.LENGTH_SHORT).show()
            }
        )
    }

    @SuppressLint("ResourceType")
    private fun createBottomSheetDialog(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.share_profile_bottom_sheet, null)
        dialog.setContentView(view)
        return dialog
    }
    private fun showImageOptionsBottomSheet() {
        val dialog = createBottomSheetDialog()
        val copyBtn = dialog.findViewById<View>(R.id.btn_copy_link)
        val shareBtn = dialog.findViewById<View>(R.id.btn_share_via)

        copyBtn?.setOnClickListener {
            dialog.dismiss()
            // TODO setup possibility to copy link
        }
        shareBtn?.setOnClickListener {
            setupShare()
            dialog.dismiss()
        }
        dialog.show()
    }


    @SuppressLint("ResourceType")
    private fun createBottomSheetDialogRepost(): BottomSheetDialog {
        val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogStyle)
        val view = layoutInflater.inflate(R.layout.repost_dialog, null)
        dialog.setContentView(view)
        return dialog
    }

    private fun showRepostDialog(id: Int, position: Int) {
        val dialog = createBottomSheetDialogRepost()
        val remove = dialog.findViewById<View>(R.id.button)
        val quote = dialog.findViewById<View>(R.id.button2)

        remove?.setOnClickListener {
            dialog.dismiss()
            deletePost(id, position)
        }
        quote?.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun setupShare() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

        val chooserIntent = Intent.createChooser(shareIntent, "Share via")
        startActivity(chooserIntent)
    }

    private fun showLogoutDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_logout, null)
        val logoutButton: Button = dialogView.findViewById(R.id.btn_logout)
        val cancelButton: Button = dialogView.findViewById(R.id.btn_cancel)

        val alertDialog = AlertDialog.Builder(requireContext(), R.style.LightDialogTheme)
            .setView(dialogView)
            .create()

        logoutButton.setOnClickListener {
            setupLogout()
        }
        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun setupLogout() {
        logoutViewModel.logout(
            onSuccess = {
                findNavController().navigate(R.id.loginFragment)
            },
            onError = {
                showToast("try again")
            }
        )
    }

    private fun setupClicks() {
        adapter.onClickListener = object : MyPostsAdapter.ListClickListener<PostView> {
            override fun onClick(data: PostView, position: Int, id: Int) {
                val action = ProfileFragmentDirections.actionToThreadFragment(id)
                findNavController().navigate(action)
            }

            override fun onCommentClick(data: PostView, position: Int, id: Int) {
                val action = ProfileFragmentDirections.actionToReplyFragment(id)
                findNavController().navigate(action)
            }

            override fun onRepostClick(data: PostView, position: Int, id: Int) {
                showRepostDialog(id, position)
            }

            override fun onShareClick(data: PostView, position: Int) {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hello, check out this awesome app!")

                val chooserIntent = Intent.createChooser(shareIntent, "Share via")
                startActivity(chooserIntent)
            }

            override fun onLikeClick(data: PostView, position: Int, id: Int) {
                likeOrDislike(id)
            }

            override fun onDeleteClick(data: PostView, position: Int, id: Int) {
                deletePost(id, position)
            }
        }
    }

    private fun likeOrDislike(id: Int) {
        likeViewModel.likeOrUnlike(id)
    }

    private fun deletePost(id: Int, position: Int) {
        deleteViewModel.deletePost(
            id,
            onSuccess = {
                adapter.removeItem(position)
            }
        )
    }
}