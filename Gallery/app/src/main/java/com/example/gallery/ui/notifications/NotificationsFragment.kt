package com.example.gallery.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.gallery.databinding.FragmentNotificationsBinding
import com.example.gallery.ui.adapter.AlbumAdapter
import com.example.gallery.ui.adapter.MediaAdapter
import com.example.gallery.viewmodel.MediaViewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val viewModel: MediaViewModel by viewModels()
    private lateinit var adapter: AlbumAdapter

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        viewModel.albums.observe(viewLifecycleOwner) {
            binding.recyclerView.adapter = AlbumAdapter(it)
        }
        viewModel.loadAlbums()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}