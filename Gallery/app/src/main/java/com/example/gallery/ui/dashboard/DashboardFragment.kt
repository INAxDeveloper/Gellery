package com.example.gallery.ui.dashboard

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
import com.example.gallery.databinding.FragmentDashboardBinding
import com.example.gallery.ui.adapter.MediaAdapter
import com.example.gallery.viewmodel.MediaViewModel

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val viewModel: MediaViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        viewModel.mediaList.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = MediaAdapter(it)
        }

        loadImages()
    }
    fun loadImages() {
        viewModel.loadMedia(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}