package com.example.playlistmaker.ui.new_playlist.fragment

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreateNewPlaylistBinding
import com.example.playlistmaker.ui.new_playlist.viewmodel.NewPlaylistViewModel
import com.example.playlistmaker.ui.util.dpToPx
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewPlaylistFragment : Fragment() {
    private var _binding: FragmentCreateNewPlaylistBinding? = null
    private val binding get() = _binding!!

    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var nameTextWatcher: TextWatcher
    private lateinit var nameEditText: EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPhotoPicker()
        initClickListeners()
        setEditText()
        setOnBackPressed()
        setObservers()
    }

    private fun setObservers() {
        newPlaylistViewModel.observeCurrentTrackCover().observe(viewLifecycleOwner) {
            setTrackCover(it)
        }

        newPlaylistViewModel.observePlaylistSaved().observe(viewLifecycleOwner) {
            val text = getString(R.string.new_playlist_toast_text, nameEditText.text.toString())
            Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        newPlaylistViewModel.observeShouldShowDialog().observe(viewLifecycleOwner) {
            if (it) {
                createDialog()
            } else {
                findNavController().navigateUp()
            }
        }
    }

    private fun initClickListeners() {
        binding.newPlaylistArrowBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.newPlaylistTrackCover.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.newPlaylistCreateBtn.setOnClickListener {
            newPlaylistViewModel.savePlaylist(
                nameEditText.text.toString(),
                binding.newPlaylistDescription.text.toString()
            )
        }
    }

    private fun createPhotoPicker() {
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            newPlaylistViewModel.setTrackCover(it)
        }
    }

    private fun setEditText() {
        nameTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.newPlaylistCreateBtn.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        nameEditText = binding.newPlaylistName
        nameEditText.addTextChangedListener(nameTextWatcher)
    }

    private fun setOnBackPressed() {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                newPlaylistViewModel.checkShouldShowDialog(
                    nameEditText.text.toString(),
                    binding.newPlaylistDescription.text.toString()
                )
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    private fun createDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.new_playlist_dialog_title))
            .setMessage(getString(R.string.new_playlist_dialog_message))
            .setNeutralButton(getString(R.string.new_playlist_dialog_cancel_btn)) { _, _ ->

            }
            .setPositiveButton(getString(R.string.new_playlist_dialog_confirm_exit_btn)) { _, _ ->
                findNavController().popBackStack()
            }
            .show()
    }

    private fun setTrackCover(uri: Uri) {
        binding.newPlaylistPlaceHolder.isVisible = false
        Glide.with(this)
            .load(uri)
            .transform(
                MultiTransformation(
                    CenterCrop(),
                    RoundedCorners(dpToPx(CORNER_RADIUS, requireContext()))
                )
            )
            .into(binding.newPlaylistTrackCover)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateNewPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        nameEditText.removeTextChangedListener(nameTextWatcher)
    }

    companion object {
        const val CORNER_RADIUS = 8f
    }
}