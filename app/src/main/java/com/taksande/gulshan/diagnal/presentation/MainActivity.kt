package com.taksande.gulshan.diagnal.presentation

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.taksande.gulshan.diagnal.R
import com.taksande.gulshan.diagnal.databinding.ActivityMainBinding
import com.taksande.gulshan.diagnal.paging.BooksAdapter
import com.taksande.gulshan.diagnal.paging.ItemMarginDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var booksAdapter: BooksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // to give default dark mode, as per provided mockup
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

        init()

    }

    private fun init() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.searchIcon.setOnClickListener {
            showSearch(true)
            binding.txtSearch.requestFocus()
            showSoftKeyboard(binding.txtSearch, true)
        }

        binding.cancelIcon.setOnClickListener {
            viewModel.clearQuery()
            showSearch(false)
            showSoftKeyboard(binding.txtSearch, false)
        }
        binding.txtTitle.text = getString(R.string.title)

        setupRv()


        viewModel.getBooksList().observe(this) { pagingData ->
            booksAdapter.submitData(lifecycle, pagingData)
        }
    }

    private fun showSearch(status: Boolean) {
        if (status) {
            binding.llTitle.visibility = View.GONE
            binding.llSearch.visibility = View.VISIBLE
        } else {
            binding.llSearch.visibility = View.GONE
            binding.llTitle.visibility = View.VISIBLE
        }
    }

    private fun setupRv() {
        val spanCount = getSpanCount()
        binding.recyclerview.layoutManager = GridLayoutManager(this, spanCount)
        binding.recyclerview.addItemDecoration(ItemMarginDecoration(15, 30))

        booksAdapter = BooksAdapter()
        binding.recyclerview.adapter = booksAdapter
    }

    private fun getSpanCount(): Int {
        val isPortrait = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        val spanCount = if (isPortrait) {
            resources.getInteger(R.integer.span_count_portrait)
        } else {
            resources.getInteger(R.integer.span_count_landscape)
        }
        return spanCount
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            // to clear the searchQuery after first time back is pressed
            if (!viewModel.isQueryBlank()) {
                viewModel.clearQuery()
                return
            }
            finish()
        }
    }

    private fun showSoftKeyboard(view: View, show: Boolean) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (show) imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        else imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}