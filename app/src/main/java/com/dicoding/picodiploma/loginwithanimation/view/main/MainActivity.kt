package com.dicoding.picodiploma.loginwithanimation.view.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMainBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.addstory.AddStoryActivity
import com.dicoding.picodiploma.loginwithanimation.view.maps.MapsActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var fabAddStory: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        fabAddStory = binding.fabAddStory
        fabAddStory.setOnClickListener {
            startActivity(Intent(this, AddStoryActivity::class.java))
        }

        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        val adapter = MainAdapter(this)
        binding.recyclerView.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.getSession().observe(this) { user ->
            if (!user!!.isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            } else {
                lifecycleScope.launch {
                    try {
                        viewModel.getStories().observe(this@MainActivity) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            this@MainActivity,
                            "Error loading stories",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                viewModel.logout()
                true
            }

            R.id.action_change_language -> {
                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(intent)
                true
            }

            R.id.action_open_maps -> {
                startActivity(Intent(this, MapsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}