package com.example.gallery.ui.home

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.databinding.FragmentHomeBinding
import com.example.gallery.ui.adapter.MediaAdapter
import com.example.gallery.viewmodel.MediaViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel: MediaViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        viewModel.mediaList.observe(viewLifecycleOwner) { mediaList ->
            binding.recyclerView.adapter = MediaAdapter(mediaList)
        }

        loadVideos()

    }
    fun loadVideos() {
        // 👇 Load videos from MediaStore
        viewModel.loadMedia(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}